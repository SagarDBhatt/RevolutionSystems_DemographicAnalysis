package net.revolutionsystems.demographic_analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@ComponentScan(basePackages = {"net.revolutionsystems.demographic_analysis.*", "net.revolutionsystems.demographic_analysis.Repository"})
public class DemographicAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemographicAnalysisApplication.class, args);
    }

}
