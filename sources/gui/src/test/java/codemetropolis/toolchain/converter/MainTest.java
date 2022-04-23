package codemetropolis.toolchain.converter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setErr(originalErr);
    }

    @Test
    public void convertJsonTest() {
        JSONObject object = new JSONObject();
        String[] expected = new String[]{"-h", "false", "-t", "test", "-s", "sourcePath", "-o", "outputPath", "-p", "apple", "banana", "orange"};
        object.put("help", false);
        object.put("type", "test");
        object.put("source", "sourcePath");
        object.put("output", "outputPath");
        JSONArray jsonArray = new JSONArray(new String[]{"apple", "banana", "orange"});
        object.put("params", jsonArray);
        assertArrayEquals(expected, Main.convertJsonToArgs(object));
    }

    @Test
    public void wrongFileTypeTest() {
        String[] result = Main.processFileInput(new String[]{"C:/Users/sooky/test.txt"});
        assertEquals("The parameter file should be a valid json file!\r\n", errContent.toString());
    }

    @Test
    public void processFileInputTest() {
        String expectedResult = "-h false -t test -s sourcePath -o outputPath -p apple banana orange";
        String[] result = Main.processFileInput(new String[]{"C:/Users/sooky/test.json"});
        assertArrayEquals(expectedResult.split(" "), result);
    }

    @Test
    public void noFileFoundTest() {
        String filePath = "C:/Users/sooky/notExist/test.txt";
        assertEquals(String.format("Cannot find resource file %s", filePath) + "\r\n", errContent.toString());
    }

}