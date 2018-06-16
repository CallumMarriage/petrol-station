package com.team2.petrolStation.model.view;

/**
 * @author callummarriage
 */
public interface Simulator {

    void simulate(Integer turns, Integer pumps, Integer tills,Double price, Double p, Double q, Boolean truckIsActive);
}
