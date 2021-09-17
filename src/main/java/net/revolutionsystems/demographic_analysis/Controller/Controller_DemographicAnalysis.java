package net.revolutionsystems.demographic_analysis.Controller;

import net.revolutionsystems.demographic_analysis.Repository.Repo_Population;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller_DemographicAnalysis {

    private Repo_Population repo_population;

    public Controller_DemographicAnalysis(Repo_Population repo_population) {
        super();
        this.repo_population = repo_population;
    }

    @GetMapping(value = "/")
    public String helloWorld(){
        return "Hello World's population";
    }

}
