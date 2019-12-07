package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.Precinct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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




}
