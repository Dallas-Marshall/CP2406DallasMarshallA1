import org.junit.jupiter.api.*;
import textio.TextIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

class RainfallAnalyserTest {
    @BeforeEach
    void setup() {
        TestHelper.updateSystemOut();
    }

    @AfterEach
    void reset() {
        TextIO.readStandardInput();
        TextIO.writeStandardOutput();
        TestHelper.resetSystemIO();
    }

    @AfterAll
    static void teardown() {
        try {
            Files.deleteIfExists(Path.of("resources/rainy_analysed.csv"));
            Files.deleteIfExists(Path.of("resources/dry_analysed.csv"));
            Files.deleteIfExists(Path.of("resources/bad1_analysed.csv"));
            Files.deleteIfExists(Path.of("resources/bad2_analysed.csv"));
            Files.deleteIfExists(Path.of("resources/bad3_analysed.csv"));
        } catch (IOException e) {
            System.out.println("failed to delete temp files: " + e.getLocalizedMessage());
        }
    }

    @Test
    void invalidFilename() {
        TestHelper.updateSystemIn("resources/unknown.csv");

        RainfallAnalyser.main(null);

        var pattern = Pattern.compile("^\\s*Enter path name:\\s+ERROR:\\s*(?<message>(?s).+)$");
        var matcher = TestHelper.check(pattern);

        var message = matcher.group("message");
        Assertions.assertTrue(message.contains("failed to process file"), "incorrect exception triggered");
    }

    @Test
    void emptyFile() {
        TestHelper.updateSystemIn("resources/empty.csv");

        RainfallAnalyser.main(null);

        var pattern = Pattern.compile("^\\s*Enter path name:\\s+ERROR:\\s*(?<message>(?s).+)$");
        var matcher = TestHelper.check(pattern);

        var message = matcher.group("message");
        Assertions.assertTrue(message.contains("file is empty"), "incorrect exception triggered");
    }

    @Test
    void rainyMonth() {
        TestHelper.updateSystemIn("resources/rainy.csv");

        RainfallAnalyser.main(null);

        var pattern = Pattern.compile("^\\s*Enter path name:\\s*$");
        TestHelper.check(pattern);

        TextIO.readFile("resources/rainy_analysed.csv");

        var header = TextIO.getln();
        Assertions.assertEquals("year,month,total,min,max", header, "invalid header record");

        var line = TextIO.getln();
        var tokens = line.trim().split(",");
        Assertions.assertEquals(tokens.length, 5, "saved record corrupted");

        var total = Double.parseDouble(tokens[2]);
        var min = Double.parseDouble(tokens[3]);
        var max = Double.parseDouble(tokens[4]);

        Assertions.assertEquals(10, min, "invalid min value");
        Assertions.assertEquals(20, max, "invalid max value");
        Assertions.assertEquals(320, total, "invalid total value");
    }

    @Test
    void dryMonth() {
        TestHelper.updateSystemIn("resources/dry.csv");

        RainfallAnalyser.main(null);

        var pattern = Pattern.compile("^\\s*Enter path name:\\s*$");
        TestHelper.check(pattern);

        TextIO.readFile("resources/dry_analysed.csv");

        var header = TextIO.getln();
        Assertions.assertEquals("year,month,total,min,max", header, "invalid header record");

        var line = TextIO.getln();
        var tokens = line.trim().split(",");

        Assertions.assertEquals(tokens.length, 5, "saved record corrupted");
        var total = Double.parseDouble(tokens[2]);
        var min = Double.parseDouble(tokens[3]);
        var max = Double.parseDouble(tokens[4]);

        Assertions.assertEquals(0, min, "invalid min value");
        Assertions.assertEquals(0, max, "invalid max value");
        Assertions.assertEquals(0, total, "invalid total value");
    }

    @Test
    void badYear() {
        TestHelper.updateSystemIn("resources/bad1.csv");

        RainfallAnalyser.main(null);

        var pattern = Pattern.compile("^\\s*Enter path name:\\s+ERROR:\\s*(?<message>(?s).+)$");
        var matcher = TestHelper.check(pattern);

        var message = matcher.group("message");
        Assertions.assertTrue(message.contains("failed to process file"), "incorrect exception triggered");
    }

    @Test
    void badMonth() {
        TestHelper.updateSystemIn("resources/bad2.csv");

        RainfallAnalyser.main(null);

        var pattern = Pattern.compile("^\\s*Enter path name:\\s+ERROR:\\s*(?<message>(?s).+)$");
        var matcher = TestHelper.check(pattern);

        var message = matcher.group("message");
        Assertions.assertTrue(message.contains("failed to process file"), "incorrect exception triggered");
    }

    @Test
    void badDay() {
        TestHelper.updateSystemIn("resources/bad3.csv");

        RainfallAnalyser.main(null);

        var pattern = Pattern.compile("^\\s*Enter path name:\\s+ERROR:\\s*(?<message>(?s).+)$");
        var matcher = TestHelper.check(pattern);

        var message = matcher.group("message");
        Assertions.assertTrue(message.contains("failed to process file"), "incorrect exception triggered");
    }
}