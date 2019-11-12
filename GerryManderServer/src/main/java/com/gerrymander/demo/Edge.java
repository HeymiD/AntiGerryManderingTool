package com.gerrymander.demo;

public class Edge {
	
	public Cluster src;
	public Cluster dest;
	public Double weight;
	
	public Edge(Cluster src, Cluster dest) 
	{
		this.src=src;
		this.dest=dest;
	}
	
	public Cluster getNeighbor(Cluster c) 
	{
		if (c.ID==src.ID) 
		{
			return dest;
		}
		else if(c.ID==dest.ID)
		{
			return src;
		}
		else {
			return null;
		}
	}

}
