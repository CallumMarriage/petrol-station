package com.team2.petrolStation.model.customer.vehicle;

import com.team2.petrolStation.model.customer.Customer;

/**
 * Vehicle allows all vehicles to return their size, and their fuel size when being managed by a pump.
 *
 * @author callummarriage
 */
public interface Vehicle extends Customer {

    Integer getTimeInQueue();

    void setTimeInQueue(Integer time);

    void setMaxFuel(Integer maxFuel);

    Integer getMaxFuel();

    Integer getShopTime();

    void setShopTime(Integer shopTime);

    Integer getShopPurchase();

    void setShopPurchase(Integer shopPurchase);

    int getMaxQueueTime();

    void setMaxQueueTimes(int queueTime);
}
