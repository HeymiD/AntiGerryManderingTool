package com.gerrymander.demo.models.concrete;


import com.gerrymander.demo.*;
import com.gerrymander.demo.measures.StateInterface;
//import com.gerrymander.demo.models.DAO.DistrictDAOInterface;
//import com.gerrymander.demo.models.Service.DistrictService;

import java.util.*;

public class State
		implements StateInterface<Precinct, District> {
	private String name;//name is saved for later storage into the database
	private HashMap<String, District> districts;
//	@OneToMany
//	@MapKey(name="state")
	private Map<String, Precinct> precincts;
//	@Transient
	public Map<String,District> oldDistricts;
	public ArrayList<Cluster> clusters;
	public ArrayList<Cluster> majMinClusters;
	public Result majMinPrecinctStats;
	public double userVoteThreshold;
	public double userDemographicThreshold;
	public ELECTIONTYPE userSelectedElection=ELECTIONTYPE.Presidential2016;
//	public boolean clusterListModified;
	public int population;
	public Set<Cluster> combinedClusters;
	public Set<Cluster> visited;
	public String[] demString;

	public State(String name, Set<Precinct> inPrecincts) {
		this.name = name;
		this.districts = new HashMap<>();
		this.precincts = new HashMap<>();
		for (Precinct p : inPrecincts) {
			String districtID = p.getOriginalDistrictID();
			District d = districts.get(districtID);
			if (d == null) {
				d = new District();
				d.setID(districtID);
				d.setState(this);
				districts.put(districtID, d);

			}
			d.addPrecinct(p);
			this.precincts.put(p.getID(), p);
		}
		this.majMinPrecinctStats = new Result(this.name,this.precincts.size());
        this.population = districts.values().stream().mapToInt(District::getPopulation).sum();
        oldDistricts = new HashMap<String, District>();


	}
	public State(String name){
		this.name = name;
		this.districts = new HashMap<>();
		this.precincts = new HashMap<String, Precinct>();
//		this.population = districts.values().stream().mapToInt(District::getPopulation).sum();
		oldDistricts = new HashMap<String, District>();
		clusters = new ArrayList<Cluster>();
		this.population=0;
		majMinClusters=new ArrayList<Cluster>();
		combinedClusters = new HashSet<Cluster>();
	}

	public void putDistrict(District d){districts.put(d.getID(),d);}

	public String getName(){return name;}

	public Set<Precinct> getPrecincts(){
        return new HashSet<>(precincts.values());
		}

	public void setPrecincts(Map<String,Precinct> precincts){
		this.precincts=precincts;
	}

	public void addPrecinct(Precinct precinct){precincts.put(precinct.getID(),precinct);}

	public Set<District> getDistricts() {
		return new HashSet<>(districts.values());
	}

	public District getDistrict(String distID) {
		return districts.get(distID);
	}

	public Precinct getPrecinct(String precID) {
		return precincts.get(precID);
	}

	@Override
	public int getPopulation() {
		return this.population;
	}

	public Result findMajMinPrecincts(Double blockThreshold, Double votingThreshold, ELECTIONTYPE election){
		for(Precinct precinct : this.getPrecincts()){
			precinct.setIsMajorityMinority(precinct.findVotingBlock(majMinPrecinctStats,blockThreshold,
					votingThreshold,election));
		}
		return majMinPrecinctStats;
	}
	public boolean makeMajMinClusters() {
	    combinedClusters.clear();
        boolean clusterListModified = false;
        Iterator<Cluster> iterator = clusters.iterator();
        while (iterator.hasNext()) {

            Cluster currCluster = iterator.next();
            for (Edge edge : currCluster.edges) {
                Cluster neighborc = edge.getNeighbor(currCluster);
                Cluster neighbor = findCluster(neighborc.getID());
//                if(neighbor==null){
//                    continue;
//                }
                if (testCombineClusters(currCluster, neighbor) != null) {
                    clusterListModified = true;
                    combineClusters(currCluster, neighbor);
                    combinedClusters.add(neighbor);
                    iterator.remove();
                    break;
                }

            }
        }
        return clusterListModified;
    }

    //Neighbor always eats current so current gets removed
	public void combineClusters(Cluster current, Cluster neighbor) {

        System.out.println("Combining "+current.getID()+" and "+neighbor.getID());
        neighbor.precinctsCluster.addAll(current.precinctsCluster);
        neighbor.precinctsCluster.forEach(p->{p.newDistrictID=neighbor.getID();});
//        System.out.println("Num of Precincts: "+neighbor.precinctsCluster.size());
        System.out.println("OLD POP: "+neighbor.population);
        neighbor.setPopulation(neighbor.getPopulation()+current.getPopulation());
        System.out.println("Pop: "+neighbor.population);
        System.out.println("CurPop: "+current.population);

        for (DEMOGRAPHIC demograhpic : DEMOGRAPHIC.values()) {
            int combined = current.getClusterDemographics().get(demograhpic) + neighbor.getClusterDemographics().get(demograhpic);
            neighbor.getClusterDemographics().put(demograhpic, combined);
        }
        for (PARTYNAME partyname : PARTYNAME.values()) {
            int combined = neighbor.getElections().get(userSelectedElection).getVotes().get(partyname) +
                    current.getElections().get(userSelectedElection).getVotes().get(partyname);
            neighbor.getElections().get(userSelectedElection).getVotes().put(partyname, combined);
        }
        neighbor.setGop_vote(current.getGOPVote() + neighbor.getGOPVote());
        neighbor.setDem_vote(neighbor.getDEMVote() + current.getDEMVote());
        int internalEdges = 0;
        int externalEdges = 0;
        for (Precinct p : neighbor.precinctsCluster) {
            for (String neighborID : p.getNeighborIDs()) {
                if (neighbor.precinctsCluster.stream().anyMatch(precinct -> {
                    return precinct.getID().equals(neighborID);
                })) {
                    internalEdges++;
                }
                else{externalEdges++;}
            }
        }
        neighbor.internalEdges = internalEdges;
        neighbor.externalEdges = externalEdges;
        for(Edge e:neighbor.edges){
            if(e.neighbor1.getID().equals(e.neighbor2.getID())){
                System.out.println("NeighborPreExisting: SELF LOOP");
            }
        }
        for (Edge edge : current.edges) {
            edge.updateNeighbor(current, neighbor);
            if (!edge.neighbor1.getID().equals(edge.neighbor2.getID())) {
                neighbor.edges.add(edge);
//                System.out.println("Edge\n n1: "+edge.neighbor1.getID()
//                        +" n2: "+edge.neighbor2.getID());
//                for(Edge e:neighbor.edges){
//                    if(e.neighbor1.getID().equals(e.neighbor2.getID())){
//
//                        System.out.println("SIKIMA");
//                        System.exit(0);
//                    }
//                }
            }
        }
        Iterator<Edge> iterator = neighbor.edges.iterator();
        while(iterator.hasNext()){
            Edge e = iterator.next();
            if(e.neighbor1.getID().equals(e.neighbor2.getID())){
                System.out.println("YIKES!");
                iterator.remove();
            }
        }


//        for (Edge edge : neighbor.edges) {
//            System.out.println("Current Id: "+current.getID());
//            System.out.println("Edge: ");
//            System.out.println("Neighbor1: "+edge.neighbor1.getID()+" POP: "+edge.neighbor1.getPopulation());
//            System.out.println("Neighbor2: "+edge.neighbor2.getID()+" POP: "+edge.neighbor2.getPopulation());
//        }

    }

	public Cluster testCombineClusters(Cluster c1, Cluster c2){
        Cluster combinedCluster = new Cluster(c1.getID());
        combinedCluster.clusterDemographics.putAll(c1.getClusterDemographics());
		combinedCluster.elections.putAll(c1.elections);
		combinedCluster.setDem_vote(c1.getDEMVote());
		combinedCluster.setGop_vote(c1.getGOPVote());
		combinedCluster.population=c1.getPopulation()+c2.getPopulation();
		combinedCluster.externalEdges =c1.externalEdges+c2.externalEdges-1;

        for(DEMOGRAPHIC demograhpic: DEMOGRAPHIC.values()){
            int combined = combinedCluster.getClusterDemographics().get(demograhpic) + c2.getClusterDemographics().get(demograhpic);
            combinedCluster.getClusterDemographics().put(demograhpic,combined);
        }
        for(PARTYNAME partyname :PARTYNAME.values()){
            int combined = combinedCluster.getElections().get(userSelectedElection).getVotes().get(partyname) +
                    c2.getElections().get(userSelectedElection).getVotes().get(partyname);
            combinedCluster.getElections().get(userSelectedElection).getVotes().put(partyname, combined);
        }
        combinedCluster.setGop_vote(c1.getGOPVote()+c2.getGOPVote());
        combinedCluster.setDem_vote(c1.getDEMVote()+c2.getDEMVote());
        if(combinedCluster.population>700000){
            return null;
        }
        boolean majmin = false;
        int newSizeMajMinClusters = 0;
        if(combinedCluster.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection,demString)){
            majmin=true;
            newSizeMajMinClusters++;
        }
        if(majmin==false){
            return null;
        }
        if(c1.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection,demString)){
            newSizeMajMinClusters--;
        }
        if(c2.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection,demString)){
            newSizeMajMinClusters--;
        }
        if(newSizeMajMinClusters>0){
            System.out.println("Non Majority minorities combining:");
            System.out.println("C1: "+c1.getID());
            System.out.println("C2: "+c2.getID());
            return combinedCluster;
        }
        if(newSizeMajMinClusters==0){
            return combinedCluster;
        }
        else{

            return null;
        }
    }

    public Cluster findCluster(String ID){
        for(Cluster c:clusters){
            if(c.getID().equals(ID)){
                return c;
            }
        }
        System.out.println("Neighbor NOT FOUND ID was "+ID);
        return null;
    }
}

