// TODO: you must use TextIO in your solution to read from the data files

import textio.TextIO;

public class RainfallAnalyser {
    public static void main(String[] args) {
        // TODO: add your solution code here
//        System.out.println("please enter the path to the file: ");
//        String filename = TextIO.getln();
        String inFile = "resources/MountSheridanStationCNS.csv";
        TextIO.readFile(inFile);
        TextIO.getln(); // remove header record

//        create outFile
        String fileName = inFile.substring(inFile.lastIndexOf("/") + 1, inFile.indexOf("."));
        String outFile = "resources/" + fileName + "_analysed.csv";
        TextIO.writeFile(outFile);

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
                System.out.println("Error: Data line too short, Discarding!");
                continue;
            }
            if (lineSplit[2].length() != 4) { // invalid year
                System.out.println("Error: Invalid Year, Discarding data line!");
                continue;
            }
            if (lineSplit[3].equals("") || lineSplit[4].equals("")) { // missing date values
                System.out.println("Error: Invalid Month/Day, Discarding the data line!");
                continue;
            }

//            replace empty rainfall amount readings with 0.0
            if (lineSplit[5].equals("")) {
                lineSplit[5] = String.valueOf(0.0);
            }

            int year = Integer.parseInt(lineSplit[2]);
            int month = Integer.parseInt(lineSplit[3]);
            double rainfallMeasurement = Double.parseDouble(lineSplit[5]);

            if (readingsProcessed == 0) {
                previousMonth = month;
                monthlyRainfallMin = rainfallMeasurement;
                monthlyRainfallMax = rainfallMeasurement;
            }
//            TODO: Save month details to file

//           check if in same month
            if (month == previousMonth) {
                monthlyRainfallSum += rainfallMeasurement;
                if (rainfallMeasurement > monthlyRainfallMax) {
                    monthlyRainfallMax = rainfallMeasurement;
                } else if (rainfallMeasurement < monthlyRainfallMin) {
                    monthlyRainfallMin = rainfallMeasurement;
                }
            } else {
//                adjust year if in new year
                int yearToPrint;
                if (previousMonth == 12) {
                    yearToPrint = year - 1;
                } else {
                    yearToPrint = year;
                }
//                System.out.printf("%d\\%d - Total: %1.2f, Max: %1.2f, Min:%1.2f\n", previousMonth, yearToPrint, monthlyRainfallSum, monthlyRainfallMax, monthlyRainfallMin);

//                update variables for new month
                monthlyRainfallSum = rainfallMeasurement;
                monthlyRainfallMin = rainfallMeasurement;
                monthlyRainfallMax = rainfallMeasurement;
                previousMonth = month;
            }

            if (TextIO.eof()) { // if data ends halfway through month print
//                System.out.printf("%d\\%d - Total: %1.2f, Max: %1.2f, Min:%1.2f\n", month, year, monthlyRainfallSum, monthlyRainfallMax, monthlyRainfallMin);
            }
            readingsProcessed++;
        }
    }
// TODO: consider using static methods
}
