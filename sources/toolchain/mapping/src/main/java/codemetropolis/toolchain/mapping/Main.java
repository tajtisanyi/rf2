package codemetropolis.toolchain.mapping;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import codemetropolis.toolchain.commons.util.FileLogger;
import codemetropolis.toolchain.commons.util.Resources;
import codemetropolis.toolchain.commons.util.Settings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
	/**
	 * Gets the arguments and checks if it contains the "-v" switch
	 * and if it does and if the next argument starts with "-" or not
	 * if it doesn't start with it then it prints an error message from resources
	 * @param args The arguments from the command line
	 * @return returns true if the switch is used incorrectly, false otherwise
	 * @author h983570 Kerekes Roland
	 */

	public static boolean check_v_switch(String[] args) {
		Set<String> optionSet = new HashSet<>(Arrays.asList(args));
		if (optionSet.contains("-v")) {
			int index = Arrays.asList(args).indexOf("-v");
			if (index < args.length - 1) {
				if (!args[index + 1].startsWith("-")){
					System.err.println(Resources.get("illegal_v_switch"));
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {

		FileLogger.load(Settings.get("mapping_log_file"));
		
		CommandLineOptions options = new CommandLineOptions();
	    CmdLineParser parser = new CmdLineParser(options);

	    if (check_v_switch(args)) return;

	    try {
	        parser.parseArgument(args);
	        if((options.getInputFile() == null || options.getMappingFile() == null) && !options.showHelp())
	        	throw new IllegalArgumentException();
	    } catch (CmdLineException | IllegalArgumentException e) {
	    	String message = Resources.get("command_line_error");
	    	FileLogger.logError(message, e);
	    	System.err.println(message);
	    	System.err.println(Resources.get("mapping_usage"));
	    	return;
	    }
	    
	    if(options.showHelp()) {
	    	System.out.println(Resources.get("mapping_introduction"));
	    	System.out.println(Resources.get("mapping_usage"));
	    	return;
	    }

	    MappingExecutor executor = new MappingExecutor();
	    executor.setPrefix(Resources.get("mapping_prefix"));
	    executor.setErrorPrefix(Resources.get("error_prefix"));
	    executor.execute(
	    		new MappingExecutorArgs(
		    		options.getInputFile(),
		    		options.getOutputFile(),
		    		options.getMappingFile(),
		    		options.getScale(),
		    		options.isHierarchyValidationEnabled())
	    		);	
	    
	}

}
