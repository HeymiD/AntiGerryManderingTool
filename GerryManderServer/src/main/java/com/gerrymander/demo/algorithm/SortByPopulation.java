package com.gerrymander.demo.algorithm;

import com.gerrymander.demo.Cluster;

import java.util.Comparator;

public class SortByPopulation implements Comparator<Cluster>
{
    public int compare(Cluster c1, Cluster c2)
    {
        return c1.population - c2.population;
    }
}