//	public void undo_combineCluster(Cluster combined, Cluster c1, Cluster c2){
//	    combinedClusters.remove(combined);
//		if (majMinClusters.contains(combined)){
//			majMinClusters.remove(combined);
//		}
//        clusters.remove(clusters.indexOf(combined));
//
//
//		if(c1.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection)){
//			majMinClusters.add(c1);
//		}
//		clusters.add(c1);
//		if(c2.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection)){
//			majMinClusters.add(c2);
//		}
//		clusters.add(c2);
//		return;
//	}


//        ((District)combinedCluster).externalEdges = combinedCluster.edges.size()-((District)combinedCluster).internalEdges;
//        ((District)combinedCluster).internalEdges = ((District)c1).internalEdges+((District)c2).internalEdges+1;
//		Cluster combinedCluster = new Cluster(c1.getID()+"|"+c2.getID()+"|");
//		combinedCluster.clusterDemographics.putAll(c1.getClusterDemographics());
//		combinedCluster.elections.putAll(c1.elections);
//		combinedCluster.setDem_vote(c1.getDEMVote());
//		combinedCluster.setGop_vote(c1.getGOPVote());
//		combinedCluster.edges.addAll(c1.edges);
//        for(Edge e:c1.edges){
//            Edge edge = new Edge(combinedCluster,e.dest);
//            combinedCluster.addEdge(edge);
//        }
////        combinedCluster.edges.addAll(c2.edges);
//        for(Edge e:c2.edges){
//            Edge edge = new Edge(combinedCluster,e.dest);
//            combinedCluster.addEdge(edge);
//        }
//        for(Edge e:combinedCluster.edges){
////            for(Edge edge:e.dest.edges){
////                edge.clusters.remove(edge.dest);
////                edge.dest=combinedCluster;
////                edge.clusters.add(edge.dest);
////            }
//            try{
//                for(Edge edge:findCluster(e.dest.getID()).edges){
//                edge.clusters.remove(edge.dest);
//                edge.dest=combinedCluster;
//                edge.clusters.add(edge.dest);
//                }
//            }
//            catch(NullPointerException exception){}
//        }

