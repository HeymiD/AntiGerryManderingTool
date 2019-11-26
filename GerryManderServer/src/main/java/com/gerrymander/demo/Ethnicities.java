package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.Precinct;

import javax.persistence.*;

@Entity
@Table(name="texas_demographics")
public class Ethnicities {
    @ManyToOne
    private Precinct precinct;
    @Column(name="White")
    private int white;
    @Column(name="Black")
    private int black;
    @Column(name="Hispanic")
    private int hispanic;
    @Column(name="Native")
    private int native_american;
    @Column(name="Pacific")
    private int pacific;
    @Column(name="Asian")
    private int asian;
    @Column(name="Other")
    private int other;

    public Ethnicities(){ }


    public int getWhite() {
        return white;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    public int getBlack() {
        return black;
    }

    public void setBlack(int black) {
        this.black = black;
    }

    public int getHispanic() {
        return hispanic;
    }

    public void setHispanic(int hispanic) {
        this.hispanic = hispanic;
    }

    public int getNative_american() {
        return native_american;
    }

    public void setNative_american(int native_american) {
        this.native_american = native_american;
    }

    public int getPacific() {
        return pacific;
    }

    public void setPacific(int pacific) {
        this.pacific = pacific;
    }

    public int getAsian() {
        return asian;
    }

    public void setAsian(int asian) {
        this.asian = asian;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

}
