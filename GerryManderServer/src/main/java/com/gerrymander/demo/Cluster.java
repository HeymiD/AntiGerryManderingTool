package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;

import java.util.*;

public class Cluster {
	
	public Set<Edge> edges;
	public Set<Precinct> precincts;
	private Map<DEMOGRAPHIC, Integer> clusterDemographics;
	private Map<ELECTIONTYPE, Votes> elections;
	private boolean isMajorityMinority;
	private int population;
	public String ID;


	public Cluster(String id){
		ID=id;
		precincts = new HashSet<Precinct>();
	}
	public Map<DEMOGRAPHIC, Integer> getClusterDemographics(){ return clusterDemographics;}
	public Map<ELECTIONTYPE, Votes> getElections(){return elections;}
	public int getPopulation(){return population;}
	public void setPopulation(int population){this.population = population;}
	public boolean getIsMajorityMinority(){return isMajorityMinority;}
	public void setIsMajorityMinority(boolean majorityMinority){this.isMajorityMinority = majorityMinority;}

	public DEMOGRAPHIC getLargestDemographic(){
		Integer maxDemographicSize = 0;
		DEMOGRAPHIC largestDemographic = null;
		for(DEMOGRAPHIC demographic : DEMOGRAPHIC.values()){
			if(clusterDemographics.get(demographic) > maxDemographicSize){
				maxDemographicSize = clusterDemographics.get(demographic);
				largestDemographic = demographic;
			}
		}
		return largestDemographic;
	}

	public double calculateDemographicSize(DEMOGRAPHIC largestDemographic, int population){
		Integer demographicSize = clusterDemographics.get(largestDemographic);
		return Double.valueOf(demographicSize)/population;
	}

	public boolean checkMajorityMinority(Double blockThreshold, Double votingThreshold,
								   ELECTIONTYPE election){
		DEMOGRAPHIC largestDemographic = this.getLargestDemographic();
		double demographicSize = this.calculateDemographicSize(largestDemographic, this.population);
		if(demographicSize < blockThreshold){
			return false;
		}
		else{
			Votes selectedElection = elections.get(election);
			PARTYNAME winningParty = selectedElection.getWinningParty();
			if(winningParty==null){return false;}
			Double winningPartyRatio = selectedElection.calculateWinningPartyRatio(winningParty);
			if(winningPartyRatio < votingThreshold){
				return false;
			}
			else {
				return true;
			}
		}
	}
	public District clusterToDistrict(){
		return new District();
	}


	public boolean equals(Cluster c) {
		if(this.ID==c.ID){
			return true;
		}
		else{
			return false;
		}
	}
}
