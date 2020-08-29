// TODO: you must use TextIO in your solution to read from the data files

import textio.TextIO;

public class RainfallAnalyser {

    static final int INDEX_OF_YEAR = 2;
    static final int INDEX_OF_MONTH = 3;
    static final int INDEX_OF_DAY = 4;
    static final int INDEX_OF_RAINFALL_MEASUREMENT = 5;

    public static void main(String[] args) {

        System.out.print("Enter path name: ");
        String pathToInFile = TextIO.getln();

        readInFile(pathToInFile);
        initialiseOutFile(pathToInFile);

        double monthlyRainfallTotal = 0;
        double monthlyRainfallMin = 0;
        double monthlyRainfallMax = 0;
        int readingsProcessed = 0;
        int previousMonth = 1;
        while (!TextIO.eof()) {
            String line = TextIO.getln();
            String[] lineSplit = line.split(",", -1);

            if (!isValidYear(lineSplit)) {
                System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Year)");
                continue;
            } else if (!isValidMonth(lineSplit)) {
                System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Month)");
                continue;
            } else if (!isValidDay(lineSplit)) {
                System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Day)");
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
                int previousYear;
                if (previousMonth == 12) {
                    previousYear = year - 1; // adjust year if in new year
                } else {
                    previousYear = year;
                }
                printToFile(previousYear, previousMonth, monthlyRainfallTotal, monthlyRainfallMin, monthlyRainfallMax);

                // reset variables for new month
                monthlyRainfallTotal = rainfallMeasurement;
                monthlyRainfallMin = rainfallMeasurement;
                monthlyRainfallMax = rainfallMeasurement;
                previousMonth = month;
            }

            if (TextIO.eof()) { // print last month of data
                printToFile(year, previousMonth, monthlyRainfallTotal, monthlyRainfallMin, monthlyRainfallMax);
            }
            readingsProcessed++;
        }
    } // end main

    static void initialiseOutFile(String nameOfInFile) {
        String fileName = nameOfInFile.substring(nameOfInFile.lastIndexOf("/") + 1, nameOfInFile.lastIndexOf("."));
        String pathToOutFile = "resources/" + fileName + "_analysed.csv";
        TextIO.writeFile(pathToOutFile);
        TextIO.putln("year,month,total,min,max");
    }

    static void readInFile(String inFile) {
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
    }

    static void printToFile(int year, int month, double rainfallTotal, double rainfallMin, double rainfallMax) {
        TextIO.putf("%d,%d,%1.2f,%1.2f,%1.2f\n", year, month, rainfallTotal, rainfallMin, rainfallMax);
    }

    static boolean isValidYear(String[] lineSplit) {
        boolean isValidYear = true;
        if (lineSplit.length < 3) {
            isValidYear = false;
        } else if (lineSplit[INDEX_OF_YEAR].length() != 4) {
            isValidYear = false;
        }
        return isValidYear;
    } // end isValidYear

    static boolean isValidMonth(String[] lineSplit) {
        boolean isValidMonth = true;
        if (lineSplit.length < 4) {
            isValidMonth = false;
        } else if (Integer.parseInt(lineSplit[INDEX_OF_MONTH]) <= 0) {
            isValidMonth = false;
        }
        return isValidMonth;
    } // end IsValidMonth

    static boolean isValidDay(String[] lineSplit) {
        boolean isValidMonth = true;
        if (lineSplit.length < 5) {
            isValidMonth = false;
        } else if (Integer.parseInt(lineSplit[INDEX_OF_DAY]) <= 0) {
            isValidMonth = false;
        }
        return isValidMonth;
    } // end isValidDay

} // end class RainfallAnalyser
