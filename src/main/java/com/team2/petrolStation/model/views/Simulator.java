package com.team2.petrolStation.model.views;

/**
 * @author callummarriage
 */
public interface Simulator {

    void simulate(Integer turns, Integer pumps, Integer tills,Double price, Double p, Double q, Boolean truckIsActive);
}
