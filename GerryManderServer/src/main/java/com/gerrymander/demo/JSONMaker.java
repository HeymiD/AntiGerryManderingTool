package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.Precinct;
import com.gerrymander.demo.models.concrete.State;

import java.util.*;


public class JSONMaker {

    public static String makeJSONCollection(Set<Precinct> precincts){

        String json = "{\"type\":\"FeatureCollection\",\"features\":[";

        int num_precincts = precincts.size();
        int i = 0;
        for(Precinct p : precincts){
            String p_geojson = p.toString();
//            String geographies = p_geojson.substring(p_geojson.indexOf("\"geometry\":"),p_geojson.length()-2);
//            String properties = "{\"type\":\"Feature\",\"properties\":{\"PCTKEY\":\""+p.getID()+"\",\"\":63.50},";
            if(i<num_precincts-1){
                json+=p_geojson+",";
            }
            else{
                json+=p_geojson;
            }
            i++;
        }
        json+="]}";
        return json;

    }
    public static String makeResult(Result result){
        return result.toString();
    }

    public static String makeJSONArray(Set<Precinct> precincts){
        String jsonArray = "{"+"\"PrecinctKeys\": "+"[";
        int i=0;
        for(Precinct p : precincts){
            jsonArray+="\""+p.getID()+"\"";
            if(i<precincts.size()-1){
                jsonArray+=", ";
            }
            else{
                jsonArray+=" ]"+"}";
            }
            i++;

        }
        return jsonArray;
    }

    public static String phase1Data(State state){
        String data = "{";
//        int clusterCounter = 0;
        int precinctCounter = 0;
        for(Cluster cluster:state.clusters){
            System.out.println("Num Edges: "+cluster.externalEdges);
        }
        for(Cluster cluster:state.clusters){
//            int precinctCounter = 0;
//            String disId = cluster.getState().getPrecinct(cluster.getID()).getOriginalDistrictID();
            String disId = String.valueOf(state.clusters.indexOf(cluster));
            for(Precinct p:cluster.precinctsCluster){
//                if(precinctCounter<cluster.precinctsCluster.size()-1 && clusterCounter<clusters.size()-1){
                if(precinctCounter < 8935){
                    data+="\""+p.getID()+"\": "+ "\"" +disId+"\""+", ";
                }
                else{
                    data+="\""+p.getID()+"\": "+ "\"" +disId+"\"";
                }
                precinctCounter++;
            }
//            clusterCounter++;
        }
        data+="}";
        return data;
    }
    public static String phase1Data(List<Cluster> clusters){
        Set<Integer> disIDs = new HashSet<Integer>();
        String data = "{";
//        int clusterCounter = 0;
        int precinctCounter = 0;
        for(Cluster cluster:clusters){
//            int precinctCounter = 0;
            String disId = cluster.getState().getPrecinct(cluster.getID()).getOriginalDistrictID();
            for(Precinct p:cluster.precinctsCluster){
//                if(precinctCounter<cluster.precinctsCluster.size()-1 && clusterCounter<clusters.size()-1){
                if(precinctCounter < 8935){
                    data+="\""+p.getID()+"\": "+ "\"U.S. Rep " +disId+"\""+", ";
                }
                else{
                    data+="\""+p.getID()+"\": "+ "\"U.S. Rep " +disId+"\"";
                }
                precinctCounter++;
            }
//            clusterCounter++;
        }
        data+="}";
        disIDs.forEach(s-> System.out.println("DisIDs: "+s));
        return data;
    }

    public static String makeJSONDict(Set<Precinct> precincts){
        String jsonDict = "{";
        int i=0;
        for(Precinct p:precincts){
            if(i<precincts.size()-1){
                jsonDict+=p.toString()+", ";
            }
            else{
                jsonDict+=p.toString();
            }
            i++;
        }
        jsonDict+="}";
        return jsonDict;
    }

    public static String makeJSONDict(Set<Precinct> precincts, ELECTIONTYPE electiontype){
        String jsonDict = "{";
        int i=0;
        for(Precinct p:precincts){
            if(i<precincts.size()-1){
                jsonDict+=p.electData(electiontype)+", ";
            }
            else{
                jsonDict+=p.electData(electiontype);
            }
            i++;
        }
        jsonDict+="}";
        return jsonDict;
    }

}
