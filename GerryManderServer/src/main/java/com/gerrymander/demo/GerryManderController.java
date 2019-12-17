package com.gerrymander.demo;

import java.util.*;

import com.gerrymander.demo.algorithm.Algorithm;
import com.gerrymander.demo.algorithm.Measure;
import com.gerrymander.demo.algorithm.SortByPopulation;
import com.gerrymander.demo.models.DAO.ClusterDAO;
import com.gerrymander.demo.models.DAO.PrecinctDAO;
import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;
import com.gerrymander.demo.models.concrete.State;
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
	public static Stack<District> districtQueue = new Stack<District>();
//    @RequestParam("districtId") int districtId
	@RequestMapping("/state")
	public String getSate(@RequestParam("state") String stateName) {
	    if(state==null){
            System.out.println("Getting State");
            state = new State(stateName);
            PrecinctDAO.initAllPrecincts(state);
            for(Precinct p:state.getPrecincts()){
                precinctsToSend.push(p);
                state.population+=p.getPopulation();
            }
            System.out.println("Init pop: "+state.population);

            algorithm = new Algorithm(state);
            algorithm.initClusters();
            System.out.println("Clusters Initialized.");
            ClusterDAO.initNeighbors(state);
        }
        return JSONMaker.makeJSONDict(state.getPrecincts());


	}
    @RequestMapping("/stateData")
    public String getStateData(@RequestParam("electionType") String election) {
	    ELECTIONTYPE elecType = ELECTIONTYPE.valueOf(election);
	    if(elecType==ELECTIONTYPE.Presidential2016){
	        return "Not Applicable";
        }
        Votes statevotes = new Votes();
        Map<PARTYNAME,Integer> stateElec = new HashMap<PARTYNAME,Integer>();
        Map<String,PARTYNAME> winningParties = new HashMap<String, PARTYNAME>();
	    for(Cluster district:state.oldDistricts.values()){
            Map<PARTYNAME,Integer> disElection = new HashMap<PARTYNAME,Integer>();
	        for(Precinct p:district.getPrecincts()){
                for(PARTYNAME party:PARTYNAME.values()){
                    try{
                        stateElec.put(party,stateElec.get(party)+p.getElections().get(elecType).getVotes().get(party));
                        disElection.put(party,disElection.get(party)+p.getElections().get(elecType).getVotes().get(party));
                    }
                    catch (NullPointerException ex){
                        stateElec.put(party,p.getElections().get(elecType).getVotes().get(party));
                        disElection.put(party,p.getElections().get(elecType).getVotes().get(party));
                    }
                }
                Votes votes = new Votes();
                votes.setVotes(disElection);
                district.elections.put(elecType,votes);
                winningParties.put(district.getID(),district.elections.get(elecType).getWinningParty());
            }
        }
        statevotes.setVotes(stateElec);
        Map<PARTYNAME,Double> partyRatios = new HashMap<PARTYNAME,Double>();
        for(PARTYNAME p:PARTYNAME.values()){
            partyRatios.put(p,statevotes.calculateWinningPartyRatio(p)*100.0);
        }
        Map<PARTYNAME,Double> districtVotingPattern = new HashMap<PARTYNAME,Double>();
	    for(PARTYNAME party:PARTYNAME.values()){
	        districtVotingPattern.put(party,0.0);
            for(String disId:winningParties.keySet()){
                if(winningParties.get(disId)==party){
                    districtVotingPattern.put(party,((districtVotingPattern.get(party)+1)/36)*100.0);
                }
            }
        }
        return "{"+ "\"RepublicanRatio\": " + "\""+partyRatios.get(PARTYNAME.REPUBLICAN).toString()+"\""+ ", "
                + "\"DemocratRatio\": " + "\""+partyRatios.get(PARTYNAME.DEMOCRAT).toString()+"\""+ ", "
                + "\"GreenRatio\": "+"\""+partyRatios.get(PARTYNAME.GREEN).toString()+"\""+", "
                + "\"LibertarianRatio\": "+"\""+partyRatios.get(PARTYNAME.LIBERTARIAN)+"\""+ ", "
                + "\"Republican\": " + "\""+districtVotingPattern.get(PARTYNAME.REPUBLICAN).toString()+"\""+ ", "
                + "\"Democrat\": " + "\""+districtVotingPattern.get(PARTYNAME.DEMOCRAT).toString()+"\""+ ", "
                + "\"Green\": "+"\""+districtVotingPattern.get(PARTYNAME.GREEN).toString()+"\""+", "
                + "\"Libertarian\": "+"\""+districtVotingPattern.get(PARTYNAME.LIBERTARIAN).toString()+"\""
                +"}";
    }
