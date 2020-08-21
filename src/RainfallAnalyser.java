// TODO: you must use TextIO in your solution to read from the data files

import textio.TextIO;

public class RainfallAnalyser {
    public static void main(String[] args) {
        // TODO: add your solution code here
        System.out.print("Enter path name: ");
        String inFile = TextIO.getln();

        try {
            TextIO.readFile(inFile);
        } catch (IllegalArgumentException e) { // file does not exist
            System.out.println("ERROR: An error occurred during runtime, failed to process file.");
        }

        try { // remove header record
            TextIO.getln();
        } catch (IllegalArgumentException e) {  // file is empty
            System.out.println("ERROR: An error occurred during runtime, file is empty.");
        }

//        create outFile
        String fileName = inFile.substring(inFile.lastIndexOf("/") + 1, inFile.lastIndexOf("."));
        String outFile = "resources/" + fileName + "_analysed.csv";
        TextIO.writeFile(outFile);
        TextIO.putln("year,month,total,min,max");

        double monthlyRainfallSum = 0;
        double monthlyRainfallMin = 0;
        double monthlyRainfallMax = 0;
        int readingsProcessed = 0;
        int previousMonth = 1;

        while (!TextIO.eof()) {
            String line = TextIO.getln();
            String[] lineSplit = line.split(",", -1);

//            check valid data lines
            if (lineSplit.length <= 5) { // data line missing necessary information
                System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Data Line)");
                continue;
            }
            if (lineSplit[2].length() != 4) { // invalid year
                System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Year)");
                continue;
            }
            if (lineSplit[3].equals("") || lineSplit[4].equals("")) { // missing date values
                System.out.println("ERROR: An error occurred during runtime, failed to process file. (Invalid Month/Day)");
                continue;
            }

//            replace blank rainfall readings with 0.0
            if (lineSplit[5].equals("")) {
                lineSplit[5] = String.valueOf(0.0);
            }

            int year = Integer.parseInt(lineSplit[2]);
            int month = Integer.parseInt(lineSplit[3]);
            double rainfallMeasurement = Double.parseDouble(lineSplit[5]);

            if (readingsProcessed == 0) { // set values on first iteration to current values
                previousMonth = month;
                monthlyRainfallMin = rainfallMeasurement;
                monthlyRainfallMax = rainfallMeasurement;
            }

//            calculate values
            if (month == previousMonth) {
                monthlyRainfallSum += rainfallMeasurement;

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
                TextIO.putf("%d,%d,%1.2f,%1.2f,%1.2f\n", previousMonth, yearToPrint, monthlyRainfallSum, monthlyRainfallMin, monthlyRainfallMax);

//                reset variables for new month
                monthlyRainfallSum = rainfallMeasurement;
                monthlyRainfallMin = rainfallMeasurement;
                monthlyRainfallMax = rainfallMeasurement;
                previousMonth = month;
            }

            if (TextIO.eof()) { // if data ends halfway through month print
                TextIO.putf("%d,%d,%1.2f,%1.2f,%1.2f\n", previousMonth, year, monthlyRainfallSum, monthlyRainfallMin, monthlyRainfallMax);
            }
            readingsProcessed++;
        }
    }
// TODO: consider using static methods
}
