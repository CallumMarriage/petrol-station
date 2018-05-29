package java.com.team2.model.facility;

import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Truck;
import com.team2.petrolStation.model.facility.Facility;
import com.team2.petrolStation.model.facility.Shop;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by callummarriage on 25/05/2018.
 */
public class ShopTest {

    @Test
    public void testManageTransactionsForShop(){
        Facility shop = new Shop(1);
        Truck truck = new Truck();
        shop.addCustomerToServiceMachine(0, truck);

        Map<Integer, Driver> driverWithPump = shop.manageTransactions();

        assertEquals(0 , driverWithPump.keySet().toArray()[0]);

    }

    @Test
    public void testAddCustomerToTill(){

        Facility facility = new Shop(1);
        for(int i = 0; i < 5; i++){
            assertTrue(facility.addCustomerToServiceMachine(i, new Driver()));
        }

    }
}
