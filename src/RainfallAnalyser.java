// TODO: you must use TextIO in your solution to read from the data files

import textio.TextIO;

public class RainfallAnalyser {

    static final int INDEX_OF_YEAR = 2;
    static final int INDEX_OF_MONTH = 3;
    static final int INDEX_OF_DAY = 4;
    static final int INDEX_OF_RAINFALL_MEASUREMENT = 5;

    public static void main(String[] args) {

        System.out.print("Enter path name: ");
        String inFile = TextIO.getln();

        try {
            TextIO.readFile(inFile);
        } catch (IllegalArgumentException e) { // file does not exist
            System.out.println("ERROR: An error occurred during runtime, failed to process file.");
        }

        try { // ignore header record
            TextIO.getln();
        } catch (IllegalArgumentException e) {  // file is empty
            System.out.println("ERROR: An error occurred during runtime, file is empty.");
        }

        // calculate outFile name and write header data
        String fileName = inFile.substring(inFile.lastIndexOf("/") + 1, inFile.lastIndexOf("."));
        String outFile = "resources/" + fileName + "_analysed.csv";
        TextIO.writeFile(outFile);
        TextIO.putln("year,month,total,min,max");

        double monthlyRainfallTotal = 0;
        double monthlyRainfallMin = 0;
        double monthlyRainfallMax = 0;
        int readingsProcessed = 0;
        int previousMonth = 1;

        while (!TextIO.eof()) {
            String line = TextIO.getln();
            String[] lineSplit = line.split(",", -1);

            if (!isValidDataLine(lineSplit)) {
                continue;
            }

            // replace blank rainfall readings with 0.0
            if (lineSplit[INDEX_OF_RAINFALL_MEASUREMENT].equals("")) {
                lineSplit[INDEX_OF_RAINFALL_MEASUREMENT] = String.valueOf(0.0);
            }

            int year = Integer.parseInt(lineSplit[INDEX_OF_YEAR]);
            int month = Integer.parseInt(lineSplit[INDEX_OF_MONTH]);
            double rainfallMeasurement = Double.parseDouble(lineSplit[INDEX_OF_RAINFALL_MEASUREMENT]);

            if (readingsProcessed == 0) { // set values on first iteration to current values
                previousMonth = month;
                monthlyRainfallMin = rainfallMeasurement;
                monthlyRainfallMax = rainfallMeasurement;
            }

            // calculate values
            if (month == previousMonth) {
                monthlyRainfallTotal += rainfallMeasurement;

                if (rainfallMeasurement > monthlyRainfallMax) {
                    monthlyRainfallMax = rainfallMeasurement;

                } else if (rainfallMeasurement < monthlyRainfallMin) {
                    monthlyRainfallMin = rainfallMeasurement;
                }
            } else {
                int yearToPrint;
                if (previousMonth == 12) {
                    yearToPrint = year - 1; // adjust year if in new year
                } else {
                    yearToPrint = year;
                }
                TextIO.putf("%d,%d,%1.2f,%1.2f,%1.2f\n", yearToPrint, previousMonth, monthlyRainfallTotal, monthlyRainfallMin, monthlyRainfallMax);

                // reset variables for new month
                monthlyRainfallTotal = rainfallMeasurement;
                monthlyRainfallMin = rainfallMeasurement;
                monthlyRainfallMax = rainfallMeasurement;
                previousMonth = month;
            }

            if (TextIO.eof()) { // if data ends halfway through month print
                TextIO.putf("%d,%d,%1.2f,%1.2f,%1.2f\n", year, previousMonth, monthlyRainfallTotal, monthlyRainfallMin, monthlyRainfallMax);
            }
            readingsProcessed++;
        }
    } // end main

    static boolean isValidDataLine(String[] lineSplit) {
        boolean isValidLine = true;
        if (lineSplit.length <= 5) { // data line missing necessary information
            System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Data Line)");
            isValidLine = false;
        }
        if (lineSplit[INDEX_OF_YEAR].length() != 4) { // invalid year
            System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Year)");
            isValidLine = false;
        }
        if (lineSplit[INDEX_OF_MONTH].equals("") || lineSplit[INDEX_OF_DAY].equals("")) { // missing date values
            System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Month/Day)");
            isValidLine = false;
        }
        return isValidLine;
    } // end isValidDataLine

} // end class RainfallAnalyser
