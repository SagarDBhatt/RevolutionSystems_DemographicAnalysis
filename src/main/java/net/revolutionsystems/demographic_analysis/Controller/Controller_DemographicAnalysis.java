package net.revolutionsystems.demographic_analysis.Controller;

import net.revolutionsystems.demographic_analysis.Entity.Population;
import net.revolutionsystems.demographic_analysis.Repository.Repo_Population;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.management.ObjectName;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/opportunityReport")
public class Controller_DemographicAnalysis {

    @Autowired
    public Repo_Population repo_population;

    public void setRepo_population(Repo_Population repo_population) {
        this.repo_population = repo_population;
    }

    @GetMapping(value = "/hw")
    public String helloWorld(){
        return "Hello World's population";
    }

    @GetMapping(value = "/all")
    public  ResponseEntity<List<Map<String,Object>>> getAll(@RequestParam("city") String city, @RequestParam("state") String state){
        List<Population> all_pop = repo_population.findAll();
        List<Map<String,Object>> allCity = repo_population.findByCity_NameAndState_Name(city,state);
        ResponseEntity<List<Map<String,Object>>> resp_Entity = new ResponseEntity<>(allCity, HttpStatus.OK);

        return resp_Entity;
    }

    @GetMapping(value = "/getCitiesWithinState")
    public ResponseEntity<List<Map<String, Object>>> get_All_Cities_Within_State(@RequestParam("state") String state){
        List<Map<String,Object>> cities_within_state = repo_population.findByState(state);
        ResponseEntity<List<Map<String,Object>>> responseEntity_cities_within_state = new ResponseEntity<>(cities_within_state, HttpStatus.OK);

        JSONArray jsonArray  = JSONArray.fromObject(cities_within_state);

        for(int i=0;i<jsonArray.size();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            System.out.println("JSON Array State = " + jsonObject.getString("State_Name"));
            System.out.println("JSON Array State = " + jsonObject.getString("City_Name"));
        }

        return responseEntity_cities_within_state;
    }

}
