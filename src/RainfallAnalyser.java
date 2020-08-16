// TODO: you must use TextIO in your solution to read from the data files
import textio.TextIO;
import java.util.ArrayList;

public class RainfallAnalyser {
    public static void main(String[] args) {
        // TODO: add your solution code here
        TextIO.readFile("resources/MountSheridanStationCNS.csv");

//        array to store arrays of each line of data split
        ArrayList<String[]> dataLines = new ArrayList<>();


        while (!TextIO.eof()) {
            String line = TextIO.getln();
            String[] lineSplit = line.split(",", -1);
            dataLines.add(lineSplit);
        }

//        TESTING ONLY - PRINT OUT ARRAY CONTENTS!!!!!!!!!!!
        for(String[] line : dataLines){
            System.out.print(" [");
//          iterate through line array and print elements
            for (String element : line) {
                System.out.print(element + ", ");
            }
            System.out.println("]");
        }
    }

    // TODO: consider using static methods
}
