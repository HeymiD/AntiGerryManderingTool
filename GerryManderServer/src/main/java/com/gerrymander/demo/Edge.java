package com.gerrymander.demo;

import java.util.Set;

public class Edge {
	
	public Cluster src;
	public Cluster dest;
	public Set<Cluster> clusters;
	public Double weight;
	
	public Edge(Cluster src, Cluster dest) 
	{
		this.src=src;
		this.dest=dest;
	}
	
//	public Cluster getNeighbor(Cluster c)
//	{
//		if (c.ID==src.ID)
//		{
//			return dest;
//		}
//		else if(c.ID==dest.ID)
//		{
//			return src;
//		}
//		else {
//			return null;
//		}
//	}

	public Cluster getNeighbor(Cluster c){
		for (Cluster cluster:clusters){
			if(cluster.ID!=c.ID){
				return cluster;
			}
		}
		return null;
	}


	public boolean compareTo(Edge e){
		if(clusters.equals(e.clusters)){
			return true;
		}
		else{
			return false;
		}
	}

}
