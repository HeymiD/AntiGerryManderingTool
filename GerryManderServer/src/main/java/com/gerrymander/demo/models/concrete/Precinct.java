package com.gerrymander.demo.models.concrete;

import com.gerrymander.demo.*;
import com.gerrymander.demo.measures.PrecinctInterface;
import com.vividsolutions.jts.geom.Geometry;


import javax.persistence.*;
import java.util.*;
//@Entity
//@Table(name="Precints_GEO")
public class Precinct implements PrecinctInterface {
//    @Id
//    @Column(name="PCTKEY")
    private final String ID;
    //    @Transient
    private Geometry geometry;
//    @Transient
    private String geometryJSON;
//    @Transient
    private String originalDistrictID;
    public String newDistrictID;
//    @Transient
    private int population;
//    @Transient
    public int gop_vote;
//    @Transient
    public int dem_vote;
//    @Transient
    private boolean isMajorityMinority;
//    @Transient
    private Map<DEMOGRAPHIC, Integer> precinctDemographics;
//    @Transient
    private Set<DemographicGroup> demographicGroups;
//    @Transient
    private Set<Votes> votes;
//    @Transient
    private Map<ELECTIONTYPE, Votes> elections;
//    @Transient
    private Set<String> neighborIDs;
//    @Transient
    private Map<PARTYNAME, Integer> votesPerParty;
//    @Transient
    public ELECTIONTYPE userSelectedElectionType=ELECTIONTYPE.Presidential2016;
//    @Transient
    public int cnty;



