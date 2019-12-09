package com.gerrymander.demo;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

import com.gerrymander.demo.algorithm.Algorithm;
import com.gerrymander.demo.models.DAO.ClusterDAO;
import com.gerrymander.demo.models.DAO.DistrictDAO;
import com.gerrymander.demo.models.DAO.PrecinctDAO;
import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;
import com.gerrymander.demo.models.concrete.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;

@RestController
public class GerryManderController {

    private State state;
	private Algorithm algorithm;
	public Map<String,Stack<Precinct>> precinctQueue = new HashMap<String, Stack<Precinct>>();
//	public Map<String,Integer> queueStatus = new HashMap<String, Integer>();
//	public Stack<Precinct> precinctsToSend = new Stack<Precinct>();
	public Stack<Precinct> precinctsToSend = new Stack<Precinct>();
	public static int queueStatus=0;
//    @RequestParam("districtId") int districtId
	@RequestMapping("/state")
	public String getSate(@RequestParam("state") String stateName) {
	    if(state==null){
            state = new State(stateName);
            PrecinctDAO.initAllPrecincts(state);
            ClusterDAO.initNeighbors(state);
//            PrecinctDAO.getAllPrecinctGeoJSON(state);
            for(Precinct p:state.getPrecincts()){
                precinctsToSend.push(p);
                state.population+=p.getPopulation();
            }

//            for(int i=1;i<37;i++){
//                precinctQueue.put("U.S. Rep "+i,new Stack<Precinct>());
//            }
//            for(int i=1;i<37;i++){
//                queueStatus.put("U.S. Rep "+i,-1);
//            }
//            for(Precinct p: state.getPrecincts()){
//                p.setGeometryJSON(PrecinctDAO.getPrecinctGeoJSONById(p.getID()));
//            }
//            System.out.println("BEGIN fetching precincts");
//            for(int i=1;i<37;i++){
//                PrecinctDAO.getPrecinctGeoJSONByDistrict("U.S. Rep "+i,state);
//            }

//            System.out.println("END fetching precincts");
//            for(Precinct p :state.getPrecincts()){
//                precinctsToSend.push(p);
//            }
            algorithm = new Algorithm(state);
        }
//        try{
//            return state.oldDistricts.get(districtId).getGeoData();
//        }
//        catch(NullPointerException e){
//
//            District d = DistrictDAO.get(districtId+"",state);
//            PrecinctDAO.initPrecinctsforDistrict(ELECTIONTYPE.Presidential2016,d);
//            state.oldDistricts.put(d.getID(),d);
////            precinctsToSend.addAll(state.getPrecincts());
////            return state.oldDistricts.get("U.S. Rep "+districtId).getGeoData();
//            return d.getGeoData();
//        }
        return JSONMaker.makeJSONArray(state.getPrecincts());


	}
//@RequestParam("districtId") String districtId
    @RequestMapping("/precincts")
    public String getPrecincts(@RequestParam("state") String stateName,
                               @RequestParam("districtId") String districtId) {
        if(queueStatus==1){
            return "Done";
        }
//        System.out.println("Sending precincts...");
//        Set<Precinct> precinctBatchToSend = makePrecinctBatch(200);
//        return JSONMaker.makeJSONCollection(precinctBatchToSend);
//        Set<Precinct> precinctBatchToSend = makePrecinctBatch(5000000);
//	    if(precinctBatchToSend.isEmpty()){
//	        System.out.println("No more precincts");
//	        return "done";
//        }
//        else{
//	        System.out.println(precinctBatchToSend.size()+"Precincts are sent");
//            return JSONMaker.makeJSONCollection(precinctBatchToSend);
//        }

//        String districtID = districtId;
//        if(districtId.contains("Begin")){
//            districtID = districtId.substring(6,districtId.length());
//            Set<Precinct> precinctsDistrict = PrecinctDAO.
//                    getPrecinctGeoJSONByDistrict("U.S. Rep "+districtID,state);
//            for (Precinct p: precinctsDistrict){
//                precinctQueue.get("U.S. Rep "+districtID).push(p);
//            }
//            queueStatus.put("U.S. Rep "+districtID,0);
//        }
//        Set<Precinct> precinctBatchToSend = makePrecinctBatch(100,precinctQueue.get("U.S. Rep "+districtID),
//                "U.S. Rep "+districtID);
//        if (queueStatus.get("U.S. Rep "+districtID)==1){return "District Done";}
//        else{return JSONMaker.makeJSONCollection(precinctBatchToSend);}
//
        return JSONMaker.makeJSONCollection(
                PrecinctDAO.getPrecinctGeoJSONByDistrict("U.S. Rep "+districtId,state));

//        System.out.println("PCTKEY: "+precinctId);
////        state.getPrecinct(precinctId).setGeometryJSON(PrecinctDAO.getPrecinctGeoJSONById(precinctId));
//        return state.getPrecinct(precinctId).toString();

    }
    @RequestMapping("/precinctsInit")
    public String getPrecinctsInit(@RequestParam("state") String stateName) {
	    return "ready";
    }
    @RequestMapping("/districtData")
    public String sendDistrictData(@RequestParam("districtId")String districtId,
                                   @RequestParam("elecType") String elecType){
        int districtPopulation = 0;
        Map<PARTYNAME,Integer> votesDistrict = new HashMap<PARTYNAME,Integer>();
        Map<DEMOGRAPHIC,Integer> demographicsDistrict = new HashMap<DEMOGRAPHIC,Integer>();
        System.out.println("District ID: "+districtId);
        for (Precinct p:state.getPrecincts()){
            if(p.getOriginalDistrictID().equals(districtId)){
                districtPopulation+=p.getPopulation();
                Votes v = p.getElections().get(ELECTIONTYPE.valueOf(elecType));
                for (PARTYNAME party:PARTYNAME.values()){
                    try{votesDistrict.put(party,votesDistrict.get(party)+v.getVotes().get(party));}
                    catch(NullPointerException e){votesDistrict.put(party,v.getVotes().get(party));}
                }
                for (DEMOGRAPHIC demographic:p.getPrecinctDemographics().keySet()){
                    try{demographicsDistrict.put(demographic,demographicsDistrict.get(demographic)+p.getPrecinctDemographics().get(demographic));}
                    catch(NullPointerException e){demographicsDistrict.put(demographic,p.getPrecinctDemographics().get(demographic));}
                }
            }
        }
	    return "{" + "\"DistrictID\": "+"\""+districtId+"\"" + ", "
                + "\"White\": " + "\""+(demographicsDistrict.get(DEMOGRAPHIC.WHITE)).toString()+"\"" + ", "
                + "\"Black\": " + "\""+(demographicsDistrict.get(DEMOGRAPHIC.AFROAM)).toString()+ "\""+", "
                + "\"Hispanic\": " + "\""+(demographicsDistrict.get(DEMOGRAPHIC.HISPANIC)).toString()+ "\""+", "
                + "\"Native\": " + "\""+(demographicsDistrict.get(DEMOGRAPHIC.NATIVE)).toString()+ "\""+", "
                + "\"Pacific\": " + "\""+(demographicsDistrict.get(DEMOGRAPHIC.PACISLAND)).toString()+ "\""+", "
                + "\"Asian\": " + "\""+(demographicsDistrict.get(DEMOGRAPHIC.ASIAN)).toString()+ "\""+", "
                + "\"Other\": " + "\""+(demographicsDistrict.get(DEMOGRAPHIC.OTHER)).toString()+ "\""+", "
                + "\"Republican\": " + "\""+votesDistrict.get(PARTYNAME.REPUBLICAN).toString()+"\""+ ", "
                + "\"Democrat\": " + "\""+votesDistrict.get(PARTYNAME.DEMOCRAT).toString()+"\""+ ", "
                + "\"Green\": "+"\""+votesDistrict.get(PARTYNAME.GREEN).toString()+"\""+", "
                + "\"Libertarian\": "+"\""+votesDistrict.get(PARTYNAME.LIBERTARIAN)+"\""+ ", "
                +"\"Population\": "+"\""+districtPopulation+"\""
                +"}";
    }

