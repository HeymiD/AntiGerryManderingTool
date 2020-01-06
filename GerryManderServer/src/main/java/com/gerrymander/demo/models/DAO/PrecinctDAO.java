package com.gerrymander.demo.models.DAO;

//import com.gerrymander.demo.models.concrete.;
import com.gerrymander.demo.*;
import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;
import com.gerrymander.demo.models.concrete.State;
import com.vividsolutions.jts.geom.Geometry;
import org.wololo.geojson.Feature;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import java.sql.*;
import java.util.*;

public class PrecinctDAO {


    private static final String database_url="jdbc:mysql://mysql4.cs.stonybrook.edu:3306/hdannon?useSSL=false";
    private static final String database_username="hdannon";
    private static final String database_password="110941800";

    public static Map<String,Precinct> populatePrecincts() throws SQLException {
        Map<String,Precinct> precincts = new HashMap<String,Precinct>();
        String queryGEO = "select * from timberwolves.Precincts_GEO";
        Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(queryGEO);
        System.out.println("Loading Precincts");
        while (resultSet.next()){
            Precinct p = new Precinct(resultSet.getString("PCTKEY"));
            p.setGeometryJSON(resultSet.getString("geojson"));
            precincts.put(resultSet.getString("PCTKEY"),p);
        }
        System.out.println("Precincts loaded");
//        String queryDemographics = "select * from timberwolves.texas_demographics";
//        resultSet = statement.executeQuery(queryDemographics);
//        while (resultSet.next()){
//            Precinct currentPrecinct = precincts.get(resultSet.getString("PCTKEY"));
//            currentPrecinct.addDemographic(DEMOGRAPHIC.AFROAM, resultSet.getInt("Black"));
//            currentPrecinct.addDemographic(DEMOGRAPHIC.WHITE, resultSet.getInt("White"));
//            currentPrecinct.addDemographic(DEMOGRAPHIC.ASIAN, resultSet.getInt("Asian"));
//            currentPrecinct.addDemographic(DEMOGRAPHIC.HISPANIC, resultSet.getInt("Hispanic"));
//            currentPrecinct.addDemographic(DEMOGRAPHIC.NATIVE, resultSet.getInt("Native"));
//            currentPrecinct.addDemographic(DEMOGRAPHIC.OTHER, resultSet.getInt("Other"));
//        }
//        for(ELECTIONTYPE e : ELECTIONTYPE.values()){
//            String queryElection = "select * from timberwolves."+e.toString();
//            resultSet = statement.executeQuery(queryElection);
//            while (resultSet.next()){
//                Precinct currentPrecinct = precincts.get(resultSet.getString("PCTKEY"));
//                Votes v = new Votes();
//                Map<PARTYNAME,Integer> votesPrecinct = new HashMap<PARTYNAME,Integer>();
//                votesPrecinct.put(PARTYNAME.DEMOCRAT,resultSet.getInt("Democrat"));
//                votesPrecinct.put(PARTYNAME.REPUBLICAN,resultSet.getInt("Republican"));
//                votesPrecinct.put(PARTYNAME.GREEN,resultSet.getInt("Green"));
//                votesPrecinct.put(PARTYNAME.LIBERTARIAN,resultSet.getInt("Libertarian"));
//                v.setVotes(votesPrecinct);
//                v.setElectiontype(e);
//                currentPrecinct.addVotes(e,v);
//            }
//        }
//
        return precincts;
    }


//    @Override
//    public List<District> get() {
//
////        Session currentSession = entityManager.unwrap(Session.class);
////        Query<District> query = currentSession.createQuery("from District", District.class);
////        List<District> list_of_OldDistricts = query.getResultList();
//        return list_of_OldDistricts;
//    }

