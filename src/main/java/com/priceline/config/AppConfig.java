package com.priceline.config;

import com.priceline.domain.model.BillInventory;
import com.priceline.domain.model.Denomination;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${initial.inventory.count}")
    private String initialInventoryCount;

    @Bean
    public List<Integer> denominationValues() {

        return Arrays.asList(20, 10, 5, 2, 1);
    }

    @Bean
    public Map<Integer, Denomination> denominations(List<Integer> denominationList) {

        return denominationList.stream().collect(
            Collectors.toMap(i -> i,
                i ->
                    new Denomination("US DOLLAR","USD",
                        "$", i)));
    }

    @Bean
    public Map<Integer, BillInventory> billsInventory(Map<Integer, Denomination> denominations) {

        return denominations.entrySet().stream().collect(
            Collectors.toMap(e -> e.getKey(),
                             e -> new BillInventory(e.getValue(), new Integer(initialInventoryCount))));
    }

    public static void resetInventory() {

    }
}
