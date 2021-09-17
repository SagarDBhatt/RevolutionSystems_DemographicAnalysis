package net.revolutionsystems.demographic_analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DemographicAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemographicAnalysisApplication.class, args);
    }

}
