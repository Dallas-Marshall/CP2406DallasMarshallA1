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
//                System.out.println("No rain measurement, Replaced with 0,0");
                lineSplit[5] = String.valueOf(0.0);
            }

            int year = Integer.parseInt(lineSplit[2]);
            int month = Integer.parseInt(lineSplit[3]);
            int day = Integer.parseInt(lineSplit[4]);
            double rainfallMeasurement = Double.parseDouble(lineSplit[5]);

//            check if in new month
            if (day != 1) {
                monthlyRainfallSum += rainfallMeasurement;
//                monthDayCount++;
            } else {
//                calculate min max
//                create identifying information for record
//                Save month details to file
                System.out.printf("%1.2f \n", monthlyRainfallSum);
                monthlyRainfallSum = rainfallMeasurement;
//                monthDayCount = 1;
            }
            if (TextIO.eof()) { // data ends halfway through month
                System.out.printf("%1.2f \n", monthlyRainfallSum);
            }
        }
    }

    // TODO: consider using static methods
}