    @RequestMapping("/phase0")
    public String sendResultPhaseZero(@RequestParam("votingThreshold") double votingThreshold,
                       @RequestParam("blockThreshold") double blockThreshold,
                       @RequestParam("electionType") String electionType){
	    state.userDemographicThreshold = blockThreshold;
	    state.userVoteThreshold = votingThreshold;
	    state.userSelectedElection = ELECTIONTYPE.valueOf(electionType);
        state.majMinPrecinctStats = new Result(state.getName(),state.getPrecincts().size());
	    Result resultPhaseZero = algorithm.phaseZero(blockThreshold,votingThreshold,
                ELECTIONTYPE.valueOf(electionType));
        return resultPhaseZero.toString();

    }

//    public int calculatePrecinctBatchSize(Set<Precinct> batchPrecincts){
//	    return JSONMaker.makeJSONCollection(batchPrecincts).length();
//    }

    public Set<Precinct> makePrecinctBatch(int batchSize){
        Set<Precinct> precinctBatchToSend = new HashSet<Precinct>();
        while (batchSize>(precinctBatchToSend.size()) && !precinctsToSend.isEmpty()){
            Precinct p = precinctsToSend.pop();
            p.setGeometryJSON(PrecinctDAO.getPrecinctGeoJSONById(p.getID()));
            precinctBatchToSend.add(p);
        }
        if(precinctsToSend.isEmpty()){queueStatus=1;}
        return precinctBatchToSend;
    }



}
