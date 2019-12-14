package com.gerrymander.demo;

import java.util.HashSet;
import java.util.Set;

public class Edge {
	
	public Cluster neighbor1;
	public Cluster neighbor2;
//	public Set<Cluster> clusters;
	
	public Edge(Cluster src, Cluster dest) 
	{
//		this.src=src;
//		this.dest=dest;
		neighbor1=src;
		if(src==null){
			System.out.println("SRC is NULL");
		}
		neighbor2=dest;
		if(dest==null){
			System.out.println("DEST is NULL");
		}
	}
	
	public Cluster getNeighbor(Cluster c)
	{
		if (c.getID().equals(neighbor1.getID()))
		{
			return neighbor2;
		}
		else if(c.getID().equals(neighbor2.getID()))
		{
			return neighbor1;
		}
		else {
			System.out.println("NEIGHBOR 1: "+neighbor1.getID());
			System.out.println("NEIGHBOR 2: "+neighbor2.getID());
			System.out.println("CLUSTER: "+c.getID());
			System.out.println("NEIGHBOR NOT FOUND FUUUUUUUCK!!!!!");
			return null;
		}
	}

	public void updateNeighbor(Cluster oldCluster, Cluster newCluster){
		if(newCluster==null){
			System.out.println("NEW CLUSTER NULL");
		}

		if(oldCluster.getID().equals(neighbor1.getID())){
			neighbor1=newCluster;
		}
		else if(oldCluster.getID().equals(neighbor2.getID())){
			neighbor2=newCluster;

		}
		else{
			System.out.println("BYEEEE");
			System.exit(0);
		}

	}

//	public Cluster getNeighbor(Cluster c){
//		for (Cluster cluster:clusters){
//			if(cluster.getID()!=c.getID()){
//				return cluster;
//			}
//		}
//		return null;
//	}


	@Override
	public boolean equals(Object o){
		if(o instanceof Edge){
			Edge toCompare = (Edge) o;
			if(toCompare.neighbor1.getID().equals(this.neighbor1.getID())&&
					toCompare.neighbor2.getID().equals(this.neighbor1.getID())){
				return true;
			}
			if(toCompare.neighbor1.getID().equals(this.neighbor2.getID())&&
					toCompare.neighbor2.getID().equals(this.neighbor1.getID())){
				return true;
			}
			return false;
		}
		return false;

	}

}
