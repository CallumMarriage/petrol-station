package java.com.team2.model.facility;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Motorbike;
import com.team2.petrolStation.model.customer.vehicle.SmallCar;
import com.team2.petrolStation.model.customer.vehicle.Truck;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
import com.team2.petrolStation.model.facility.Facility;
import com.team2.petrolStation.model.facility.FillingStation;
import com.team2.petrolStation.model.facility.Shop;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by callummarriage on 25/05/2018.
 */
public class FacilityTest {

    @Test
    public void testFindBestAvailableServiceMachine(){
        Customer truck = new Truck();
        Customer car = new SmallCar();
        Customer motorbike = new Motorbike();

        FillingStation facility = new FillingStation(3);

        assertEquals(0, facility.findBestServiceMachine());
        facility.addCustomerToServiceMachine(0, truck);
        assertEquals(1, facility.findBestServiceMachine());
        facility.addCustomerToServiceMachine(1, car);
        assertEquals(2, facility.findBestServiceMachine());
        facility.addCustomerToServiceMachine(2, motorbike);
        assertEquals(0, facility.findBestServiceMachine());
    }
}
