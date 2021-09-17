package net.revolutionsystems.demographic_analysis.Controller;

//import net.revolutionsystems.demographic_analysis.Repository.Repo_Population;
import net.revolutionsystems.demographic_analysis.Entity.city_Population;
import net.revolutionsystems.demographic_analysis.Repository.Repo_Population;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller_DemographicAnalysis {

    @Autowired
    public Repo_Population repo_population;

    public void setRepo_population(Repo_Population repo_population) {
        this.repo_population = repo_population;
    }

    @GetMapping("/hw")
    public String helloWorld(){
        return "Hello World's population";
    }

    @GetMapping(value = "/population")
    public List<city_Population> getPopulation(){
        List<city_Population> populations = repo_population.findAll();
        return populations;
    }

}