    public static Set<Precinct> initPrecinctsforDistrict(ELECTIONTYPE election,
                                                         District dis) {

//        String query = "select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.District, P.geojson " +
//                "from timberwolves.Precincts_GEO P, "+election.toString()+" C, timberwolves.texas_demographics T " +
//                "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY AND C.District=\'"+dis.getID()+"\'";
        String query = "select * " +
                "from timberwolves.PrecinctsGEO " +
                "where District =\'"+dis.getID()+"\'";
        try{
            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                Precinct precinctobj = new Precinct(resultSet.getString("PCTKEY"));
                precinctobj.setGeometryJSON(resultSet.getString("geojson"));
                precinctobj.setOriginalDistrictID(resultSet.getString("District"));
                System.out.println("Key: "+resultSet.getString("PCTKEY"));
                System.out.println("District: "+resultSet.getString("District"));
                dis.putPrecinct(resultSet.getString("PCTKEY"),precinctobj);
                dis.getState().addPrecinct(precinctobj);
            }
//            if(dis.getID().equals("U.S. Rep 36")){
                if(dis.getState().getPrecincts().size()==8936 ){
                query = "select * " +
                        "from timberwolves."+election.toString();
                resultSet = statement.executeQuery(query);
                while(resultSet.next()){
                    Precinct p = dis.getState().getPrecinct(resultSet.getString("PCTKEY"));
                    Votes v = new Votes();
                    Map<PARTYNAME,Integer> votesPrecinct = new HashMap<PARTYNAME,Integer>();
                    votesPrecinct.put(PARTYNAME.DEMOCRAT,resultSet.getInt("Democrat"));
                    votesPrecinct.put(PARTYNAME.REPUBLICAN,resultSet.getInt("Republican"));
                    votesPrecinct.put(PARTYNAME.GREEN,resultSet.getInt("Green"));
                    votesPrecinct.put(PARTYNAME.LIBERTARIAN,resultSet.getInt("Libertarian"));
                    v.setVotes(votesPrecinct);
                    v.setElectiontype(election);
                    p.addVotes(election,v);
                }
                query = "select * " +
                        "from timberwolves.texas_demographics";
                resultSet = statement.executeQuery(query);
                while(resultSet.next()){
                    Precinct precinctobj = dis.getState().getPrecinct(resultSet.getString("PCTKEY"));
                    int totPop = 0;
                    precinctobj.addDemographic(DEMOGRAPHIC.AFROAM, Integer.parseInt(resultSet.getString("Black")));
                    totPop+=Integer.parseInt(resultSet.getString("Black"));
                    precinctobj.addDemographic(DEMOGRAPHIC.WHITE, Integer.parseInt(resultSet.getString("White")));
                    totPop+=Integer.parseInt(resultSet.getString("White"));
                    precinctobj.addDemographic(DEMOGRAPHIC.ASIAN, Integer.parseInt(resultSet.getString("Asian")));
                    totPop+=Integer.parseInt(resultSet.getString("Asian"));
                    precinctobj.addDemographic(DEMOGRAPHIC.HISPANIC, Integer.parseInt(resultSet.getString("Hispanic")));
                    totPop+=Integer.parseInt(resultSet.getString("Hispanic"));
                    precinctobj.addDemographic(DEMOGRAPHIC.NATIVE, Integer.parseInt(resultSet.getString("Native")));
                    totPop+=Integer.parseInt(resultSet.getString("Native"));
                    precinctobj.addDemographic(DEMOGRAPHIC.OTHER, Integer.parseInt(resultSet.getString("Other")));
                    totPop+=Integer.parseInt(resultSet.getString("Other"));
                    precinctobj.addDemographic(DEMOGRAPHIC.PACISLAND, Integer.parseInt(resultSet.getString("Pacific")));
                    totPop+=Integer.parseInt(resultSet.getString("Pacific"));
                    precinctobj.setPopulation(totPop);
                }
//                GerryManderController.precinctsToSend.addAll(dis.getState().getPrecincts());
            }

        }
        catch (Exception e)
        {
            System.out.println(e);
            System.out.println("Query: \n"+query);
            System.out.println("Precinct Database connection Problem");
        }
        return dis.getPrecincts();
    }

    public static Set<Precinct> initAllPrecincts(State state) {

//        String query = "select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.District, P.geojson " +
//                "from timberwolves.Precincts_GEO P, "+election.toString()+" C, timberwolves.texas_demographics T " +
//                "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY AND C.District=\'"+dis.getID()+"\'";
        String query = "";
        try{
            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
            Statement statement = connection.createStatement();
            ResultSet resultSet=null;
            for(ELECTIONTYPE election:ELECTIONTYPE.values()){

                query = "select * " +
                        "from timberwolves."+election.toString();
                resultSet = statement.executeQuery(query);
                while(resultSet.next()){
                    try{
                        Precinct p = state.getPrecinct(resultSet.getString("PCTKEY"));
                        Votes v = new Votes();
                        Map<PARTYNAME,Integer> votesPrecinct = new HashMap<PARTYNAME,Integer>();
                        votesPrecinct.put(PARTYNAME.DEMOCRAT,resultSet.getInt("Democrat"));
                        votesPrecinct.put(PARTYNAME.REPUBLICAN,resultSet.getInt("Republican"));
                        votesPrecinct.put(PARTYNAME.GREEN,resultSet.getInt("Green"));
                        votesPrecinct.put(PARTYNAME.LIBERTARIAN,resultSet.getInt("Libertarian"));
                        v.setVotes(votesPrecinct);
                        v.setElectiontype(election);
                        p.addVotes(election,v);
                    }
                    catch(NullPointerException e){
                        Precinct p = new Precinct(resultSet.getString("PCTKEY"));
                        p.setOriginalDistrictID(resultSet.getString("District"));
                        Cluster d = state.oldDistricts.get(p.getOriginalDistrictID());
                        if (d == null) {
                            d = new Cluster(resultSet.getString("District"));
                            d.setState(state);
                            state.oldDistricts.put(d.getID(),d);
                        }
//                        d.addPrecinct(p);
                        Votes v = new Votes();
                        Map<PARTYNAME,Integer> votesPrecinct = new HashMap<PARTYNAME,Integer>();
                        votesPrecinct.put(PARTYNAME.DEMOCRAT,resultSet.getInt("Democrat"));
                        p.dem_vote = resultSet.getInt("Democrat");
                        votesPrecinct.put(PARTYNAME.REPUBLICAN,resultSet.getInt("Republican"));
                        p.gop_vote = resultSet.getInt("Republican");
                        d.addPrecinct(p);
                        votesPrecinct.put(PARTYNAME.GREEN,resultSet.getInt("Green"));
                        votesPrecinct.put(PARTYNAME.LIBERTARIAN,resultSet.getInt("Libertarian"));
                        v.setVotes(votesPrecinct);
                        v.setElectiontype(election);
                        p.addVotes(election,v);
                        state.addPrecinct(p);
                    }


                }
            }
            query = "select * " +
                    "from timberwolves.texas_demographics";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                Precinct precinctobj = state.getPrecinct(resultSet.getString("PCTKEY"));
//                System.out.println("PCTKEY: "+resultSet.getString("PCTKEY"));
//                System.out.println("Precinct: "+precinctobj.getID());
                int totPop = 0;
                precinctobj.addDemographic(DEMOGRAPHIC.AFROAM, (resultSet.getInt("Black")));
                totPop+=(resultSet.getInt("Black"));
                precinctobj.addDemographic(DEMOGRAPHIC.WHITE, (resultSet.getInt("White")));
                totPop+=(resultSet.getInt("White"));
                precinctobj.addDemographic(DEMOGRAPHIC.ASIAN, (resultSet.getInt("Asian")));
                totPop+=(resultSet.getInt("Asian"));
                precinctobj.addDemographic(DEMOGRAPHIC.HISPANIC, (resultSet.getInt("Hispanic")));
                totPop+=(resultSet.getInt("Hispanic"));
                precinctobj.addDemographic(DEMOGRAPHIC.NATIVE, (resultSet.getInt("Native")));
                totPop+=(resultSet.getInt("Native"));
                precinctobj.addDemographic(DEMOGRAPHIC.OTHER, (resultSet.getInt("Other")));
                totPop+=(resultSet.getInt("Other"));
                precinctobj.addDemographic(DEMOGRAPHIC.PACISLAND,(resultSet.getInt("Pacific")));
                totPop+=(resultSet.getInt("Pacific"));
                precinctobj.setPopulation(totPop);
                precinctobj.cnty=resultSet.getInt("cntykey");
            }



        }
        catch (Exception e)
        {
            System.out.println(e);
            System.out.println("Query: \n"+query);
            System.out.println("Precinct Database connection Problem");
        }
        return state.getPrecincts();
    }

    public static Set<Precinct> getPrecinctGeoJSONByDistrict(String disID,State state){
        try{
            Set<Precinct> precincts = new HashSet<Precinct>();
            String queryGEO = "select * from timberwolves.PrecinctsGEO where District=\""+disID+"\"";
            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryGEO);
            while (resultSet.next()){
                state.getPrecinct(resultSet.getString("PCTKEY"))
                        .setGeometryJSON(resultSet.getString("geojson"));
                precincts.add(state.getPrecinct(resultSet.getString("PCTKEY")));
            }
            return precincts;
        }catch (SQLException e){
            return null;
        }

    }

    public static void getAllPrecinctGeoJSON(State state){
        try{
            Set<Precinct> precincts = new HashSet<Precinct>();
            String queryGEO = "select * from timberwolves.PrecinctsGEO";
            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryGEO);
            while (resultSet.next()){
                state.getPrecinct(resultSet.getString("PCTKEY"))
                        .setGeometryJSON(resultSet.getString("geojson"));
                String geojson = resultSet.getString("geojson");
                GeoJSONReader reader = new GeoJSONReader();
                Feature feature = (Feature) GeoJSONFactory.create(geojson);
                state.getPrecinct(resultSet.getString("PCTKEY")).
                        setGeometry(reader.read(feature.getGeometry()));

            }
            return;
        }catch (SQLException e){
            return;
        }

    }

    public static String getPrecinctGeoJSONById(String precinctId){
        try{
            Set<Precinct> precincts = new HashSet<Precinct>();
            String queryGEO = "select geojson from timberwolves.PrecinctsGEO where PCTKEY='"+precinctId+"'";
//            System.out.println(queryGEO);
            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryGEO);
            resultSet.next();
            return resultSet.getString("geojson");
        }catch (SQLException e){
            return "";
        }

    }



}
