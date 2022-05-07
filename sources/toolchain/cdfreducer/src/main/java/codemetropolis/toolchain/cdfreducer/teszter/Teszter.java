package codemetropolis.toolchain.cdfreducer.teszter;

import codemetropolis.toolchain.cdfreducer.CdfReducerExecutorArgs;
import codemetropolis.toolchain.cdfreducer.CdfReducerExecutorTeszt;
import codemetropolis.toolchain.cdfreducer.CommandLineOptions;
import codemetropolis.toolchain.commons.util.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Teszter {

    public void doTeszt(CommandLineOptions options) {

        CdfReducerExecutorTeszt executor = new CdfReducerExecutorTeszt();
        executor.setPrefix(Resources.get("cdfreducer_prefix"));
        executor.setErrorPrefix(Resources.get("error_prefix"));

        List<Map<String, String>> paramList = new ArrayList<>();
        paramList.add(new HashMap<String, String>()
        {{
            put("property-name-regex", "source_id");
            put("property-value-regex","0");
        }});

        paramList.add(new HashMap<String, String>()
        {{
            put("property-name-regex", "source_id");
        }});

        paramList.add(new HashMap<String, String>()
        {{
            put("property-value-regex","[A-Z][0-9]{3}");
        }});

        paramList.add(new HashMap<String, String>()
        {{
            put("property-name-regex", "[A-Z]([a-z]+)[A-Z]([a-z]+)");
        }});

        //rossz kulcsnál nem redukál semmit -> elvárt működés
        paramList.add(new HashMap<String, String>()
        {{
            put("nem_letezo_kulcs", "[A-Z]([a-z]+)[A-Z]([a-z]+)");
        }});
        //kulcs nélkül nem redukál semmit -> elvárt működés
        paramList.add(new HashMap<String, String>()
        {{
        }});

        for (int i = 0; i < paramList.size(); i++) {
            boolean passed = executor.execute(
                    new CdfReducerExecutorArgs(
                            options.getSource(),
                            "toolchain/cdfreducer/tesztresults/"+(i+1) + "-xml_teszt.xml",
                            paramList.get(i)
                    ));
            System.out.println("Test " + (i+1) + ": ");
            System.out.println(paramList.get(i));
            if (passed) System.out.println("Passed!");
            else System.out.println("Failed");

        }

        System.out.println("See the results at: ./cdfreducer/tesztresults/");
    }

}
