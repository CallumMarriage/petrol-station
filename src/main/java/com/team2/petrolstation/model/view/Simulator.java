package com.team2.petrolstation.model.view;

/**
 * @author callummarriage
 */
public interface Simulator {

    void simulate(Integer turns, Integer pumps, Integer tills,Double price, Boolean truckIsActive);

    String getResults();

    void setP(Double p);

    void setQ(Double q);
}