//        combinedCluster.precinctsCluster.addAll(c1.precinctsCluster);
//        combinedCluster.precinctsCluster.addAll(c2.precinctsCluster);
//        clusters.set(clusters.indexOf(c1),null);
//        clusters.set(clusters.indexOf(c2),null);
//        clusters.remove(clusters.indexOf(c1));
//        clusters.remove(clusters.indexOf(c2));


//		Set<Edge[]> commonNeighbors = findCommonNeighbors(combinedCluster,c2);
//		Set<Edge> averageEdges = new HashSet<Edge>();
//		for (Edge[] edge_pair:commonNeighbors){
//			try{
//				averageEdges.add(makeAverageEdge(edge_pair[0],edge_pair[1]));
//				for(Edge e:edge_pair){
//					if(combinedCluster.edges.contains(e)){
//						combinedCluster.edges.remove(e);
//					}
//				}
//			}
//			catch (NullPointerException e){
//
//			}
//		}

//		for (Edge e:averageEdges){
//			combinedCluster.edges.add(e);
//		}
//		clusters.add(combinedCluster);
//		combinedClusters.add(combinedCluster);
//		districts.put(combinedCluster.getID(),combinedCluster);
//		return combinedCluster;
//	}

//	public Set<Edge[]> findCommonNeighbors(Cluster c1, Cluster c2){
//		Set<Edge[]> commonNeighbors = new HashSet<Edge[]>();
//		for (Edge edge:c1.edges){
//			for (Edge edge2: c2.edges){
//				if(edge.dest.equals(edge2.dest)){
//					Edge[] commonEdges = new Edge[2];
//					commonEdges[0]=edge;
//					commonEdges[1]=edge2;
//					commonNeighbors.add(commonEdges);
//				}
//			}
//		}
//
//		return commonNeighbors;
//	}

