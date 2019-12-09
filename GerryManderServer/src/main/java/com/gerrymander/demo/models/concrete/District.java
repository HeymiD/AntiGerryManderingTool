package com.gerrymander.demo.models.concrete;

import com.gerrymander.demo.DEMOGRAPHIC;
import com.gerrymander.demo.ELECTIONTYPE;
import com.gerrymander.demo.Election;
import com.gerrymander.demo.Votes;
import com.gerrymander.demo.measures.DistrictInterface;
import com.gerrymander.demo.models.concrete.State;
import org.locationtech.jts.algorithm.MinimumBoundingCircle;
import org.locationtech.jts.geom.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
//@Entity
//@Table(name="Districts")
public class District implements DistrictInterface<Precinct>{
//        implements DistrictInterface<com.gerrymander.demo.models.concrete.Precinct>
//{   @Column(name="geojson")
    private String geoData;
//    @Id
//    @Column(name="id",nullable = false)
    private String ID;
//    @Transient
    private HashMap<DEMOGRAPHIC,Double> demographics;
//    @Transient
//    private int totPop;
//    @Transient
//    public Map<ELECTIONTYPE,Votes> electionData;
    @Transient
    private State state;
    @Transient
    private static final int MAXX = 0;
    @Transient
    private static final int MAXY = 1;
    @Transient
    private static final int MINX = 2;
    @Transient
    private static final int MINY = 3;
    @Transient
    public int population;
    @Transient
    private int gop_vote;
    @Transient
    private int dem_vote;
    @Transient
    public int internalEdges = 0;
    @Transient
    public int externalEdges = 0;
    @Transient
    private HashMap<String, Precinct> precincts;
    @Transient
    private Set<Precinct> borderPrecincts;
    @Transient
    private MultiPolygon multiPolygon;
    @Transient
    private Geometry boundingCircle;
    @Transient
    private Geometry convexHull;
    @Transient
    private boolean boundingCircleUpdated=false;
    @Transient
    private boolean multiPolygonUpdated=false;
    @Transient
    private boolean convexHullUpdated=false;

    /*public ConcreteDistrict(String ID) {
            this(ID, null);
        }*/
//, State state
    public District( ) {
//        this.ID = ID;
        population = 0;
        gop_vote = 0;
        dem_vote = 0;
        internalEdges=0;
        precincts = new HashMap<String, Precinct>();
        borderPrecincts = new HashSet<Precinct>();
//        this.state = state;
    }

    public void putPrecinct(String ID,Precinct p){precincts.put(ID,p);}

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID=id;
    }

    public int getPopulation() {
        return population;
    }

    public int getGOPVote() {
        return gop_vote;
    }

    public int getDEMVote() {
        return dem_vote;
    }

    public void setGop_vote(int gop_vote) {
        this.gop_vote = gop_vote;
    }

    public void setDem_vote(int dem_vote) {
        this.dem_vote = dem_vote;
    }

    public int getInternalEdges() {
        return internalEdges;
    }

    public int getExternalEdges() {
        return externalEdges;
    }

    public void setInternalEdges(int internalEdges) {
        this.internalEdges = internalEdges;
    }

    public void setExternalEdges(int externalEdges) {
        this.externalEdges = externalEdges;
    }

//    public HashMap<DEMOGRAPHIC, Double> getDemographics() {
//        return demographics;
//    }
//
//    public void setDemographics(HashMap<DEMOGRAPHIC, Double> demographics) {
//        this.demographics = demographics;
//    }
//
//    public int getTotPop() {
//        return totPop;
//    }
//
//    public void setTotPop(int totPop) {
//        this.totPop = totPop;
//    }

