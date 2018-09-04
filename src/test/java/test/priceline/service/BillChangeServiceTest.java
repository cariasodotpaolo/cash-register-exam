package test.priceline.service;

import static org.junit.Assert.assertEquals;

import com.priceline.domain.exception.InsufficentFundsException;
import com.priceline.domain.exception.NoExactChangeException;
import com.priceline.domain.model.BillExchange;
import com.priceline.domain.service.BillChangeService;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import test.priceline.TestApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class BillChangeServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BillChangeService billChangeService;
    @Autowired
    private BillInventoryService billInventoryService;
    @Autowired
    private DenominationService denominationService;

    @Before
    public void initSetup() {
        billInventoryService.resetInventory();
    }

    @Test
    public void testGetChange() throws NoExactChangeException, InsufficentFundsException {

        int changeAmount = 87;
        List<BillExchange> changeBills;

        changeBills = billChangeService.getChange(changeAmount);
        Integer totalAmount = changeBills.stream().mapToInt(c -> c.getTotalAmount()).sum();

        assertEquals(changeAmount, totalAmount.intValue());
    }

    @Test(expected = InsufficentFundsException.class)
    public void Should_Throw_InsufficientFundsException() throws NoExactChangeException, InsufficentFundsException {

        List<Integer> denominations = denominationService.getValues();
        List<BillExchange> removedBills = new ArrayList<>();

        for(Integer value : denominations) {
            removedBills.add(new BillExchange(denominationService.get(value), 10));
        }

        billInventoryService.dispense(removedBills);

        billChangeService.getChange(98);
    }

    @Test(expected = NoExactChangeException.class)
    public void Should_Throw_NoExactChangeException() throws NoExactChangeException, InsufficentFundsException {

        List<Integer> denominations = denominationService.getValues();
        List<BillExchange> removedBills = new ArrayList<>();

        removedBills.add(new BillExchange(denominationService.get(new Integer(1)), 10));

        billInventoryService.dispense(removedBills);

        billChangeService.getChange(91);
    }
}