    public Precinct(
            String ID,
            Geometry geometry,
            String geometryJSON,
            String districtID,
            int population,
            int gop_vote,
            int dem_vote,
            boolean isMajorityMinority,
            Map<DEMOGRAPHIC, Integer> precinctDemographics,
            Map<ELECTIONTYPE, Votes> elections,
            Set<String> neighborIDs) {
        	this.ID = ID;
        	this.geometry = geometry;
        	this.geometryJSON = geometryJSON;
        	this.originalDistrictID = districtID;
        	this.population = population;
            this.gop_vote = gop_vote;
            this.dem_vote = dem_vote;
            this.isMajorityMinority = isMajorityMinority;
            this.precinctDemographics = precinctDemographics;
            this.neighborIDs = neighborIDs;

    }
    public Precinct(String id){
        ID=id;
        geometryJSON="";
        originalDistrictID="";
        this.precinctDemographics=new HashMap<DEMOGRAPHIC,Integer>();
        this.geometry=null;
        this.population=0;
        this.gop_vote=0;
        this.dem_vote=0;
        neighborIDs=new HashSet<String>();
        elections = new HashMap<ELECTIONTYPE,Votes>();
//        for (DemographicGroup group:demographicGroups){
//            precinctDemographics.put(group.getGroupDemographic(),group.getPopulation());
//        }
//        for(ELECTIONTYPE e:ELECTIONTYPE.values()){
//            Map<PARTYNAME,Integer> electionResultSet = new HashMap<PARTYNAME,Integer>();
//            elections.put(e,electionResultSet);
//        }
//        for(Votes voting: votes){
//            elections.get(voting.getElectiontype()).put(voting.getParty(),voting.getVotes());
//            }
    }
    public Map<ELECTIONTYPE, Votes> getElections() {
        return elections;
    }
    public Map<DEMOGRAPHIC, Integer> getPrecinctDemographics() {
        return precinctDemographics;
    }
    public void setOriginalDistrictID(String districtID){originalDistrictID=districtID;}
    public void addNeighbor(String ID){
        neighborIDs.add(ID);
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String getID() {
        	return ID;
        }

    public String getGeometryJSON() {
        return geometryJSON;
    }
    public void setGeometryJSON(String geojson) {
         this.geometryJSON=geojson;
    }

    public Geometry getGeometry() {
            return geometry;
        }

    public double getPopulationDensity() {
        if (geometry !=null && geometry.getArea() != 0)
            return getPopulation() / geometry.getArea();
        return -1;
    }
    public void addVotes(ELECTIONTYPE e, Votes v){elections.put(e,v);}
    public void addDemographic(DEMOGRAPHIC d, int population){precinctDemographics.put(d,population);}

    @Override
    public String getOriginalDistrictID() {
            return originalDistrictID;
        }

    @Override
    public Set<String> getNeighborIDs() {
            return neighborIDs;
        }

    @Override
    public int getPopulation() {
        	return population;
        }
    public void setPopulation(int pop) { this.population =  pop;}

    @Override
    public int getGOPVote() {
            return gop_vote;
        }

    @Override
    public int getDEMVote() {
            return dem_vote;
        }

    public void setIsMajorityMinority(boolean isMajorityMinority){this.isMajorityMinority = isMajorityMinority;}

    public boolean getIsMajorityMinority(){return isMajorityMinority;}

    public DEMOGRAPHIC getLargestDemographic(){
        Integer maxDemographicSize = 0;
        DEMOGRAPHIC largestDemographic = null;
        for(DEMOGRAPHIC demographic : DEMOGRAPHIC.values()){
            if(demographic!=DEMOGRAPHIC.WHITE&&precinctDemographics.get(demographic) >= maxDemographicSize){
                maxDemographicSize = precinctDemographics.get(demographic);
                largestDemographic = demographic;
            }
        }
        return largestDemographic;
    }

    public double calculateDemographicSize(DEMOGRAPHIC largestDemographic, int population){
        Integer demographicSize = precinctDemographics.get(largestDemographic);
        return Double.valueOf(demographicSize)/population;
    }

    public boolean findVotingBlock(Result majorityMinorityResult, Double blockThreshold, Double votingThreshold,
                                   ELECTIONTYPE election){
        DEMOGRAPHIC largestDemographic = this.getLargestDemographic();
        if(largestDemographic == DEMOGRAPHIC.WHITE){
            return false;
        }
        double demographicSize = this.calculateDemographicSize(largestDemographic, this.population);

        if(demographicSize < blockThreshold){
            return false;
        }
        else{
            Votes selectedElection = elections.get(election);
            PARTYNAME winningParty = selectedElection.getWinningParty();
            if(winningParty==null){return false;}
            System.out.println("Precinct ID: "+this.ID+" Winning Party: "+winningParty.toString());
            Double winningPartyRatio = selectedElection.calculateWinningPartyRatio(winningParty);
            if(winningPartyRatio < votingThreshold){
                return false;
            }
            else{
                majorityMinorityResult.addToResult(this.ID, largestDemographic, winningParty);
                return true;
            }
        }
    }

    @Override
    public String toString() {

        return "\""+this.ID +"\": { "
                + "\"DistrictID\": "+"\""+this.originalDistrictID+"\"" + ", "
                + "\"White\": " + "\""+(precinctDemographics.get(DEMOGRAPHIC.WHITE)).toString()+"\"" + ", "
                + "\"Black\": " + "\""+(precinctDemographics.get(DEMOGRAPHIC.AFROAM)).toString()+ "\""+", "
                + "\"Hispanic\": " + "\""+(precinctDemographics.get(DEMOGRAPHIC.HISPANIC)).toString()+ "\""+", "
                + "\"Native\": " + "\""+(precinctDemographics.get(DEMOGRAPHIC.NATIVE)).toString()+ "\""+", "
                + "\"Pacific\": " + "\""+(precinctDemographics.get(DEMOGRAPHIC.PACISLAND)).toString()+ "\""+", "
                + "\"Asian\": " + "\""+(precinctDemographics.get(DEMOGRAPHIC.ASIAN)).toString()+ "\""+", "
                + "\"Other\": " + "\""+(precinctDemographics.get(DEMOGRAPHIC.OTHER)).toString()+ "\"" + ", "
                + "\"Population\": " + "\"" + population + "\""
                +" }";
    }

    public String electData(ELECTIONTYPE electiontype){
        return "\""+this.ID +"\": { "
                +"\"Republican\": "
                + "\""+elections.get(electiontype).getVotes().get(PARTYNAME.REPUBLICAN)+"\""+ ", "
                + "\"Democrat\": "
                + "\""+elections.get(electiontype).getVotes().get(PARTYNAME.DEMOCRAT)+"\""+ ", "
                + "\"Green\": "
                +"\""+ elections.get(electiontype).getVotes().get(PARTYNAME.GREEN)+ "\""+", "
                + "\"Libertarian\": "
                + "\""+elections.get(electiontype).getVotes().get(PARTYNAME.LIBERTARIAN)+ "\""
                +"}";
    }


//        public PARTYNAME getWinningParty(ELECTIONTYPE electiontype){
//        Map<PARTYNAME,Integer> votes = elections.get(electiontype);
//        PARTYNAME winningParty = null;
//        long winningVotes = 0;
//        for(PARTYNAME party : PARTYNAME.values()){
//            if(votes.get(party) > winningVotes){
//                winningVotes = votes.get(party);
//                winningParty = party;
//            }
//        }
//        return winningParty;
//    }
//    public double calculateWinningPartyRatio(PARTYNAME party,ELECTIONTYPE electiontype){
//        Map<PARTYNAME,Integer> votes = elections.get(electiontype);
//        Double totalVotes = 0.0;
//        for(PARTYNAME parties : PARTYNAME.values()){
//            totalVotes += votes.get(parties);
//        }
//        return votes.get(party)/totalVotes;
//    }



}
