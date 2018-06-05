package com.team2.petrolStation.model.customer.vehicle;

import com.team2.petrolStation.model.customer.Customer;

import java.util.Random;

/**
 * Vehicle allows all vehicles to return their size, and their fuel size when being managed by a pump.
 *
 * @author callummarriage
 */
public interface Vehicle extends Customer {

    Integer getTimeInQueue();

    Integer getMaxFuel();

    Integer getShopTime();

    Integer getShopPurchase();

    int getMaxQueueTime();

    Double getChanceOfGoingToShop();

    Boolean decide(Random random);
}
