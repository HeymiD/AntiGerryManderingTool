package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.Precinct;

import javax.persistence.*;
import java.util.HashMap;

//@Entity
//@Table(name="texas_demographics_formatted")
public class DemographicGroup {
//    @Column(name="Demographic")
//    @Enumerated
    private DEMOGRAPHIC groupDemographic;
//    @Column(name="population")
    private int population;
//    @ManyToOne
    private Precinct precinct;
//    @Id
//    @Column(name = "PCTKEY")
    private String pctkey;

    public DemographicGroup(){}

    public Precinct getPrecinct() {
        return precinct;
    }

    public void setPrecinct(Precinct precinct) {
        this.precinct = precinct;
    }

    public DEMOGRAPHIC getGroupDemographic() {
        return groupDemographic;
    }

    public void setGroupDemographic(DEMOGRAPHIC groupDemographic) {
        this.groupDemographic = groupDemographic;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
