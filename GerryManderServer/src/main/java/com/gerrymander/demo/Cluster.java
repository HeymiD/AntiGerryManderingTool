package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

public class Cluster extends District{
	
	public Set<Edge> edges;
	public Set<Precinct> precinctsCluster;
	public Map<DEMOGRAPHIC, Integer> clusterDemographics;
	public Map<ELECTIONTYPE, Votes> elections;
	private boolean isMajorityMinority;


//	private int population;
//	public String ID;


	public Cluster(String id){
		super();
		setID(id);
		precinctsCluster = new HashSet<Precinct>();
		edges = new HashSet<Edge>();
		clusterDemographics = new HashMap<DEMOGRAPHIC,Integer>();
		elections = new HashMap<ELECTIONTYPE,Votes>();

	}
	public Map<DEMOGRAPHIC, Integer> getClusterDemographics(){ return clusterDemographics;}
	public void setClusterDemographics(Map<DEMOGRAPHIC, Integer> clusterDemographics) {
		this.clusterDemographics = clusterDemographics;
	}
	public Map<ELECTIONTYPE, Votes> getElections(){return elections;}
//	public int getPopulation(){return population;}
//	public void setPopulation(int population){this.population = population;}
	public boolean getIsMajorityMinority(){return isMajorityMinority;}
	public void setIsMajorityMinority(boolean majorityMinority){this.isMajorityMinority = majorityMinority;}

	public DEMOGRAPHIC getLargestDemographic(){
		Integer maxDemographicSize = 0;
		DEMOGRAPHIC largestDemographic = null;
		for(DEMOGRAPHIC demographic : DEMOGRAPHIC.values()){
			if(demographic==DEMOGRAPHIC.WHITE){
				continue;
			}
//			System.out.println(demographic.toString()+": "+clusterDemographics.get(demographic));
			if(clusterDemographics.get(demographic) >= maxDemographicSize){
				maxDemographicSize = clusterDemographics.get(demographic);
				largestDemographic = demographic;
			}
		}
		return largestDemographic;
	}

	public double calculateDemographicSize(DEMOGRAPHIC largestDemographic, int population){
		if(largestDemographic==null){
			System.out.println("LARGEST DEM NULL");
		}
		int demographicSize = clusterDemographics.get(largestDemographic);
		return Double.valueOf(demographicSize)/population;
	}

	public boolean checkMajorityMinority(double blockThreshold, double votingThreshold,
								   ELECTIONTYPE election, String[] demsStrings){
		int demSize=0;
		for(String d:demsStrings){
			demSize+=clusterDemographics.get(DEMOGRAPHIC.valueOf(d));
		}
		DEMOGRAPHIC largestDemographic = this.getLargestDemographic();
		double demographicSize = 0.0;
		if(demSize>clusterDemographics.get(largestDemographic)){
			 demographicSize = demSize/population;
		}
		else{
			 demographicSize = this.calculateDemographicSize(largestDemographic, getPopulation());
		}
		if(demographicSize < blockThreshold){
			return false;
		}
		else{
			Votes selectedElection = elections.get(election);
			PARTYNAME winningParty = selectedElection.getWinningParty();

			if(winningParty==null){return false;}
			double winningPartyRatio = selectedElection.calculateWinningPartyRatio(winningParty);

			if(winningPartyRatio < votingThreshold){
				return false;
			}
			else {

				return true;
			}
		}
	}
	public District clusterToDistrict(){

		District district = new District();

		return district;
	}

    @Override
    public boolean equals(Object o){
        if(o instanceof Cluster){
            Cluster toCompare = (Cluster) o;
            return this.getID().equals(toCompare.getID());
        }
        return false;
    }

//    public void addEdge(Edge newEdge){
//		for(Edge e:edges){
//			if(!e.src.getID().equals(newEdge.src.getID())){
//				edges.add(newEdge);
//				return;
//			}
//			if(!e.dest.getID().equals(newEdge.dest.getID())){
//				edges.add(newEdge);
//				return;
//			}
//		}
//	}

//    @Override
//	public boolean equals(Object c) {
//		if(this.ID==((Cluster)c).ID){
//			return true;
//		}
//		else{
//			return false;
//		}
//	}
	@Override
    public int hashCode() {
        return getID().hashCode();
    }
}
