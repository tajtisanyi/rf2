package codemetropolis.toolchain.mapping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class checkVTest {

    @Test
    void checkRightArgs() {
        String[] args = {"java", "-jar", "mapping-1.4.0.jar", "-i" ,"converterToMapping.xml",
                "-m", "sourcemeter_mapping_example_2_0.xml", "-v" ,"true"};
        assertTrue(Main.check_v_switch(args));
    }

    @Test
    void checkWithNoValidate() {
        String[] args = {"java", "-jar", "mapping-1.4.0.jar", "-i" ,"converterToMapping.xml",
                "-m", "sourcemeter_mapping_example_2_0.xml", "-v"};
        assertFalse(Main.check_v_switch(args));
    }

    @Test
    void checkWrongArgs() {
        String[] args = {"java", "-jar", "mapping-1.4.0.jar", "-i" ,"converterToMapping.xml",
                "-m", "sourcemeter_mapping_example_2_0.xml", "-v" ,"-i"};
        assertFalse(Main.check_v_switch(args));
    }

    @Test
    void checkWithNoArgs() {
        String[] args = new String[0];
        assertFalse(Main.check_v_switch(args));
    }

}