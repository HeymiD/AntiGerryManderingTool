package com.gerrymander.demo;

import java.io.FileNotFoundException;
import java.util.*;

import com.gerrymander.demo.algorithm.Algorithm;
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
	private Queue<Precinct> precinctsToSend = new LinkedList<Precinct>();

	@RequestMapping("/state")
	public String getSate(@RequestParam("state") String stateName,
                          @RequestParam("districtId") int districtId,
                          @RequestParam("electionType") String electionType) {
        try{
            return state.oldDistricts.get(districtId).getGeoData();
        }
        catch(NullPointerException e){
            state = new State(stateName);
            precinctsToSend.addAll(state.getPrecincts());
            algorithm = new Algorithm(state);
            return state.oldDistricts.get(districtId).getGeoData();
        }


	}

    @RequestMapping("/precincts")
    public String getPrecincts(@RequestParam("state") String stateName,
                               @RequestParam("districtId") int districtId,
                               @RequestParam("electionType") String electionType) {
        Set<Precinct> precinctBatchToSend = makePrecinctBatch(5000);
	    if(precinctBatchToSend.isEmpty()){
	        return "done";
        }
        else{
            return JSONMaker.makeJSONCollection(precinctBatchToSend);
        }
    }
    @RequestMapping("/phase0")
    public String sendResultPhaseZero(@RequestParam("votingThreshold") double votingThreshold,
                       @RequestParam("blockThreshold") double blockThreshold,
                       @RequestParam("districtId") String demographicGroups,
                       @RequestParam("electionType") String electionType){
        String demographicsCombination[] = demographicGroups.split(",");
        Set<DEMOGRAPHIC> demographicsSelectedByUser = new HashSet<DEMOGRAPHIC>();
        for (String demographic : demographicsCombination){
            demographicsSelectedByUser.add(DEMOGRAPHIC.valueOf(demographic));
        }
	    Result resultPhaseZero = algorithm.phaseZero(blockThreshold,votingThreshold,
                ELECTIONTYPE.valueOf(electionType),
                demographicsSelectedByUser);
        return JSONMaker.makeResult(resultPhaseZero);

    }

    public int calculatePrecinctBatchSize(Set<Precinct> batchPrecincts){
	    return JSONMaker.makeJSONCollection(batchPrecincts).length();
    }

    public Set<Precinct> makePrecinctBatch(int batchSize){
        Set<Precinct> precinctBatchToSend = new HashSet<Precinct>();
        while (batchSize>calculatePrecinctBatchSize(precinctBatchToSend) && !precinctsToSend.isEmpty()){
            precinctBatchToSend.add(precinctsToSend.remove());
        }
        return precinctBatchToSend;
    }
	
	
	
}
