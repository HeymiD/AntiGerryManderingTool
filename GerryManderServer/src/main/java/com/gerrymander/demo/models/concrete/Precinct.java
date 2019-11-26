package com.gerrymander.demo.models.concrete;

import com.gerrymander.demo.*;
import com.gerrymander.demo.measures.PrecinctInterface;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.*;
import java.util.*;
@Entity
@Table(name="Precints_GEO")
public class Precinct implements PrecinctInterface {
    @Id
    @Column(name="PCTKEY")
    private final String ID;
    @OneToMany(mappedBy = "PCTKEY")
    private Ethnicities ethnicities;
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
    private Map<DEMOGRAPHIC, Integer> demographics;
    @OneToMany(mappedBy = "PCTKEY")
    private Set<Votes> votes;
    @Transient
    private Map<ELECTIONTYPE, Votes> elections;
    @Transient
    private final Set<String> neighborIDs;


    public Precinct(
            String ID,
            Geometry geometry,
            String geometryJSON,
            String districtID,
            int population,
            int gop_vote,
            int dem_vote,
            boolean isMajorityMinority,
            Map<DEMOGRAPHIC, Integer> demographics,
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
            this.demographics = demographics;
            this.elections = elections;
            this.neighborIDs = neighborIDs;
    }
    public Precinct(String id,String geojson,
                    String districtId){
        ID=id;
        geometryJSON=geojson;
        originalDistrictID=districtId;
        this.demographics=null;
        this.geometry=null;
        this.population=0;
        this.gop_vote=0;
        this.dem_vote=0;
        neighborIDs=null;
        demographics.put(DEMOGRAPHIC.WHITE,ethnicities.getWhite());
        demographics.put(DEMOGRAPHIC.AFROAM,ethnicities.getBlack());
        demographics.put(DEMOGRAPHIC.WHITE,ethnicities.getWhite());
        demographics.put(DEMOGRAPHIC.WHITE,ethnicities.getWhite());
        demographics.put(DEMOGRAPHIC.WHITE,ethnicities.getWhite());
        demographics.put(DEMOGRAPHIC.WHITE,ethnicities.getWhite());



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
        Integer maxDemographicSize = Integer.valueOf(0);
        DEMOGRAPHIC largestDemographic = null;
        Integer combinedDemographicSize = Integer.valueOf(0);
        for(DEMOGRAPHIC demographic : DEMOGRAPHIC.values()){
            if(demographics.get(demographic) > maxDemographicSize){
                maxDemographicSize = demographics.get(demographic);
                largestDemographic = demographic;
            }
        }
        Set<DEMOGRAPHIC> demographicToReturn = new HashSet<>();
        demographicToReturn.add(largestDemographic);
        try {
            for (DEMOGRAPHIC combiningDemographic : demographicsToCombine) {
                combinedDemographicSize += demographics.get(combiningDemographic);
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

    public Long calculateDemographicSize(Set<DEMOGRAPHIC> largestDemographic,int population){
        Long demographicSize = Long.valueOf(0);
        for(DEMOGRAPHIC demographic : largestDemographic){
            demographicSize += demographics.get(demographic);
        }
        return demographicSize/population;
    }

    public boolean findVotingBlock(Result majorityMinorityResult, Long blockThreshold, Long votingThreshold,
                                   ELECTIONTYPE election, Set<DEMOGRAPHIC> combinedDemographics){
        Set<DEMOGRAPHIC> largestDemographicsSet = this.getLargestDemographic(combinedDemographics);
        Long demographicSize = this.calculateDemographicSize(largestDemographicsSet, this.population);
        if(demographicSize < blockThreshold){
            return false;
        }
        else{
            Votes selectedElection = elections.get(election);
            PARTYNAME winningParty = selectedElection.getWinningParty();
            Long winningPartyRatio = selectedElection.calculateWinningPartyRatio(winningParty);
            if(winningPartyRatio < votingThreshold){
                return false;
            }
            else{
                majorityMinorityResult.addToResult(this.ID, largestDemographicsSet, winningParty);
                return true;
            }
        }
    }
}