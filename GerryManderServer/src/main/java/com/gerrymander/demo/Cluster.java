package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;

import java.util.*;

public class Cluster {
	
	public Set<Edge> edges;
	public Set<Precinct> precincts;
	public String ID;

	public Cluster(String id){
		ID=id;
		precincts = new HashSet<Precinct>();
	}
	public boolean checkMajMin(){
		boolean value = false;
		return value;
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
