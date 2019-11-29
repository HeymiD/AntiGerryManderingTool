package com.gerrymander.demo.models.DAO;

//import com.gerrymander.demo.models.concrete.;
import com.gerrymander.demo.*;
import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.*;
import java.util.*;

public class PrecinctDAO {


    @Autowired
    private EntityManager entityManager;

    private static final String database_url="jdbc:mysql://mysql4.cs.stonybrook.edu:3306/timberwolves?useSSL=false";
    private static final String database_username="timberwolves";
    private static final String database_password="changeit";

    public static Map<String,Precinct> populatePrecincts() throws SQLException {
        Map<String,Precinct> precincts = new HashMap<String,Precinct>();
        String queryGEO = "select * from timberwolves.Precincts_GEO";
        Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(queryGEO);
        while (resultSet.next()){
            Precinct p = new Precinct(resultSet.getString("PCTKEY"),resultSet.getString("geojson"));
            precincts.put(resultSet.getString("PCTKEY"),p);
        }
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

//    public static Set<Precinct> initPrecinctsforDistrict(String districtID, ELECTIONTYPE election,
//                                                         District dis) {
//
//        String query = "";
//        switch(election){
//            case Presidential2016:
//                query = "select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.District, P.geojson " +
//                        "from timberwolves.Precincts_GEO P, timberwolves.presidential_2016 C, timberwolves.texas_demographics T " +
//                        "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY AND C.District=\'"+districtID+"\'";
//                break;
//            case congressional2016:
//                query="select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.Office, P.geojson " +
//                        "from timberwolves.Precincts_GEO P, timberwolves.congressional_2016 C, timberwolves.texas_demographics T " +
//                        "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY AND C.Office=\'"+districtID+"\'";
//                break;
//            case congressional2018:
//                query="select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.Office, P.geojson " +
//                        "from timberwolves.Precincts_GEO P, timberwolves.congressional_2018 C, timberwolves.texas_demographics T " +
//                        "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY AND C.Office=\'"+districtID+"\'";
//                break;
//        }
//
//
//        try{
//            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
//            Statement statement = connection.createStatement();
//
//            ResultSet resultSet = statement.executeQuery(query);
//            while(resultSet.next()){
//                Precinct precinctobj=null;
//                /*
//                HashMap<DEMOGRAPHIC,Integer> demographics = new HashMap<DEMOGRAPHIC, Integer>();
//                demographics.put(DEMOGRAPHIC.AFROAM, Integer.parseInt(resultSet.getString("Black")));
//                demographics.put(DEMOGRAPHIC.WHITE, Integer.parseInt(resultSet.getString("White")));
//                demographics.put(DEMOGRAPHIC.ASIAN, Integer.parseInt(resultSet.getString("Asian")));
//                demographics.put(DEMOGRAPHIC.HISPANIC, Integer.parseInt(resultSet.getString("Hispanic")));
//                demographics.put(DEMOGRAPHIC.NATIVE, Integer.parseInt(resultSet.getString("Native")));
//                demographics.put(DEMOGRAPHIC.OTHER, Integer.parseInt(resultSet.getString("Other")));
//
//                int totPop = 0;
//                for (DEMOGRAPHIC key : demographics.keySet()){
//                    totPop+=demographics.get(key);
//                }
////              */
//                if (query.contains("Office")){
//                    precinctobj = new Precinct(resultSet.getString("PCTKEY"),
//                            resultSet.getString("geojson"),
//                            resultSet.getString("Office"));
//                    dis.putPrecinct(resultSet.getString("PCTKEY"),precinctobj);
//                }
//                else{
//                    System.out.println("Key: "+resultSet.getString("PCTKEY"));
//                    System.out.println("District: "+resultSet.getString("District"));
//                    precinctobj = new Precinct(resultSet.getString("PCTKEY"),
//                            resultSet.getString("geojson"),
//                            resultSet.getString("District"));
//                    dis.putPrecinct(resultSet.getString("PCTKEY"),precinctobj);
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            System.out.println(e);
//            System.out.println("Query: \n"+query);
//            System.out.println("Precinct Database connection Problem");
//        }
//        return dis.getPrecincts();
//    }

//    public static Map<String,Precinct> populatePrecincts() throws SQLException {
//        Map<String,Precinct> precincts = new HashMap<String,Precinct>();
//        for(ELECTIONTYPE e : ELECTIONTYPE.values()){
//            String query = "select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.District, P.geojson, C.year"
//                    +" from timberwolves.Precincts_GEO P, timberwolves."+e.toString()
//                    +" C, timberwolves.texas_demographics T "+
//                    "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY";
//            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
//            Statement statement = connection.createStatement();
//
//            ResultSet resultSet = statement.executeQuery(query);
//            while(resultSet.next()){
//                Precinct precinct = precincts.get(resultSet.getString("PCTKEY"));
//                if (precinct==null){
//                    Precinct p = new Precinct(resultSet.getString("PCTKEY"),
//                            resultSet.getString("geojson"),
//                            resultSet.getString("District"));
//                    //populate
//                    Votes v = new Votes();
//                    Map<PARTYNAME,Integer> votesPrecinct = new HashMap<PARTYNAME,Integer>();
//                    votesPrecinct.put(PARTYNAME.DEMOCRAT,resultSet.getInt("Democrat"));
//                    votesPrecinct.put(PARTYNAME.REPUBLICAN,resultSet.getInt("Republican"));
//                    votesPrecinct.put(PARTYNAME.GREEN,resultSet.getInt("Green"));
//                    votesPrecinct.put(PARTYNAME.LIBERTARIAN,resultSet.getInt("Libertarian"));
//                    v.setVotes(votesPrecinct);
//                    v.setElectiontype(e);
//                    p.addVotes(e,v);
//                    p.addDemographic(DEMOGRAPHIC.AFROAM, resultSet.getInt("Black"));
//                    p.addDemographic(DEMOGRAPHIC.WHITE, resultSet.getInt("White"));
//                    p.addDemographic(DEMOGRAPHIC.ASIAN, resultSet.getInt("Asian"));
//                    p.addDemographic(DEMOGRAPHIC.HISPANIC, resultSet.getInt("Hispanic"));
//                    p.addDemographic(DEMOGRAPHIC.NATIVE, resultSet.getInt("Native"));
//                    p.addDemographic(DEMOGRAPHIC.OTHER, resultSet.getInt("Other"));
//                    precincts.put(resultSet.getString("PCTKEY"),p);
//                }
//                else{
//                    //populate
//                    Votes v = new Votes();
//                    Map<PARTYNAME,Integer> votesPrecinct = new HashMap<PARTYNAME,Integer>();
//                    votesPrecinct.put(PARTYNAME.DEMOCRAT,resultSet.getInt("Democrat"));
//                    votesPrecinct.put(PARTYNAME.REPUBLICAN,resultSet.getInt("Republican"));
//                    votesPrecinct.put(PARTYNAME.GREEN,resultSet.getInt("Green"));
//                    votesPrecinct.put(PARTYNAME.LIBERTARIAN,resultSet.getInt("Libertarian"));
//                    v.setVotes(votesPrecinct);
//                    v.setElectiontype(e);
//                    precinct.addVotes(e,v);
//                    precinct.addDemographic(DEMOGRAPHIC.AFROAM, resultSet.getInt("Black"));
//                    precinct.addDemographic(DEMOGRAPHIC.WHITE, resultSet.getInt("White"));
//                    precinct.addDemographic(DEMOGRAPHIC.ASIAN, resultSet.getInt("Asian"));
//                    precinct.addDemographic(DEMOGRAPHIC.HISPANIC, resultSet.getInt("Hispanic"));
//                    precinct.addDemographic(DEMOGRAPHIC.NATIVE, resultSet.getInt("Native"));
//                    precinct.addDemographic(DEMOGRAPHIC.OTHER, resultSet.getInt("Other"));
//                }
//            }
//        }
//        return precincts;
//     }
//    public void populatePrecinctVotes(ELECTIONTYPE electiontype,String pctkey){
//        String statement = "select Democrat,Republican,Green,Libertarian from "
//                +electiontype+" where PCTKEY="+pctkey;
//        Query query = entityManager.
//                createNativeQuery(statement, "VotesResult");
//        @SuppressWarnings("unchecked")
//        List<Votes> voteResults = query.getResultList();
//    }



}
