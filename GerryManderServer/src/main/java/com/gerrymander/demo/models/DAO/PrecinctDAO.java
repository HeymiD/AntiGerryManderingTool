package com.gerrymander.demo.models.DAO;

//import com.gerrymander.demo.models.concrete.;
import com.gerrymander.demo.DEMOGRAPHIC;
import com.gerrymander.demo.ELECTIONTYPE;
import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrecinctDAO {


//    @Autowired
//    private EntityManager entityManager;

    private static final String database_url="jdbc:mysql://mysql4.cs.stonybrook.edu:3306/timberwolves?useSSL=false";
    private static final String database_username="timberwolves";
    private static final String database_password="changeit";


//    @Override
//    public List<District> get() {
//
////        Session currentSession = entityManager.unwrap(Session.class);
////        Query<District> query = currentSession.createQuery("from District", District.class);
////        List<District> list_of_OldDistricts = query.getResultList();
//        return list_of_OldDistricts;
//    }

    public static Set<Precinct> initPrecinctsforDistrict(String districtID, ELECTIONTYPE election,
                                                         District dis) {

        String query = "";
        switch(election){
            case PRESIDENTIAL2016:
                query = "select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.District, P.geojson " +
                        "from timberwolves.Precincts_GEO P, timberwolves.presidential_2016 C, timberwolves.texas_demographics T " +
                        "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY AND C.District=\'"+districtID+"\'";
                break;
            case CONGRESSIONAL2016:
                query="select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.Office, P.geojson " +
                        "from timberwolves.Precincts_GEO P, timberwolves.congressional_2016 C, timberwolves.texas_demographics T " +
                        "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY AND C.Office=\'"+districtID+"\'";
                break;
            case CONGRESSIONAL2018:
                query="select T.*, C.Democrat, C.Republican, C.Green, C.Libertarian, C.Office, P.geojson " +
                        "from timberwolves.Precincts_GEO P, timberwolves.congressional_2018 C, timberwolves.texas_demographics T " +
                        "where P.PCTKEY=C.PCTKEY AND P.PCTKEY=T.PCTKEY AND C.Office=\'"+districtID+"\'";
                break;
        }


        try{
            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                Precinct precinctobj=null;
                /*
                HashMap<DEMOGRAPHIC,Integer> demographics = new HashMap<DEMOGRAPHIC, Integer>();
                demographics.put(DEMOGRAPHIC.AFROAM, Integer.parseInt(resultSet.getString("Black")));
                demographics.put(DEMOGRAPHIC.WHITE, Integer.parseInt(resultSet.getString("White")));
                demographics.put(DEMOGRAPHIC.ASIAN, Integer.parseInt(resultSet.getString("Asian")));
                demographics.put(DEMOGRAPHIC.HISPANIC, Integer.parseInt(resultSet.getString("Hispanic")));
                demographics.put(DEMOGRAPHIC.NATIVE, Integer.parseInt(resultSet.getString("Native")));
                demographics.put(DEMOGRAPHIC.OTHER, Integer.parseInt(resultSet.getString("Other")));

                int totPop = 0;
                for (DEMOGRAPHIC key : demographics.keySet()){
                    totPop+=demographics.get(key);
                }
//              */
                if (query.contains("Office")){
                    precinctobj = new Precinct(resultSet.getString("PCTKEY"),
                            resultSet.getString("geojson"),
                            resultSet.getString("Office"));
                    dis.putPrecinct(resultSet.getString("PCTKEY"),precinctobj);
                }
                else{
                    System.out.println("Key: "+resultSet.getString("PCTKEY"));
                    System.out.println("District: "+resultSet.getString("District"));
                    precinctobj = new Precinct(resultSet.getString("PCTKEY"),
                            resultSet.getString("geojson"),
                            resultSet.getString("District"));
                    dis.putPrecinct(resultSet.getString("PCTKEY"),precinctobj);
                }
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

}
