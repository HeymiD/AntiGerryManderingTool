package com.gerrymander.demo.models.concrete;

import com.gerrymander.demo.*;
import com.gerrymander.demo.measures.PrecinctInterface;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.*;
@Entity
@Table(name="Precints_GEO")
public class Precinct implements PrecinctInterface {
    @Id
    @Column(name="PCTKEY")
    private final String ID;
    @Transient
    private final Geometry geometry;
    @Transient
    private final String geometryJSON;
    @Transient
    private final String originalDistrictID;
    @Transient
    private final int population;
    @Transient
    private final int gop_vote;
    @Transient
    private final int dem_vote;
    @Transient
    private boolean isMajorityMinority;
    @Transient
    private Map<DEMOGRAPHIC, Integer> precinctDemographics;
    @Transient
    private Set<DemographicGroup> demographicGroups;
    @Transient
    private Set<Votes> votes;
    @Transient
    private Map<ELECTIONTYPE, Votes> elections;
    @Transient
    private final Set<String> neighborIDs;
    @Transient
    private Map<PARTYNAME, Integer> votesPerParty;



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
    public Precinct(String id,String geojson){
        ID=id;
        geometryJSON=geojson;
        originalDistrictID="";
        this.precinctDemographics=new HashMap<DEMOGRAPHIC,Integer>();
        this.geometry=null;
        this.population=0;
        this.gop_vote=0;
        this.dem_vote=0;
        neighborIDs=null;
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

    @Override
    public String getID() {
        	return ID;
        }

    public String getGeometryJSON() {
        return geometryJSON;
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

    public Set<DEMOGRAPHIC> getLargestDemographic(Set<DEMOGRAPHIC> demographicsToCombine){
        Integer maxDemographicSize = 0;
        DEMOGRAPHIC largestDemographic = null;
        Integer combinedDemographicSize = 0;
        for(DEMOGRAPHIC demographic : DEMOGRAPHIC.values()){
            if(precinctDemographics.get(demographic) > maxDemographicSize){
                maxDemographicSize = precinctDemographics.get(demographic);
                largestDemographic = demographic;
            }
        }
        Set<DEMOGRAPHIC> demographicToReturn = new HashSet<>();
        demographicToReturn.add(largestDemographic);
        try {
            for (DEMOGRAPHIC userSelectedDemographic : demographicsToCombine) {
                combinedDemographicSize += precinctDemographics.get(userSelectedDemographic);
            }
        }
        catch(NullPointerException e) {
            return demographicToReturn;
        }
        if (maxDemographicSize < combinedDemographicSize) {
            return demographicsToCombine;
        }
        return demographicToReturn;
    }

    public double calculateDemographicSize(Set<DEMOGRAPHIC> largestDemographic, int population){
        Integer demographicSize = 0;
        for(DEMOGRAPHIC demographic : largestDemographic){
            demographicSize += precinctDemographics.get(demographic);
        }
        return Double.valueOf(demographicSize)/population;
    }

    public boolean findVotingBlock(Result majorityMinorityResult, Double blockThreshold, Double votingThreshold,
                                   ELECTIONTYPE election, Set<DEMOGRAPHIC> combinedDemographics){
        Set<DEMOGRAPHIC> largestDemographicsSet = this.getLargestDemographic(combinedDemographics);
        double demographicSize = this.calculateDemographicSize(largestDemographicsSet, this.population);
        if(demographicSize < blockThreshold){
            return false;
        }
        else{
            Votes selectedElection = elections.get(election);
            PARTYNAME winningParty = selectedElection.getWinningParty();
            Double winningPartyRatio = selectedElection.calculateWinningPartyRatio(winningParty);
            if(winningPartyRatio < votingThreshold){
                return false;
            }
            else{
                majorityMinorityResult.addToResult(this.ID, largestDemographicsSet, winningParty);
                return true;
            }
        }
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