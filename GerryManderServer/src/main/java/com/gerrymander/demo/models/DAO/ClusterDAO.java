package com.gerrymander.demo.models.DAO;

//import com.gerrymander.demo.models.concrete.;
import com.gerrymander.demo.*;
import com.gerrymander.demo.algorithm.Algorithm;
import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;
import com.gerrymander.demo.models.concrete.State;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.*;
import java.util.*;

public class ClusterDAO {

    private static final String database_url="jdbc:mysql://mysql4.cs.stonybrook.edu:3306/timberwolves?useSSL=false";
    private static final String database_username="timberwolves";
    private static final String database_password="changeit";

    public static void initNeighbors(State state) {
        try {
            String query = "SELECT * FROM timberwolves.fixedneighbors";
            Connection connection = DriverManager.getConnection(database_url, database_username, database_password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Precinct precinct = state.getPrecinct(resultSet.getString("PCTKEY"));
                Cluster cluster = new Cluster(precinct.getID());
                ((District)cluster).population = precinct.getPopulation();
                cluster.precinctsCluster.add(precinct);
                state.clusters.add(cluster);
                String neighborsString = (resultSet.getString("neighbors"));
                String[] neighbors = neighborsString.split("\\|");
                for (String neighbor : neighbors) {
//                    System.out.println("neighbor id: "+neighbor);
                    Precinct neighborPrecinct = state.getPrecinct(neighbor);
                    Cluster neighborCluster = new Cluster(neighborPrecinct.getID());
                    ((District)cluster).population = neighborCluster.getPopulation();
                    neighborCluster.precinctsCluster.add(neighborPrecinct);
                    state.clusters.add(neighborCluster);
                    Edge edge = new Edge(cluster, neighborCluster);
                    cluster.edges.add(edge);
                    neighborCluster.edges.add(edge);
                }
            }
        }
        catch (SQLException e){
            System.out.print(e);
        }
        System.out.println("Init clusters!");
        System.out.println("Cluster Size: "+state.clusters.size());
        for(Cluster c:state.clusters){
            ((District)c).setDem_vote(c.getElections().get(state.userSelectedElection).getVotes().get(PARTYNAME.DEMOCRAT));
            ((District)c).setGop_vote(c.getElections().get(state.userSelectedElection).getVotes().get(PARTYNAME.REPUBLICAN));
            ((District)c).setExternalEdges(c.edges.size());
            state.putDistrict(c);
//            System.out.println("Cluster ID: "+c.ID);
//            System.out.println("Cluster precincts size: "+c.precincts.size());
//            System.out.println("Edge count: "+c.edges.size());
        }


    }
}
