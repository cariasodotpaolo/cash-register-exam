package test.priceline;

import com.priceline.api.shell.CashRegisterShellController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;

@SpringBootApplication(exclude = {SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class,
                                  StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class})
@ComponentScan(basePackages = {"com.priceline.config", "com.priceline.domain"}, excludeFilters={
    @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=CashRegisterShellController.class)})
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
