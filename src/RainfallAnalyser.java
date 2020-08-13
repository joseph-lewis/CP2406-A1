//1.	RainfallAnalyser: (use static Java methods only)
//2.	Read BOM rainfall data for 10+ years from csv text files (Mt. Sheridan Station Cairns)
//3.	Calculate monthly rainfall totals along with minimum daily and maximum daily rainfall during each month, across the 10+ years’ worth of data
//4.	Save the calculated information into a new text file

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RainfallAnalyser {
    public static List<String> OUTPUT = new ArrayList<>();
    public static List<Double> RAINFALL_TOTALS = new ArrayList<>();
    public static List<Double> RAINFALL_MIN = new ArrayList<>();
    public static List<Double> RAINFALL_MAX = new ArrayList<>();
    public static String DB_NAME = "MountSheridanStationCNS";



    public static void main(String[] args) {
        System.out.println("Rainfall Analyser -");
        try {
            readCSV(DB_NAME + ".csv"); //Read the file, and unload data into OUTPUT list variable
            calculateRainStats(); //Calculate the monthly rainfall statistics
            writeCSV(); //Write the new calculated data into new CSV file
        } catch (IOException error){
            System.out.printf("An error has occurred -\n%s", error);
        }
    }

    public static void readCSV(String dbPath) throws IOException {
        File file = new File(dbPath);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        while((line = bufferedReader.readLine()) != null) {
            OUTPUT.add(line);
        }
        bufferedReader.close();
        System.out.printf("Successfully read %s!\n", dbPath);
    }


    private static void calculateRainStats() {
        int currentMonth = 0;
        double currentMonthRain = 0;
        double rainfallAmount;
        double minRainfall = 0;
        double maxRainfall = 0;
        int rainfallMonth;

        for (int i = 1; i < OUTPUT.size(); i++) {
            String[] tempLine = (OUTPUT.get(i)).split(",");
            try {
                rainfallAmount = Double.parseDouble(tempLine[5]);
            } catch (ArrayIndexOutOfBoundsException error) { //If no rain that month (Empty value), assign value 0
                rainfallAmount = 0;
            }
            rainfallMonth = Integer.parseInt(tempLine[3]);

            if (currentMonth != rainfallMonth) {
                if (currentMonth != 0) { //If it is a new month, add current month data, and reset temp variables
                    UpdateConstants(currentMonthRain, minRainfall, maxRainfall);
                    currentMonthRain = 0;
                    minRainfall = 0;
                    maxRainfall = 0;
                }
                currentMonth = rainfallMonth;
            }

            currentMonthRain += rainfallAmount;

            if (rainfallAmount > 0) { //If there was any rain, see if it is the new min or max for the month
                if (minRainfall == 0) { //Set the new minimum
                    minRainfall = rainfallAmount;
                }
                if (rainfallAmount < minRainfall) { //Is the rain the new min?
                    minRainfall = rainfallAmount;
                }
                if (rainfallAmount > maxRainfall) { //Is the rain the new max?
                    maxRainfall = rainfallAmount;
                }
            }
        }
        UpdateConstants(currentMonthRain, minRainfall, maxRainfall);
        System.out.println("Successfully calculated Rain fall Stats!");
    }

    private static void UpdateConstants(double currentMonthRain, double minRainfall, double maxRainfall){
        RAINFALL_TOTALS.add(currentMonthRain);
        RAINFALL_MIN.add(minRainfall);
        RAINFALL_MAX.add(maxRainfall);
    }

    private static void writeCSV() {
        String resultsCSV = DB_NAME + " - Calculations";

        try (PrintWriter writer = new PrintWriter(new File(resultsCSV + ".csv"))) {
            String csvHeaders = "Month,Total Rainfall,Min Rain,Max Rain\n"; //Write headers for the text document to identify calculations
            writer.write(csvHeaders);

            for(int i = 0; i < RAINFALL_TOTALS.size(); i++){ //Write each line of calculations to a CSV file
                int monthNum = i + 1;
                //The same index correlates to the same data in the calculation arrays
                double totalRain = RAINFALL_TOTALS.get(i);
                double minRain = RAINFALL_MIN.get(i);
                double maxRain = RAINFALL_MAX.get(i);

                String monthData = String.format("%s,%s,%s,%s\n", monthNum, totalRain, minRain, maxRain);
                writer.write(monthData);
                writer.flush();
            }
            writer.close(); //Close file to save changes
            System.out.printf("Successfully wrote to %s.csv!", resultsCSV);
        }
        catch (FileNotFoundException error) {
            System.out.printf("ERROR WRITING TO FILE -\n%s%n", error.getMessage());
        }
    }
}