//    public ArrayList<Election> getElectionData() {
//        return electionData;
//    }
//
//    public void setElectionData(ArrayList<Election> electionData) {
//        this.electionData = electionData;
//    }

    public String getGeoData() {
        return geoData;
    }

    public void setGeoData(String geoData) {
        this.geoData = geoData;
    }

    public boolean isMajMin()
    {
        for (DEMOGRAPHIC minority: demographics.keySet())
        {
            if ((demographics.get(minority)/population)>=0.5)
            {
                return true;
            }
        }
        return false;
    }

    private Set<Precinct> getInternalNeighbors(Precinct p) {
        Set<Precinct> neighborsInternal = new HashSet<>();
        for (String nid : p.getNeighborIDs()) {
            if (precincts.containsKey(nid)) {
                Precinct neighbor = precincts.get(nid);
                neighborsInternal.add(neighbor);
            }
        }
        return neighborsInternal;
    }

    public Set<Precinct> getPrecincts() {
        Set<Precinct> to_return = new HashSet<Precinct>();
        for (Precinct p : precincts.values()) {
            to_return.add(p);
        }
        return to_return;
    }

    public Set<Precinct> getBorderPrecincts() {
        return new HashSet<>(borderPrecincts);
    }

    public boolean isBorderPrecinct(Precinct precinct) {
        for (String neighborID : precinct.getNeighborIDs()) {
            //if the neighbor's neighbor is not in the district, then it is outer
            if (!precincts.containsKey(neighborID))  {
                return true;
            }
        }
        return false;
    }

    public Precinct getPrecinct(String PrecID) {
        return precincts.get(PrecID);
    }

    public void addPrecinct(Precinct p) {
        precincts.put(p.getID(), p);
        population += p.getPopulation();
        gop_vote += p.getGOPVote();
        dem_vote += p.getDEMVote();
        borderPrecincts.add(p);
        Set<Precinct> newInternalNeighbors = getInternalNeighbors(p);
        int newInternalEdges = newInternalNeighbors.size();
        internalEdges += newInternalEdges;
        externalEdges -= newInternalEdges;
        externalEdges += (p.getNeighborIDs().size() - newInternalEdges);
        newInternalNeighbors.removeIf(
                this::isBorderPrecinct
        );
        borderPrecincts.removeAll(newInternalNeighbors);

        this.multiPolygonUpdated = false;
        this.convexHullUpdated = false;
        this.boundingCircleUpdated = false;
    }

    public void removePrecinct(Precinct p) {
        precincts.remove(p.getID());
        population -= p.getPopulation();
        gop_vote -= p.getGOPVote();
        dem_vote -= p.getDEMVote();
        Set<Precinct> lostInternalNeighbors = getInternalNeighbors(p);
        int lostInternalEdges = lostInternalNeighbors.size();
        internalEdges -= lostInternalEdges;
        externalEdges += lostInternalEdges;
        externalEdges -= (p.getNeighborIDs().size() - lostInternalEdges);
        borderPrecincts.remove(p);
        borderPrecincts.addAll(lostInternalNeighbors);

        this.multiPolygonUpdated = false;
        this.convexHullUpdated = false;
        this.boundingCircleUpdated = false;
    }


    public MultiPolygon computeMulti() {
        Polygon[] polygons = new Polygon[getPrecincts().size()];

        Iterator<Precinct> piter = getPrecincts().iterator();
        for(int ii = 0; ii < polygons.length; ii++) {
            Geometry poly = piter.next().getGeometry();
            if (poly instanceof Polygon)
                polygons[ii] = (Polygon) poly;
            else
                polygons[ii] = (Polygon) poly.convexHull();
        }
        MultiPolygon mp = new MultiPolygon(polygons,new GeometryFactory());
        this.multiPolygon = mp;
        this.multiPolygonUpdated = true;
        return mp;
    }

    public MultiPolygon getMulti() {
        if (this.multiPolygonUpdated && this.multiPolygon != null)
            return this.multiPolygon;
        return computeMulti();
    }

    public Geometry getConvexHull() {
        if (convexHullUpdated && convexHull !=null)
            return convexHull;
        convexHull = multiPolygon.convexHull();
        this.convexHullUpdated = true;
        return convexHull;
    }

    public Geometry getBoundingCircle() {
        if (boundingCircleUpdated && boundingCircle !=null)
            return boundingCircle;
        boundingCircle = new MinimumBoundingCircle(getMulti()).getCircle();
        this.boundingCircleUpdated = true;
        return boundingCircle;
    }
}
