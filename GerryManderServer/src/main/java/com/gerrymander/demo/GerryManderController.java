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
	public static Stack<Precinct> precinctsToSend = new Stack<>();
//    @RequestParam("districtId") int districtId
	@RequestMapping("/state")
	public String getSate(@RequestParam("state") String stateName) {
	    if(state==null){
            state = new State(stateName);
            PrecinctDAO.initAllPrecincts(state);
            ClusterDAO.initNeighbors(state);
//            System.out.println("BEGIN fetching precincts");
//            for(int i=1;i<37;i++){
//                PrecinctDAO.getPrecinctGeoJSONByDistrict("U.S. Rep "+i,state);
//            }
////            PrecinctDAO.getAllPrecinctGeoJSON(state);
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
//
    @RequestMapping("/precincts")
    public String getPrecincts(@RequestParam("state") String stateName,
                               @RequestParam("districtId") String districtId) {
//        Set<Precinct> precinctBatchToSend = makePrecinctBatch(5000000);
//	    if(precinctBatchToSend.isEmpty()){
//	        System.out.println("No more precincts");
//	        return "done";
//        }
//        else{
//	        System.out.println(precinctBatchToSend.size()+"Precincts are sent");
//            return JSONMaker.makeJSONCollection(precinctBatchToSend);
//        }
//
        return JSONMaker.makeJSONCollection(
                PrecinctDAO.getPrecinctGeoJSONByDistrict("U.S. Rep "+districtId,state));


//        System.out.println("PCTKEY: "+precinctId);
////        state.getPrecinct(precinctId).setGeometryJSON(PrecinctDAO.getPrecinctGeoJSONById(precinctId));
//        return state.getPrecinct(precinctId).toString();

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

    public int calculatePrecinctBatchSize(Set<Precinct> batchPrecincts){
	    return JSONMaker.makeJSONCollection(batchPrecincts).length();
    }

    public Set<Precinct> makePrecinctBatch(int batchSize){
        Set<Precinct> precinctBatchToSend = new HashSet<Precinct>();
        while (batchSize>calculatePrecinctBatchSize(precinctBatchToSend) && !precinctsToSend.isEmpty()){
            precinctBatchToSend.add(precinctsToSend.pop());
        }
        return precinctBatchToSend;
    }



}
