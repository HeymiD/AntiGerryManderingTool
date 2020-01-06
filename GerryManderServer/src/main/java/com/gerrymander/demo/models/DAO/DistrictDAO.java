package com.gerrymander.demo.models.DAO;

import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.State;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
//@Repository
public class DistrictDAO {

//    @Autowired
//    private EntityManager entityManager;

    private static final String database_url="jdbc:mysql://mysql4.cs.stonybrook.edu:3306/hdannon?useSSL=false";
    private static final String database_username="hdannon";
    private static final String database_password="110941800";


//    @Override
//    public List<District> get() {
//
////        Session currentSession = entityManager.unwrap(Session.class);
////        Query<District> query = currentSession.createQuery("from District", District.class);
////        List<District> list_of_OldDistricts = query.getResultList();
//        return list_of_OldDistricts;
//    }

    public static District get(String id, State state) {

        District districtObj=null;
        try{
            Connection connection = DriverManager.getConnection(database_url,database_username,database_password);
//            connection pooling, driver manager, cursor,
            Statement statement = connection.createStatement();
            String query = "select * from timberwolves.Districts where id="+id;
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                districtObj = new District();
                districtObj.setState(state);
                districtObj.setID("U.S. Rep "+resultSet.getString("id"));
                districtObj.setGeoData(resultSet.getString("geojson"));
            }
        }
        catch (Exception e)
        {
            System.out.println("\n");
            System.out.println("Database connection Problem");
        }
////        Session currentSession = entityManager.unwrap(Session.class);
//        District districtObj = entityManager.find(District.class, id);
//        System.out.print(districtObj.getGeoData());
        return districtObj;
    }
}
