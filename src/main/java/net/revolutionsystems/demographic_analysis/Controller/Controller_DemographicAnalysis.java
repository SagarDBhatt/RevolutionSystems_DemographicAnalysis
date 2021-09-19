package net.revolutionsystems.demographic_analysis.Controller;

import com.sun.corba.se.impl.orbutil.RepIdDelegator;
import net.revolutionsystems.demographic_analysis.Entity.Population;
import net.revolutionsystems.demographic_analysis.Repository.Repo_Population;
import net.revolutionsystems.demographic_analysis.Utility.ReportCalc;
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
import java.io.IOException;
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
    public String get_All_Cities_Within_State(@RequestParam("city") String city, @RequestParam("state") String state) throws IOException {
        List<Map<String,Object>> list_origin_city = repo_population.findByCity_NameAndState_Name(city,state);
        JSONArray jsonArray_origin_city = JSONArray.fromObject(list_origin_city);
        JSONObject jsonObject_origin_city = jsonArray_origin_city.getJSONObject(0);

        ReportCalc.wasteGeneration(jsonObject_origin_city.getDouble("Population"));
        ReportCalc.processingPricePerTon();
        ReportCalc.materialRevenue();
        ReportCalc.pickupCharge(jsonObject_origin_city.getDouble("Population"));
        ReportCalc.truckingExpense(0);
        ReportCalc.householdCost(jsonObject_origin_city.getDouble("Population"));
        ReportCalc.procCost();
        ReportCalc.netRecycling();

        ReportCalc.writeToFile(jsonObject_origin_city.getDouble("Population"),
                jsonObject_origin_city.getString("City_Name"),
                jsonObject_origin_city.getString("State_Name"),
                true,
                jsonObject_origin_city.getString("County_Name"),
                jsonObject_origin_city.getDouble("Latitude"),
                jsonObject_origin_city.getDouble("Longitude"),
                0D
                );

        List<Map<String,Object>> cities_within_state = repo_population.findByState(state);
        ResponseEntity<List<Map<String,Object>>> responseEntity_cities_within_state = new ResponseEntity<>(cities_within_state, HttpStatus.OK);
        JSONArray jsonArray  = JSONArray.fromObject(cities_within_state);

        for(int i=0;i<jsonArray.size();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            ReportCalc.wasteGeneration(jsonObject.getDouble("Population"));
            ReportCalc.processingPricePerTon();
            ReportCalc.materialRevenue();
            ReportCalc.pickupCharge(jsonObject.getDouble("Population"));

            Double distance_bet_cities = ReportCalc.distance(jsonObject_origin_city.getDouble("Latitude"), jsonObject_origin_city.getDouble("Longitude"),
                    jsonObject.getDouble("Latitude"), jsonObject.getDouble("Longitude"));

            ReportCalc.truckingExpense(distance_bet_cities);
            ReportCalc.householdCost(jsonObject_origin_city.getDouble("Population"));
            ReportCalc.procCost();
            ReportCalc.netRecycling();

            ReportCalc.writeToFile(jsonObject.getDouble("Population"),
                    jsonObject.getString("City_Name"),
                    jsonObject.getString("State_Name"),
                    false,
                    jsonObject.getString("County_Name"),
                    jsonObject.getDouble("Latitude"),
                    jsonObject.getDouble("Longitude"),
                    distance_bet_cities
            );

        }


        return "Report generated successfully!!! ";
    }//End of get_All_Cities_Within_State()

}//End of class Controller_DemographicAnalysis
