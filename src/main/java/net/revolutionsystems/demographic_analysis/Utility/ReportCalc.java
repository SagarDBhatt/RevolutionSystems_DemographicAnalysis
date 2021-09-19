package net.revolutionsystems.demographic_analysis.Utility;

import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ReportCalc {

    String oppLocation, oppCounty;
    double oppPopulation;
    static double wstGenerationTPD;
    static double wstTPD;
    static double processingRevenue;
    static double materialRevenue;
    static double pickupCharge;
    static double truckingExpenseUnbaled;
    static double householdCost;
    static double processingCost;
    static double netRecycling;
    static double annualRecycling;
    static final double wstGenerationRate = 3.6;
    static final double wstDiversionRate = 0.3;
    static final double prcPricePerTon = 30;
    static final double targetYield = 0.85;
    static final double mtrlPricePerTon = 157.03;
    static final double peoplePerHouse = 2.4;
    static final double pickupChargePerHH = 1.50;
    static final double processingCostPerTon = 53.58;

    static final double truckOperatingCost = 5.32;
    static final double truckingCostUnbaled = 15;
    final double truckingCostBaled = 21;

    public static void wasteGeneration(double cityPopulation)
    {
        wstGenerationTPD = cityPopulation * (wstGenerationRate/2000) * wstDiversionRate;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        wstTPD = Double.parseDouble(df.format(wstGenerationTPD));
        //System.out.println("Waste Generated = " + wstTPD);
    }

    public static void processingPricePerTon()
    {
        processingRevenue = Double.parseDouble(new DecimalFormat("#.##").format(wstGenerationTPD * prcPricePerTon));
        //System.out.println("Processing Revenue of " + CityName + " is $" + processingRevenue);
    }

    public static void materialRevenue()
    {
        materialRevenue = Double.parseDouble(new DecimalFormat("#.##").format(wstGenerationTPD * targetYield * mtrlPricePerTon));
        //System.out.println("Material Revenue generated in" + CityName + " is $" + materialRevenue);
    }

    public static void pickupCharge(double cityPopulation)
    {
        pickupCharge = Double.parseDouble(new DecimalFormat("#.##").format((cityPopulation/peoplePerHouse) * (pickupChargePerHH)/30));
        //System.out.println("Pickup charge per House hold in " + cityName + " $" + pickupCharge + " daily");
    }

    public static void truckingExpense(double distanceBetCities)
    {
        truckingExpenseUnbaled = Double.parseDouble(new DecimalFormat("#.##").format((2*distanceBetCities*truckOperatingCost)/truckingCostUnbaled*wstGenerationTPD));
        //JOptionPane.showMessageDialog(null, "Trucking expense to transport unbaled material is $" + truckingExpenseUnbaled);
    }

    public static void householdCost(double cityPopulation)
    {
        householdCost = Double.parseDouble(new DecimalFormat("#.##").format((processingRevenue + truckingExpenseUnbaled)/((cityPopulation/peoplePerHouse))*30));
        //JOptionPane.showMessageDialog(null, "House hold cost $" + householdCost);
    }

    public static void procCost()
    {
        processingCost = Double.parseDouble(new DecimalFormat("#.##").format((wstGenerationTPD * processingCostPerTon)));
        //JOptionPane.showMessageDialog(null, "Processing Cost $" + processingCost);
    }

    public static void netRecycling()
    {
        netRecycling = Double.parseDouble(new DecimalFormat("#.##").format(processingRevenue + materialRevenue + pickupCharge - processingCost));
        //JOptionPane.showMessageDialog(null, "Net value of Recycling $" + netRecycling);

        annualRecycling = Double.parseDouble(new DecimalFormat("#.##").format(netRecycling * 365));
        //JOptionPane.showMessageDialog(null, "Annualized Recycling revenue $" + annualRecycling);
    }

    public static void writeToFile(double cityPopulation, String cityName, String State, boolean column_Headers,String Counties, double lati, double longi, Double distBetCities) throws IOException, IOException {
        String fileContent = "";
        String fileCont = "";

        if(column_Headers == true)
        {
            fileContent += 	"City\t County\t State\t Population\t Recyclable Material (TPD)\t Processing Revenue\t Material Revenue\t Pickup Revenue\t Distance\t Truck expense\t House hold cost\t Processing Cost\t Net Recycling\t Annualized\t Latitude\t Longitude " + "\n\n";
        }

        fileContent += cityName + "\t"+ Counties +  "\t" + State + "\t" + cityPopulation + "\t" + wstTPD +"\t" + processingRevenue +"\t" + materialRevenue +"\t" + pickupCharge +"\t" + distBetCities + "\t"+ truckingExpenseUnbaled +"\t"+ householdCost +"\t" + processingCost +"\t" + netRecycling +"\t" + annualRecycling + "\t" + lati + "\t" + longi + "\n";

        FileWriter fw = new FileWriter("OpportunityReport.txt",true);
        fw.write(fileContent);
        fw.close();
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {

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
