package com.team2.petrolStation.model.constant;

/**
 * Constants for vehicles
 *
 * @author callummarriage
 */
public class VehicleConstants {
    //chances of the vehicles going to shop
    public static final Double CHANCE_OF_SMALL_CAR_GOING_TO_SHOP = 0.3;
    public static final Double CHANCE_OF_FAMILY_SEDAN_GOING_TO_SHOP = 0.4;
    public static final Double CHANCE_OF_TRUCK_GOING_TO_SHOP = 1.0;


    //The size of all customers
    public static final Double SIZE_OF_FAMILY_SEDAN = 1.5;
    public static final Double SIZE_OF_SMALL_CAR = 1.0;
    public static final Double SIZE_OF_MOTORBIKE = 0.75;
    public static final Double SIZE_OF_TRUCK = 2.0;
    public static final Double SIZE_OF_DRIVER = 1.0;

    //Max queue times for vehicles
    public static final Integer MAX_QUEUE_TIME_TRUCK = 48;
    public static final Integer MAX_QUEUE_TIME_FAMILY_SEDAN = 60;
    public static final Integer MAX_QUEUE_TIME_SMALL_CAR = 30;

    /*public static final Integer MAX_QUEUE_TIME_TRUCK = 48;
    public static final Integer MAX_QUEUE_TIME_FAMILY_SEDAN = 600;
    public static final Integer MAX_QUEUE_TIME_SMALL_CAR = 300;*/
}
