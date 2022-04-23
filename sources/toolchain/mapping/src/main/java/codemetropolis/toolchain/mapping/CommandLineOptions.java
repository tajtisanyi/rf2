package codemetropolis.toolchain.mapping;

import codemetropolis.toolchain.commons.util.Resources;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

public class CommandLineOptions {

    @Option(name = "-h", aliases = {"--help"})
    private boolean showHelp = false;

    @Option(name = "-i", aliases = {"--input"})
    private String inputFile = null;

    @Option(name = "-o", aliases = {"--output"})
    private String outputFile = "mappingToPlacing.xml";

    @Option(name = "-m", aliases = {"--mapping"})
    private String mappingFile = null;

    @Option(name = "-s", aliases = {"--scale"}, handler = ScaleOptionHandler.class)
    private double scale = 1.0;

    @Option(name = "-v", aliases = {"--validate"})
    private boolean hierarchyValidation = false;

    public boolean showHelp() {
        return showHelp;
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getMappingFile() {
        return mappingFile;
    }

    public double getScale() {
        return scale;
    }

    public boolean isHierarchyValidationEnabled() {
        return hierarchyValidation;
    }

    /**
     * Option handler class for Scale
     * This class handles the cases when scale options has no value and when the value is incorrect,
     * if there is no parsing error, then its set the option to the supplied value
     */
    public static class ScaleOptionHandler extends OptionHandler<Double> {

        public ScaleOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Double> setter) {
            super(parser, option, setter);
        }

        /**
         * This method checks whether the supplied value is present and check if its parsable as double.
         * @param params The rest of the arguments. This method can use this object to access the arguments of the option if necessary.
         *               The object is valid only during the method call.
         * @return The number of arguments consumed. (For example, returns 0 if this option doesn't take any parameters.)
         * @throws CmdLineException
         */
        @Override
        public int parseArguments(Parameters params) throws CmdLineException {
            if (params.size() == 0) {
                String message = Resources.get("scale_option_without_value");
                throw new CmdLineException(owner, message, null);
            }
            try {
                Double scale = Double.parseDouble(params.getParameter(0));
                setter.addValue(scale);
                return 1;
            } catch (NumberFormatException e) {
                //TODO: error handling for parse errors
                throw new CmdLineException(owner, "", null);
            }
        }

        @Override
        public String getDefaultMetaVariable() {
            return null;
        }
    }
}
