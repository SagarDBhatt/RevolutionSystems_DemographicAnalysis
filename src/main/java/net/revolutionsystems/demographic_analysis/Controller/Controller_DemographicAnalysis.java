package net.revolutionsystems.demographic_analysis.Controller;

import net.revolutionsystems.demographic_analysis.Entity.Population;
import net.revolutionsystems.demographic_analysis.Repository.Repo_Population;
import net.revolutionsystems.demographic_analysis.Utility.ReportCalc;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    }//End of getAll()

    /**
     * Method to get the list of all the states
     * @return
     */
    @GetMapping("/getAllStates")
    public ResponseEntity<List<Map<String,Object>>> getAllStates(){
        List<Map<String,Object>> allStates = repo_population.findAllStates();
        ResponseEntity<List<Map<String,Object>>> responseEntity_allStates = new ResponseEntity<List<Map<String,Object>>>(allStates,HttpStatus.OK);
        return responseEntity_allStates;
    }//End of getAllStates()

    /**
     * Find all cities within state
     * @return
     */
    @GetMapping("/getAllCitiesWithinState")
    public ResponseEntity<List<Map<String,Object>>> getAllCitiesWithinState(@RequestParam(value = "state") String state) {
        List<Map<String,Object>> allCitiesWithinState = repo_population.findAllCitiesWithinState(state);
        ResponseEntity<List<Map<String,Object>>> responseEntity_allCitiesWIthinState = new ResponseEntity<>(allCitiesWithinState, HttpStatus.OK);
        return responseEntity_allCitiesWIthinState;
    }//End of getAllCitiesWithinState()

    /**
     * Get all the cities
     * @return
     */
    @GetMapping("/getAllCities")
    public ResponseEntity<List<Map<String,Object>>> getAllCities(){
        List<Map<String,Object>> list_getAllCities = repo_population.getAllCities();
        ResponseEntity<List<Map<String,Object>>> respEntity_getAllCities = new ResponseEntity<>(list_getAllCities, HttpStatus.OK);
        return respEntity_getAllCities;
    }// End of getAllCities()



    @GetMapping(value = "/getDemographicReport")
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

    @GetMapping(value = "/getCitiesWithinState/withinMiles")
    public ResponseEntity<Resource> get_All_Cities_Within_fiftyMiles(@RequestParam("city") String city, @RequestParam("state") String state, @RequestParam("distance") int distance_parameter) throws IOException {
        System.out.println("API called ============================");
        System.out.println("***********************************************");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet opportunityReportSheet = workbook.createSheet("Opportunity Report");
        XSSFRow row = opportunityReportSheet.createRow(0);

        row.createCell(0).setCellValue("City");
        row.createCell(1).setCellValue("County");
        row.createCell(2).setCellValue("State");
        row.createCell(3).setCellValue("Population");
        row.createCell(4).setCellValue("Recyclable Material (TPD)");
        row.createCell(5).setCellValue("Processing Revenue ($)");
        row.createCell(6).setCellValue("Material Revenue ($)");
        row.createCell(7).setCellValue("Pickup Revenue ($)");
        row.createCell(8).setCellValue("Distance");
        row.createCell(9).setCellValue("Truck Expense ($)");
        row.createCell(10).setCellValue("Household Cost ($)");
        row.createCell(11).setCellValue("Processing Cost ($)");
        row.createCell(12).setCellValue("Net Recycling ($)");
        row.createCell(13).setCellValue("Annualized Recycling ($)");
        row.createCell(14).setCellValue("Latitude");
        row.createCell(15).setCellValue("Longitude");

        List<Map<String,Object>> list_origin_city = repo_population.findByCity_NameAndState_Name(city,state);

//        System.out.println("List of original city = " + list_origin_city.stream()
//                .map(obj -> obj.get("City_Name"))
//                .collect(Collectors.toList())
//        );

        int row_count = 1;

        for(int i = 0; i < list_origin_city.size(); i++){
            row = opportunityReportSheet.createRow(row_count++);

            row.createCell(0).setCellValue(list_origin_city.stream()
                    .map(obj -> obj.get("City_Name").toString())
                    .collect(Collectors.toList())
                    .get(i));

            row.createCell(1).setCellValue(list_origin_city.stream()
                    .map(obj -> obj.get("County_Name").toString())
                    .collect(Collectors.toList())
                    .get(i));

            row.createCell(2).setCellValue(list_origin_city.stream()
                    .map(obj -> obj.get("State_Name").toString())
                    .collect(Collectors.toList())
                    .get(i));

            row.createCell(3).setCellValue(
                    Double.parseDouble(list_origin_city.stream()
                    .map(obj -> obj.get("Population").toString())
                    .collect(Collectors.toList())
                    .get(i)));

            row.createCell(4)
                    .setCellValue(
                            ReportCalc.wasteGeneration(
                                        Double.parseDouble(
                                            list_origin_city.stream()
                                                    .map(obj -> obj.get("Population").toString())
                                                    .collect(Collectors.toList())
                                                    .get(i)
                                        )
                            )
                    );

            row.createCell(5).setCellValue(
                    ReportCalc.processingPricePerTon()
            );

            row.createCell(6).setCellValue(ReportCalc.materialRevenue());

            row.createCell(7).setCellValue(ReportCalc.pickupCharge(
                    Double.parseDouble(
                            list_origin_city.stream().map(obj -> obj.get("Population").toString()).collect(Collectors.toList()).get(i)
                    )
            ));

            row.createCell(8).setCellValue(0);

            row.createCell(9).setCellValue(ReportCalc.truckingExpense(0));

            row.createCell(10).setCellValue(ReportCalc.householdCost(
                    Double.parseDouble(list_origin_city.stream().map(obj -> obj.get("Population").toString()).collect(Collectors.toList()).get(i))
            ));

            row.createCell(11).setCellValue(ReportCalc.procCost());

            row.createCell(12).setCellValue(ReportCalc.netRecycling());

            row.createCell(13).setCellValue(ReportCalc.annualizedRecycling());

            row.createCell(14).setCellValue(
                    Double.parseDouble(list_origin_city.stream().map(obj -> obj.get("Latitude").toString()).collect(Collectors.toList()).get(i)));

            row.createCell(15).setCellValue(
                    Double.parseDouble(list_origin_city.stream().map(obj -> obj.get("Longitude").toString()).collect(Collectors.toList()).get(i)));

        }//END OF FOR LOOP

//        List<Map<String,Object>> list_other_cities = repo_population.findAllCities();
        List<Map<String,Object>> list_other_cities = repo_population.findAllWithinState(state);
        Double original_City_latitude = Double.parseDouble(list_origin_city.stream().map(obj -> obj.get("Latitude").toString()).collect(Collectors.toList()).get(0));
        Double original_City_longitude = Double.parseDouble(list_origin_city.stream().map(obj -> obj.get("Longitude").toString()).collect(Collectors.toList()).get(0));

        for(int i = 0; i < list_other_cities.size(); i++){

            Double other_City_latitude = Double.parseDouble(list_other_cities.stream().map(obj -> obj.get("Latitude").toString()).collect(Collectors.toList()).get(i));
            Double other_City_longitude = Double.parseDouble(list_other_cities.stream().map(obj -> obj.get("Longitude").toString()).collect(Collectors.toList()).get(i));

            Double distance_bet_cities = ReportCalc.distance(original_City_latitude, original_City_longitude, other_City_latitude, other_City_longitude);

            if(distance_bet_cities <= distance_parameter){

                row = opportunityReportSheet.createRow(row_count++);

                row.createCell(0).setCellValue(list_other_cities.stream()
                        .map(obj -> obj.get("City_Name").toString())
                        .collect(Collectors.toList())
                        .get(i));

                row.createCell(1).setCellValue(list_other_cities.stream()
                        .map(obj -> obj.get("County_Name").toString())
                        .collect(Collectors.toList())
                        .get(i));

                row.createCell(2).setCellValue(list_other_cities.stream()
                        .map(obj -> obj.get("State_Name").toString())
                        .collect(Collectors.toList())
                        .get(i));

                row.createCell(3).setCellValue(
                        Double.parseDouble(list_other_cities.stream()
                                .map(obj -> obj.get("Population").toString())
                                .collect(Collectors.toList())
                                .get(i)));

                row.createCell(4)
                        .setCellValue(
                                ReportCalc.wasteGeneration(
                                        Double.parseDouble(
                                                list_other_cities.stream()
                                                        .map(obj -> obj.get("Population").toString())
                                                        .collect(Collectors.toList())
                                                        .get(i)
                                        )
                                )
                        );

                row.createCell(5).setCellValue(
                        ReportCalc.processingPricePerTon()
                );

                row.createCell(6).setCellValue(ReportCalc.materialRevenue());

                row.createCell(7).setCellValue(ReportCalc.pickupCharge(
                        Double.parseDouble(
                                list_other_cities.stream().map(obj -> obj.get("Population").toString()).collect(Collectors.toList()).get(i)
                        )
                ));

                row.createCell(8).setCellValue(distance_bet_cities);

                row.createCell(9).setCellValue(ReportCalc.truckingExpense(distance_bet_cities));

                row.createCell(10).setCellValue(ReportCalc.householdCost(
                        Double.parseDouble(list_other_cities.stream().map(obj -> obj.get("Population").toString()).collect(Collectors.toList()).get(i))
                ));

                row.createCell(11).setCellValue(ReportCalc.procCost());

                row.createCell(12).setCellValue(ReportCalc.netRecycling());

                row.createCell(13).setCellValue(ReportCalc.annualizedRecycling());

                row.createCell(14).setCellValue(
                        Double.parseDouble(list_other_cities.stream().map(obj -> obj.get("Latitude").toString()).collect(Collectors.toList()).get(i)));

                row.createCell(15).setCellValue(
                        Double.parseDouble(list_other_cities.stream().map(obj -> obj.get("Longitude").toString()).collect(Collectors.toList()).get(i)));

            }//END OF IF STATEMENT

        }//END OF FOR LOOP Other Cities


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=opportunityReport.xlsx");

        return new ResponseEntity<>(new ByteArrayResource(outputStream.toByteArray()), headers, HttpStatus.CREATED);


//        JSONArray jsonArray_origin_city = JSONArray.fromObject(list_origin_city);
//        JSONObject jsonObject_origin_city = jsonArray_origin_city.getJSONObject(0);
//
//        ReportCalc.wasteGeneration(jsonObject_origin_city.getDouble("Population"));
//        ReportCalc.processingPricePerTon();
//        ReportCalc.materialRevenue();
//        ReportCalc.pickupCharge(jsonObject_origin_city.getDouble("Population"));
//        ReportCalc.truckingExpense(0);
//        ReportCalc.householdCost(jsonObject_origin_city.getDouble("Population"));
//        ReportCalc.procCost();
//        ReportCalc.netRecycling();
//
//        ReportCalc.writeToFile(jsonObject_origin_city.getDouble("Population"),
//                jsonObject_origin_city.getString("City_Name"),
//                jsonObject_origin_city.getString("State_Name"),
//                true,
//                jsonObject_origin_city.getString("County_Name"),
//                jsonObject_origin_city.getDouble("Latitude"),
//                jsonObject_origin_city.getDouble("Longitude"),
//                0D
//        );
//
//        List<Map<String,Object>> all_cities = repo_population.findAllCities();
//        ResponseEntity<List<Map<String,Object>>> responseEntity_all_cities = new ResponseEntity<>(all_cities, HttpStatus.OK);
//        JSONArray jsonArray_all_cities  = JSONArray.fromObject(all_cities);
//
//        for(int i=0;i<jsonArray_all_cities.size();i++) {
//            JSONObject jsonObject_all_cities = jsonArray_all_cities.getJSONObject(i);
//
//            ReportCalc.wasteGeneration(jsonObject_all_cities.getDouble("Population"));
//            ReportCalc.processingPricePerTon();
//            ReportCalc.materialRevenue();
//            ReportCalc.pickupCharge(jsonObject_all_cities.getDouble("Population"));
//
//            Double distance_bet_cities = ReportCalc.distance(jsonObject_origin_city.getDouble("Latitude"), jsonObject_origin_city.getDouble("Longitude"),
//                    jsonObject_all_cities.getDouble("Latitude"), jsonObject_all_cities.getDouble("Longitude"));
//
//            ReportCalc.truckingExpense(distance_bet_cities);
//            ReportCalc.householdCost(jsonObject_origin_city.getDouble("Population"));
//            ReportCalc.procCost();
//            ReportCalc.netRecycling();
//
//            if(distance_bet_cities <= distance){
//                ReportCalc.writeToFile(jsonObject_all_cities.getDouble("Population"),
//                        jsonObject_all_cities.getString("City_Name"),
//                        jsonObject_all_cities.getString("State_Name"),
//                        false,
//                        jsonObject_all_cities.getString("County_Name"),
//                        jsonObject_all_cities.getDouble("Latitude"),
//                        jsonObject_all_cities.getDouble("Longitude"),
//                        distance_bet_cities
//                );
//            }
//        }

    }//End of get_All_Cities_Within_fiftyMiles()

}//End of class Controller_DemographicAnalysis
