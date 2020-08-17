// TODO: you must use TextIO in your solution to read from the data files

import textio.TextIO;

public class RainfallAnalyser {
    public static void main(String[] args) {
        // TODO: add your solution code here
//        System.out.println("please enter the path to the file: ");
//        String filename = TextIO.getln();
        String filename = "resources/MountSheridanStationCNS.csv";
        TextIO.readFile(filename);
        TextIO.getln(); // remove header record

        int lineCount = 0;
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

//            System.out.println(year);
//            System.out.println(month);
//            System.out.println(day);
//            System.out.println(rainfallMeasurement);
//            System.out.println();

            lineCount++;
        }
    }

    // TODO: consider using static methods
}
