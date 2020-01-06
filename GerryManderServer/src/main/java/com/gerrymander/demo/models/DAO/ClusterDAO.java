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
//    jdbc:mysql://mysql4.cs.stonybrook.edu:3306/?user=hdannon
    private static final String database_url="jdbc:mysql://mysql4.cs.stonybrook.edu:3306/hdannon?useSSL=false";
    private static final String database_username="hdannon";
    private static final String database_password="110941800";

    public static void initNeighbors(State state) {
        try {
            String query = "SELECT * FROM timberwolves.fixedneighbors";
            Connection connection = DriverManager.getConnection(database_url, database_username, database_password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Cluster currCluster = state.findCluster(resultSet.getString("PCTKEY"));
                String neighborsString = (resultSet.getString("neighbors"));
                String[] neighbors = neighborsString.split("\\|");

                for (String neighbor : neighbors) {
                    currCluster.precinctsCluster.forEach(p->p.addNeighbor(neighbor));
                    Cluster neighborCluster = state.findCluster(neighbor);
                    Edge newEdge = new Edge(currCluster,neighborCluster);
                    boolean match = false;
                    for(Edge e:neighborCluster.edges){
                        if(e.equals(newEdge)){
                            currCluster.edges.add(e);
                            match=true;
                            break;
                        }
                    }
                    if(match==false){
                        currCluster.edges.add(newEdge);
                    }
                }
            }
        }
        catch (SQLException e){
            System.out.print(e);
        }
        System.out.println("Init clusters!");
        System.out.println("Cluster Size: "+state.clusters.size());
//        for(Cluster c:state.clusters){
//////            ((District)c).setDem_vote(c.getElections().get(state.userSelectedElection).getVotes().get(PARTYNAME.DEMOCRAT));
//////            ((District)c).setGop_vote(c.getElections().get(state.userSelectedElection).getVotes().get(PARTYNAME.REPUBLICAN));
//////            ((District)c).setExternalEdges(c.edges.size());
//////                    state.putDistrict(c);
////            System.out.println("Cluster ID: "+c.getID());
////            System.out.println("Cluster precincts size: "+c.precinctsCluster.size());
////            System.out.println("Edge count: "+c.edges.size());
////
//        }


    }
}
