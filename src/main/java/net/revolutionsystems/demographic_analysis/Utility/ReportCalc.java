package net.revolutionsystems.demographic_analysis.Utility;

import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ReportCalc {

    String oppLocation, oppCounty;
    double oppPopulation,wstGenerationTPD, wstTPD, processingRevenue, materialRevenue, pickupCharge,
            truckingExpenseUnbaled, householdCost, processingCost, netRecycling, annualRecycling;
    final double wstGenerationRate = 3.6;
    final double wstDiversionRate = 0.3;
    final double prcPricePerTon = 30;
    final double targetYield = 0.85;
    final double mtrlPricePerTon = 157.03;
    final double peoplePerHouse = 2.4;
    final double pickupChargePerHH = 1.50;
    final double processingCostPerTon = 53.58;

    final double truckOperatingCost = 5.32;
    final double truckingCostUnbaled = 15;
    final double truckingCostBaled = 21;

    public void wasteGeneration(double cityPopulation, String cityName)
    {
        wstGenerationTPD = cityPopulation * (wstGenerationRate/2000) * wstDiversionRate;

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        wstTPD = Double.parseDouble(df.format(wstGenerationTPD));

        System.out.println("Waste Generated = " + wstTPD + " for city = " + cityName);
    }

    public void processingPricePerTon(String CityName)
    {
        processingRevenue = Double.parseDouble(new DecimalFormat("#.##").format(wstGenerationTPD * prcPricePerTon));

        //JOptionPane.showMessageDialog(null, "Processing Revenue of " + CityName + " is $" + processingRevenue);
    }

    public void materialRevenue(String CityName)
    {
        materialRevenue = Double.parseDouble(new DecimalFormat("#.##").format(wstGenerationTPD * targetYield * mtrlPricePerTon));
        //JOptionPane.showMessageDialog(null, "Material Revenue generated in" + CityName + " is $" + materialRevenue);
    }

    public void pickupCharge(double cityPopulation, String cityName)
    {
        pickupCharge = Double.parseDouble(new DecimalFormat("#.##").format((cityPopulation/peoplePerHouse) * (pickupChargePerHH)/30));
        //JOptionPane.showMessageDialog(null, "Pickup charge per House hold in " + cityName + " $" + pickupCharge + " daily");
    }

    public void truckingExpense(double distanceBetCities)
    {
        truckingExpenseUnbaled = Double.parseDouble(new DecimalFormat("#.##").format((2*distanceBetCities*truckOperatingCost)/truckingCostUnbaled*wstGenerationTPD));
        //JOptionPane.showMessageDialog(null, "Trucking expense to transport unbaled material is $" + truckingExpenseUnbaled);
    }

    public void householdCost(double cityPopulation)
    {
        householdCost = Double.parseDouble(new DecimalFormat("#.##").format((processingRevenue + truckingExpenseUnbaled)/((cityPopulation/peoplePerHouse))*30));
        //JOptionPane.showMessageDialog(null, "House hold cost $" + householdCost);
    }

    public void procCost()
    {
        processingCost = Double.parseDouble(new DecimalFormat("#.##").format((wstGenerationTPD * processingCostPerTon)));
        //JOptionPane.showMessageDialog(null, "Processing Cost $" + processingCost);
    }

    public void netRecycling()
    {
        netRecycling = Double.parseDouble(new DecimalFormat("#.##").format(processingRevenue + materialRevenue + pickupCharge - processingCost));
        //JOptionPane.showMessageDialog(null, "Net value of Recycling $" + netRecycling);

        annualRecycling = Double.parseDouble(new DecimalFormat("#.##").format(netRecycling * 365));
        //JOptionPane.showMessageDialog(null, "Annualized Recycling revenue $" + annualRecycling);
    }

//    public void writeToFile(double cityPopulation, String cityName, String State, boolean FC,String Counties, double lati, double longi) throws IOException, IOException {
//        String fileContent = "";
//        String fileCont = "";
//
//        if(FC == true)
//        {
//            //fileContent = "Opportunity Survey of ::::  " + cityName + "\n\n";
//            fileContent += 	"City\t County\t State\t Population\t Recyclable Material (TPD)\t Processing Revenue\t Material Revenue\t Pickup Revenue\t Distance\t Truck expense\t House hold cost\t Processing Cost\t Net Recycling\t Annualized\t Latitude\t Longitude " + "\n\n";
//            //filename = "OppSurveyfor__"+ cityName + ".txt";
//        }
//
//        fileContent += cityName + "\t"+ Counties +  "\t" + State + "\t" + cityPopulation + "\t" + wstTPD +"\t" + processingRevenue +"\t" + materialRevenue +"\t" + pickupCharge +"\t" + oppCityNameObject.distanceBetCities + "\t"+ truckingExpenseUnbaled +"\t"+ householdCost +"\t" + processingCost +"\t" + netRecycling +"\t" + annualRecycling + "\t" + lati + "\t" + longi + "\n";
//
//        FileWriter fw = new FileWriter("OpportunityReport.txt",true);
//        fw.write(fileContent);
//        fw.close();
//    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        /* System.out.println("xxxx"); */

        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }
    }
}
