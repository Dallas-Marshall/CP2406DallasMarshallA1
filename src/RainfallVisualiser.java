import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import textio.TextIO;
import javafx.scene.control.Label;

/**
 * This file can be used to draw a chart that effectively represents rainfall data.  Just fill in
 * the definition of drawPicture with the code that draws your picture.
 */
public class RainfallVisualiser extends Application {

    /**
     * Draws a picture.  The parameters width and height give the size
     * of the drawing area, in pixels.
     */
    public void drawPicture(GraphicsContext g, int width, int height) {

        g.setFill(Color.WHITE);
        g.fillRect(0, 0, width, height);

        int INDEX_OF_YEAR = 0;
        int INDEX_OF_MONTH = 1;
        int INDEX_OF_RAINFALL_TOTAL = 2;
        int X_AXIS_HEIGHT = height - 40;
        int startingValueX = 125;

//        create axis
        g.strokeLine(startingValueX, X_AXIS_HEIGHT, width - 25, X_AXIS_HEIGHT);
        g.strokeLine(startingValueX, X_AXIS_HEIGHT, startingValueX, 25);
        g.strokeText("Year", (width / 2.0), height - 10, 25);
        g.strokeText("Rainfall (mL)", 10, (height / 2.0), 60);
        g.strokeText("Monthly Rainfall Totals per Year", (width / 2.0 - 30),  20, 125);

        TextIO.getln(); // remove header record

        int currentValueX = startingValueX; // start graphing inside axis
        int yearsGraphed = 0;
        while (!TextIO.eof()) {
            String line = TextIO.getln();
            String[] lineSplit = line.split(",", -1);

            int year = Integer.parseInt(lineSplit[INDEX_OF_YEAR]);
            int month = Integer.parseInt(lineSplit[INDEX_OF_MONTH]);
            double monthlyRainfallSum = Double.parseDouble(lineSplit[INDEX_OF_RAINFALL_TOTAL]);
            monthlyRainfallSum = monthlyRainfallSum / 4; // scale down to fit larger totals

//            alternate bar colours per year
            if (month == 1) {
                yearsGraphed++;
                if (yearsGraphed % 2 == 0) {
                    g.setFill(Color.BLACK);
                } else {
                    g.setFill(Color.RED);
                }
            }
            g.fillRect(currentValueX, X_AXIS_HEIGHT - monthlyRainfallSum, 4, monthlyRainfallSum);
            currentValueX += 5; // move x coordinate across for each new bar
        }
    } // end drawPicture()


    //------ Implementation details: DO NOT EDIT THIS ------
    public void start(Stage stage) {
        int width = 218 * 6 + 40;
        int height = 500;
        Canvas canvas = new Canvas(width, height);
        drawPicture(canvas.getGraphicsContext2D(), width, height);
        BorderPane root = new BorderPane(canvas);
        root.setStyle("-fx-border-width: 4px; -fx-border-color: #444");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Rainfall Visualiser");
        stage.show();
        stage.setResizable(false);
    }
    //------ End of Implementation details ------


    public static void main(String[] args) {
//        System.out.print("Enter path: ");
//        var path = TextIO.getln();

        var path = "resources/MountSheridanStationCNS_analysed.csv";
        TextIO.readFile(path);
        launch();
    }

} // end SimpleGraphicsStarter
