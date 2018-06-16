package com.team2.petrolstation.model.constant;

/**
 * General constant for Application class
 *
 * @author callummarriage
 */
public class PetrolStationConstants {

	private PetrolStationConstants() {
	    throw new IllegalStateException("Utility class");
	}
	
    public static final Integer SECONDS_PER_TICK = 10;
    public static final String RESULTS_DESTINATION_FILE = "target/results";
    public static final Integer SLEEP_TIME = 60;
    public static final String START = "\n--------------------------\n          Results\n--------------------------\n";
    public static final String MOTORBIKE_ARRIVED = "A motorbike has arrived";
    public static final String SMALL_CAR_ARRIVED = "A small car has arrived";
    public static final String TRUCK_ARRIVED = "A truck has arrived";
    public static final String FAMILY_SEDAN = "A family sedan has arrived";
    public static final String OUTPUT_FILE = "target/output-";
}