//@RequestParam("districtId") String districtId
    @RequestMapping("/precincts")
    public String getPrecincts(@RequestParam("electionType") String elecType) {
	    ELECTIONTYPE e = ELECTIONTYPE.valueOf(elecType);
        return JSONMaker.makeJSONDict(state.getPrecincts(),e);
	}


    @RequestMapping("/districtData")
    public String sendDistrictData(@RequestParam("districtId")String districtId,
                                   @RequestParam("elecType") String elecType){
        int districtPopulation = 0;
        Map<PARTYNAME,Integer> votesDistrict = new HashMap<PARTYNAME,Integer>();
        Map<DEMOGRAPHIC,Integer> demographicsDistrict = new HashMap<DEMOGRAPHIC,Integer>();
        String[] editDisId = districtId.split(" ");
        districtId = "U.S. Rep "+editDisId[editDisId.length-1];
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
                + "\"Libertarian\": "+"\""+votesDistrict.get(PARTYNAME.LIBERTARIAN).toString()+"\""+ ", "
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

    @RequestMapping("/phase1")
    public String sendResultPhaseOne(@RequestParam("votingThreshold") double votingThreshold,
                                      @RequestParam("blockThreshold") double blockThreshold,
                                      @RequestParam("electionType") String electionType,
                                     @RequestParam("targetNumDistricts") int targetNumDistricts,
                                     @RequestParam("update") boolean update,
                                     @RequestParam("demString") String[] demString,
                                     @RequestParam("begin") boolean begin,
                                     @RequestParam("fairnessMin") double fairnessMin,
                                     @RequestParam("reokCompactMin") double reokCompactMin,
                                     @RequestParam("convexCompactMin") double convexCompactMin,
                                     @RequestParam("edgeCompactMin") double edgeCompactMin,
                                     @RequestParam("popEqualMin") double popEqualMin,
                                     @RequestParam("popHomoMin") double popHomoMin,
                                     @RequestParam("efficiencyMin") double efficiencyMin,
                                     @RequestParam("competitiveMin") double competitiveMin,
                                     @RequestParam("republicGerryMin") double republicGerryMin,
                                     @RequestParam("democratGerryMin") double democratGerryMin,
                                     @RequestParam("countyCompactMin") double countyCompactMin){

        System.out.println("BT: "+blockThreshold);
        System.out.println("VT: "+votingThreshold);
        state.userDemographicThreshold = blockThreshold;
        state.userVoteThreshold = votingThreshold;
        state.userSelectedElection = ELECTIONTYPE.valueOf(electionType);
        algorithm.targetNumDist = targetNumDistricts;
        algorithm.popThreshMax=0.6;
        algorithm.popThreshMin=0.5;
        algorithm.demString = demString;
        state.demString=demString;
        algorithm.weights= new HashMap<Measure,Double>();
        algorithm.weights.put(Measure.COMPETITIVENESS,competitiveMin);
        algorithm.weights.put(Measure.EFFICIENCY_GAP,efficiencyMin);
        algorithm.weights.put(Measure.GERRYMANDER_DEMOCRAT,democratGerryMin);
        algorithm.weights.put(Measure.CONVEX_HULL_COMPACTNESS,convexCompactMin);
        algorithm.weights.put(Measure.REOCK_COMPACTNESS,reokCompactMin);
        algorithm.weights.put(Measure.PARTISAN_FAIRNESS,fairnessMin);
        algorithm.weights.put(Measure.POPULATION_EQUALITY,popEqualMin);
        algorithm.weights.put(Measure.POPULATION_HOMOGENEITY,popHomoMin);
        algorithm.weights.put(Measure.GERRYMANDER_REPUBLICAN,republicGerryMin);
        algorithm.weights.put(Measure.EDGE_COMPACTNESS,edgeCompactMin);
        algorithm.factors = new HashMap<FACTOR,Double>();
        algorithm.factors.put(FACTOR.EQPOP,popEqualMin);
        algorithm.factors.put(FACTOR.MAJMIN,2.0);
        algorithm.factors.put(FACTOR.COMPACTNESS,edgeCompactMin);
        algorithm.factors.put(FACTOR.POLFAIR,fairnessMin);
        algorithm.factors.put(FACTOR.COUNTY,countyCompactMin);

        if(begin==true){
            System.out.println("Initializing GeoJSON");
            PrecinctDAO.getAllPrecinctGeoJSON(state);
            System.out.println("GeoJSON Done");
            Collections.sort(state.clusters,new SortByPopulation());
        }
        if(state.clusters.size() <= targetNumDistricts){
            state.majMinClusters.addAll(state.clusters);
            return "done";
        }
//        if(state.majMinClusters.isEmpty()){
//            for(Cluster c:state.clusters){
//                if(c.checkMajorityMinority(blockThreshold,votingThreshold,ELECTIONTYPE.valueOf(electionType))){
//                    state.majMinClusters.add(c);
//                }
//            }
//            System.out.println("MajMin Clusters Size: "+state.majMinClusters.size());
//        }
        if(algorithm.phaseOne(update)){
            return JSONMaker.phase1Data(state);
        }
        return JSONMaker.phase1Data(state.clusters);

    }

    @RequestMapping("/phase2")
    public String sendResultPhaseTwo(@RequestParam("votingThreshold") double votingThreshold,
                                     @RequestParam("blockThreshold") double blockThreshold,
                                     @RequestParam("electionType") String electionType,
                                     @RequestParam("targetNumDistricts") int targetNumDistricts,
                                     @RequestParam("demString") String[] demString,
                                     @RequestParam("begin") boolean begin,
                                     @RequestParam("fairnessMin") double fairnessMin,
                                     @RequestParam("reokCompactMin") double reokCompactMin,
                                     @RequestParam("convexCompactMin") double convexCompactMin,
                                     @RequestParam("edgeCompactMin") double edgeCompactMin,
                                     @RequestParam("popEqualMin") double popEqualMin,
                                     @RequestParam("popHomoMin") double popHomoMin,
                                     @RequestParam("efficiencyMin") double efficiencyMin,
                                     @RequestParam("competitiveMin") double competitiveMin,
                                     @RequestParam("republicGerryMin") double republicGerryMin,
                                     @RequestParam("democratGerryMin") double democratGerryMin,
                                     @RequestParam("countyCompactMin") double countyCompactMin){

        state.userDemographicThreshold = blockThreshold;
        state.userVoteThreshold = votingThreshold;
        state.userSelectedElection = ELECTIONTYPE.valueOf(electionType);
        algorithm.targetNumDist = targetNumDistricts;
        algorithm.demString = demString;
        state.demString=demString;
        algorithm.weights= new HashMap<Measure,Double>();
        algorithm.weights.put(Measure.COMPETITIVENESS,competitiveMin);
        algorithm.weights.put(Measure.EFFICIENCY_GAP,efficiencyMin);
        algorithm.weights.put(Measure.GERRYMANDER_DEMOCRAT,democratGerryMin);
        algorithm.weights.put(Measure.CONVEX_HULL_COMPACTNESS,convexCompactMin);
        algorithm.weights.put(Measure.REOCK_COMPACTNESS,reokCompactMin);
        algorithm.weights.put(Measure.PARTISAN_FAIRNESS,fairnessMin);
        algorithm.weights.put(Measure.POPULATION_EQUALITY,popEqualMin);
        algorithm.weights.put(Measure.POPULATION_HOMOGENEITY,popHomoMin);
        algorithm.weights.put(Measure.GERRYMANDER_REPUBLICAN,republicGerryMin);
        algorithm.weights.put(Measure.EDGE_COMPACTNESS,edgeCompactMin);
        algorithm.factors = new HashMap<FACTOR,Double>();
        algorithm.factors.put(FACTOR.EQPOP,popEqualMin);
        algorithm.factors.put(FACTOR.MAJMIN,2.0);
        algorithm.factors.put(FACTOR.COMPACTNESS,edgeCompactMin);
        algorithm.factors.put(FACTOR.POLFAIR,fairnessMin);
        algorithm.factors.put(FACTOR.COUNTY,countyCompactMin);
        if(begin){
            algorithm.phase2Init();
            System.out.println("Phase 2 Initialized");
        }
        if(districtQueue.isEmpty()){
            return "done";
        }
        Move move = algorithm.getMoveFromDistrict(districtQueue.pop());
        System.out.println("Move Done");
        if(move!=null){
            String clusterId = move.getTo().getID();
            String disId = String.valueOf(state.clusters.indexOf(state.findCluster(clusterId)));
            System.out.println("{"+"\""+move.getPrecinct().getID()+"\": "+"\""+disId+"\"}");
            return "{"+"\""+move.getPrecinct().getID()+"\": "+"\""+disId+"\"}";
        }


        return "";

    }

    @RequestMapping("/phase2Scores")
    public String sendPhase2Scores(){
//        for(Cluster old : state.oldDistricts.values()){
//            for(Precinct p : old.getPrecincts()){
//                old.setGop_vote(old.getGOPVote() + p.getGOPVote());
//                old.setDem_vote(old.getDEMVote() + p.getDEMVote());
//            }
//            System.out.println(old.getGOPVote());
//            System.out.println(old.getDEMVote());
//        }

	    double oldGMScore = 0;
	    for(String districtId: state.oldDistricts.keySet()){
            oldGMScore+=FACTOR.EFFICIENCY_GAP.calculateMeasure(state.oldDistricts.get(districtId));
            oldGMScore+=Measure.COMPETITIVENESS.calculateMeasure(state.oldDistricts.get(districtId));

        }
        double newGMScore = 0;
        for(District dis: state.getDistricts()){
            newGMScore+=Measure.EFFICIENCY_GAP.calculateMeasure(dis);
            newGMScore+=Measure.COMPETITIVENESS.calculateMeasure(dis);
        }
        return "{\"OldGmScore\": "+"\""+oldGMScore+"\","
                +"\"NewGmScore\": "+"\""+newGMScore+"\"}";

    }

    @RequestMapping("/FinalResult")
    public String sendPhase1Results(){

        ArrayList<String> results = new ArrayList<String>();
        int numMajMin = 0;
        for(Cluster c:state.majMinClusters){
            boolean majmin = false;
            if(c.checkMajorityMinority(state.userDemographicThreshold,
                    state.userVoteThreshold, state.userSelectedElection, algorithm.demString)){
                    majmin=true;
                    numMajMin++;
                    String result = "";
                    result+="DistrictID: "+state.clusters.indexOf(c)
                            +"\nLargest Demographic: "+c.getLargestDemographic().toString()
                            +"\nPopulation: "+c.getClusterDemographics().get(c.getLargestDemographic())
                            +"\nMajMin: "+majmin;
                    results.add(result);
            }
//                String result = "";
//
//                result+="DistrictID: "+state.clusters.indexOf(c)
//                        +"\nLargest Demographic: "+c.getLargestDemographic().toString()
//                        +"\nPopulation: "+c.getClusterDemographics().get(c.getLargestDemographic())
//                        +"\nMajMin: "+majmin;
//                results.add(result);




        }

        String resultToSend = "# of Majoirty Minority Districts: "+numMajMin+"\n";
        for(String s:results){
            resultToSend+=s+"\n";
        }
        return resultToSend;

    }

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
