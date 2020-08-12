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
    public static List<Float> RAINFALL_TOTALS = new ArrayList<>();

    public static void main(String[] args) {
        String dbPath = "MountSheridanStationCNS.csv";
        readCSV(dbPath); //Read the file, and unload data into OUTPUT list variable
        calculateMonthlyRain(); //Calculate total rainfall per month
//        calculateMinRain(); //Calculate minimum daily rainfall per month
//        calculateMaxRain(); //Calculate maximum daily rainfall per month
//        writeCSV(); //Write the new calculated data into new CSV file
        System.out.println(RAINFALL_TOTALS);

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

    private static void calculateMonthlyRain() {
        int currentMonth = 0;
        float currentMonthRain = 0;
        double rainfallAmount;
        int rainfallMonth;

        for(int i=1; i < OUTPUT.size(); i++){
            String[] tempLine = (OUTPUT.get(i)).split(",");
            try{
                rainfallAmount = Double.parseDouble(tempLine[5]);
            } catch (Exception e) {
                rainfallAmount = 0;
            }
            rainfallMonth = Integer.parseInt(tempLine[3]);

            if(currentMonth != rainfallMonth){
                if(currentMonth != 0){
                    RAINFALL_TOTALS.add(currentMonthRain);
                    currentMonthRain = 0;
                }
                currentMonth = rainfallMonth;
            }

            currentMonthRain += rainfallAmount;

        }
    }

    private static void calculateMinRain() {
    }

    private static void calculateMaxRain() {
    }

    private static void writeCSV() {
    }


}
