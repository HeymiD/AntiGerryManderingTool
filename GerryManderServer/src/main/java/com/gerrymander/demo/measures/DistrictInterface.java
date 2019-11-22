package com.gerrymander.demo.measures;

import java.util.Set;

public interface DistrictInterface<Precinct extends PrecinctInterface> {
    String getID();
    Set<Precinct> getPrecincts();
    void removePrecinct(Precinct p);
    void addPrecinct(Precinct p);
    Set<Precinct> getBorderPrecincts();
    Precinct getPrecinct(String precinctID);
    default int getPopulation() {
        return 0;
    }
}
