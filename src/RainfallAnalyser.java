//1.	RainfallAnalyser: (use static Java methods only)
//2.	Read BOM rainfall data for 10+ years from csv text files (Mt. Sheridan Station Cairns)
//3.	Calculate monthly rainfall totals along with minimum daily and maximum daily rainfall during each month, across the 10+ years’ worth of data
//4.	Save the calculated information into a new text file

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RainfallAnalyser {
    public static List<String> OUTPUT = new ArrayList<>();
    public static List<Double> RAINFALL_TOTALS = new ArrayList<>();
    public static List<Double> RAINFALL_MIN = new ArrayList<>();
    public static List<Double> RAINFALL_MAX = new ArrayList<>();


    public static void main(String[] args) {
        String dbPath = "MountSheridanStationCNS.csv";
        readCSV(dbPath); //Read the file, and unload data into OUTPUT list variable
        calculateRainStats(); //Calculate the monthly rainfall statistics
        for(int i = 0; i < RAINFALL_TOTALS.size(); i++){
            double totalRain = RAINFALL_TOTALS.get(i);
            double minRain = RAINFALL_MIN.get(i);
            double maxRain = RAINFALL_MAX.get(i);

            System.out.printf("Month - %s, Total - %.2f, Min - %.2f, Max - %.2f\n", i+1, totalRain, minRain, maxRain);
        }
//        writeCSV(); //Write the new calculated data into new CSV file

    }

    public static void readCSV(String dbPath){
        try {
            File file = new File(dbPath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while((line = br.readLine()) != null) {
                OUTPUT.add(line);
            }
            br.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void calculateRainStats() {
        int currentMonth = 0;
        double currentMonthRain = 0;
        double rainfallAmount;
        double minRainfall = 0;
        double maxRainfall = 0;
        int rainfallMonth;


        for(int i=1; i < OUTPUT.size(); i++){
            String[] tempLine = (OUTPUT.get(i)).split(",");
            try{
                rainfallAmount = Double.parseDouble(tempLine[5]);
            } catch (Exception e) {
                rainfallAmount = 0; //If there is an error, means there was no rain on that day
            }
            rainfallMonth = Integer.parseInt(tempLine[3]);

            if(currentMonth != rainfallMonth){
                if(currentMonth != 0){ //If it is a new month, add current month data, and reset temp variables
                    UpdateConstants(currentMonthRain, minRainfall, maxRainfall);
                    currentMonthRain = 0;
                    minRainfall = 0;
                    maxRainfall = 0;
                }
                currentMonth = rainfallMonth;
            }

            currentMonthRain += rainfallAmount;

            if(rainfallAmount > 0){ //If there was any rain, see if it is the new min or max for the month
                if(minRainfall == 0){ //Set the new minimum
                    minRainfall = rainfallAmount;
                }
                if(rainfallAmount < minRainfall ){ //Is the rain the new min?
                    minRainfall = rainfallAmount;
                }
                if(rainfallAmount > maxRainfall){ //Is the rain the new max?
                    maxRainfall = rainfallAmount;
                }
            }
        }
        UpdateConstants(currentMonthRain, minRainfall, maxRainfall);
    }

    private static void UpdateConstants(double currentMonthRain, double minRainfall, double maxRainfall){
        RAINFALL_TOTALS.add(currentMonthRain);
        RAINFALL_MIN.add(minRainfall);
        RAINFALL_MAX.add(maxRainfall);
    }

    private static void calculateMaxRain() {
    }

    private static void writeCSV() {
    }


}
