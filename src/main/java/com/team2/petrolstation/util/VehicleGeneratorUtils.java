package com.team2.petrolstation.util;

import com.team2.petrolstation.model.customer.Vehicle;

import java.util.Random;

import static com.team2.petrolstation.model.constant.VehicleConstants.*;

/**
 * Created by callummarriage on 11/01/2019.
 */
public class VehicleGeneratorUtils {

    public static Vehicle generateMotorbike(){
        return new Vehicle(
                0,
                0,
                5,
                0.0,
                SIZE_OF_MOTORBIKE,
                0);
    }

    public static Vehicle generateSmallCar(Random random){
        return new Vehicle(
                random.nextInt(24 - 12 + 1) + 12,
                random.nextInt(10 -5 + 1) + 5,
                random.nextInt(9 - 7  + 1) + 7,
                CHANCE_OF_SMALL_CAR_GOING_TO_SHOP,
                SIZE_OF_SMALL_CAR,
                MAX_QUEUE_TIME_SMALL_CAR);
    }

    public static Vehicle generateTruck(Random random){
        int i = (36 -24 +1);
        Vehicle vehicle = new Vehicle(
                random.nextInt(36 -24 +1  ) +24,
                random.nextInt(20 - 15 + 1) + 15,
                random.nextInt(40 - 30 + 1) + 30,
                CHANCE_OF_TRUCK_GOING_TO_SHOP,
                SIZE_OF_TRUCK,
                MAX_QUEUE_TIME_TRUCK);

        //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
        //tell that its a truck
        vehicle.setIsTruck();
        return vehicle;
    }

    public static Vehicle generateFamilySedan(Random random){
        return new Vehicle(
                random.nextInt(30 - 12 + 1) + 12,
                random.nextInt(16 - 8 + 1) + 8,
                random.nextInt(18) + 12,
                CHANCE_OF_FAMILY_SEDAN_GOING_TO_SHOP,
                SIZE_OF_FAMILY_SEDAN,
                MAX_QUEUE_TIME_FAMILY_SEDAN);
    }
}
