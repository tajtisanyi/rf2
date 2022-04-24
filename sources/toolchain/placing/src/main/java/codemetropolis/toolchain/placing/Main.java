package codemetropolis.toolchain.placing;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import codemetropolis.toolchain.commons.util.FileLogger;
import codemetropolis.toolchain.commons.util.Resources;
import codemetropolis.toolchain.commons.util.Settings;

public class Main {

	public static void main(String[] args) {
		
		FileLogger.load(Settings.get("placing_log_file"));
		
		CommandLineOptions options = new CommandLineOptions();
	    CmdLineParser parser = new CmdLineParser(options);

	    try {
	        parser.parseArgument(args);
	        if(options.getInputFile() == null && !options.showHelp())
	        	throw new IllegalArgumentException();
	    } catch (CmdLineException | IllegalArgumentException e) {
			
			/**
			* It goes through the command line arguments, and warns the user if required
			* map_parameter_error =The map parameter does not take a value - found in resources.properties  
	 		* @param args The Command Line arguments
			* @author CQIO60 Marcell Bezzeg
	 		*/

			for(int i=0; i<args.length; i++) {
	    		if ( args[i].equals("true") && i == args.length-1) {
            		System.err.println(Resources.get("map_parameter_error"));
                    return;
            	}
            }

	    	String message = Resources.get("command_line_error");
	    	FileLogger.logError(message, e);
	    	System.err.println(message);
	    	System.err.println(Resources.get("placing_usage"));
	    	return;
	    }
	    
	    if(options.showHelp()) {
	    	System.out.println(Resources.get("placing_introduction"));
	    	System.out.println(Resources.get("placing_usage"));
	    	return;
	    }
		
		PlacingExecutor executor = new PlacingExecutor();
	    executor.setPrefix(Resources.get("placing_prefix"));
	    executor.setErrorPrefix(Resources.get("error_prefix"));
		executor.execute(
				new PlacingExecutorArgs(
						options.getInputFile(),
						options.getOutputFile(),
						options.getLayout(),
						options.showMap())
				);
		
	}

}
