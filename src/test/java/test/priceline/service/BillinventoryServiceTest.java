package test.priceline.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.priceline.domain.exception.InsufficentFundsException;
import com.priceline.domain.model.BillExchange;
import com.priceline.domain.model.BillInventory;
import com.priceline.domain.service.BillInventoryService;
import com.priceline.domain.service.DenominationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import test.priceline.TestApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class BillinventoryServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BillInventoryService billInventoryService;
    @Autowired
    private DenominationService denominationService;
    @Value("${initial.inventory.count}")
    private String initialInventoryCount;

    @Before
    public void initSetup() {
        billInventoryService.resetInventory();
    }

    @Test
    public void testShowAllInventory() {

        List<BillInventory> inventory = billInventoryService.getAll();

        List<Integer> values = denominationService.getValues();

        for (BillInventory bill : inventory) {

            System.out.println(String.format("Denomination: %s | Count: %s",
                                             bill.getDenomination().getValue(),
                                             bill.getCount()));

            assertThat(values, hasItem(bill.getDenomination().getValue()));
            assertThat(bill.getCount(), is(greaterThan(0)));
        }

        assertEquals(inventory.size(), values.size());

    }

    @Test
    public void testReplenishBillsInventory() {

        int addCount = 10;

        List<Integer> denominations = denominationService.getValues();
        List<BillExchange> newBills = new ArrayList<>();

        for(Integer value : denominations) {
            newBills.add(new BillExchange(denominationService.get(value), addCount));
        }

        billInventoryService.replenish(newBills);

        List<BillInventory> inventory = billInventoryService.getAll();

        for(BillInventory bill : inventory ) {

            assertThat(bill.getCount(), is(equalTo(Integer.parseInt(initialInventoryCount) + addCount)));

        }
    }

    @Test
    public void testDispenseBillsInventory() throws InsufficentFundsException {

        int dispenseCount = 5;

        List<Integer> denominations = denominationService.getValues();
        List<BillExchange> dispensedBills = new ArrayList<>();

        for(Integer value : denominations) {
            dispensedBills.add(new BillExchange(denominationService.get(value), dispenseCount));
        }

        billInventoryService.dispense(dispensedBills);

        List<BillInventory> inventory = billInventoryService.getAll();

        for(BillInventory bill : inventory ) {

            assertThat(bill.getCount(), is(equalTo(Integer.parseInt(initialInventoryCount) - dispenseCount)));

        }
    }

    @Test(expected = InsufficentFundsException.class)
    public void testDispenseBills_ThrowsInsufficientFunds() throws InsufficentFundsException {

        int dispenseCount = 15;

        List<Integer> denominations = denominationService.getValues();
        List<BillExchange> dispensedBills = new ArrayList<>();

        for(Integer value : denominations) {
            dispensedBills.add(new BillExchange(denominationService.get(value), dispenseCount));
        }

        billInventoryService.dispense(dispensedBills);
    }
}
