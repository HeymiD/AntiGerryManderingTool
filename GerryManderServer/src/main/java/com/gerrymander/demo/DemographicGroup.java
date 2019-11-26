package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.Precinct;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@Table(name="texas_demographics_formatted")
public class DemographicGroup {

    @Column(name="population")
    @MapKeyColumn(name = "Demographic")
    @MapKeyEnumerated(EnumType.STRING)
    private HashMap<DEMOGRAPHIC,Integer> demographicInfo;
    @ManyToOne
    private Precinct precinct;
    @Id
    @Column(name = "PCTKEY")
    private String pctkey;

    public DemographicGroup(){}

    public Precinct getPrecinct() {
        return precinct;
    }

    public void setPrecinct(Precinct precinct) {
        this.precinct = precinct;
    }

    public HashMap<DEMOGRAPHIC, Integer> getDemographicInfo() {
        return demographicInfo;
    }

    public void setDemographicInfo(HashMap<DEMOGRAPHIC, Integer> demographicInfo) {
        this.demographicInfo = demographicInfo;
    }


}