//	public Edge makeAverageEdge(Edge e1,Edge e2){
//		if ((e1.src.getID()!=e2.src.getID()) ||(e1.dest.getID()!=e2.dest.getID())){
//			System.out.print("They are not common edges.");
//			return null;
//		}
//		Edge averageEdge = new Edge(e1.src,e1.dest);
//		averageEdge.weight=(e1.weight+e2.weight)/2;
//		return averageEdge;
//	}


//		for (int i=0;i<clusters.size();i++){
//		    Cluster c = clusters.get(i);
//		    if(c==null){
//		        continue;
//            }
//		    if(combinedClusters.contains(c)){
//		        continue;
//            }
//			Set<Cluster> neighbors = new HashSet<Cluster>();
//			for (Edge edge: c.edges){
//			    System.out.println("Neigbor: "+edge.getNeighbor(c).getID());
//			    if(!combinedClusters.contains(edge.getNeighbor(c)) && findCluster(edge.getNeighbor(c).getID())!=null){
//                    neighbors.add(edge.getNeighbor(c));
//                }
//			}
//            System.out.println("Current ID: "+c.getID());
//			System.out.println("Combined Clusters: "+combinedClusters.size());
//			System.out.println("Num of neighbors: "+neighbors.size());
//			for (Cluster neighbor: neighbors){
//			    if(neighbor==null){
//			        System.out.println("Neighbor is null!");
//                }
//				if (testCombineClusters(c,neighbor)!=null){
//				    Cluster combined = combineClusters(c,neighbor);
////				    majMinClusters.add(combined);
//				    clusterListModified=true;
//				    break;
//				}
//			}
//		}

//	}