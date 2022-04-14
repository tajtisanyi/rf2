package codemetropolis.toolchain.cdfreducer;

import codemetropolis.toolchain.commons.util.FileLogger;
import codemetropolis.toolchain.commons.util.Resources;
import codemetropolis.toolchain.commons.util.Settings;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		FileLogger.load(Settings.get("converter_log_file"));

		CommandLineOptions options = new CommandLineOptions();
		CmdLineParser parser = new CmdLineParser(options);

		try {
			parser.parseArgument(args);
			if(options.getSource() == null || options.getOutputFile() == null ){
				throw new IllegalArgumentException();
			}
		} catch (CmdLineException | IllegalArgumentException e) {
			String message = Resources.get("command_line_error");
			FileLogger.logError(message, e);
			System.err.println(message);
			return;
		}

		Map<String, String> params = new HashMap<>();
		if(options.getParams() != null) {
			try {
				String[] paramsArray = options.getParams();
				for(String str : paramsArray) {
					String[] strParts = str.split("=");
					params.put(strParts[0], strParts[1]);
				}
			} catch(Exception e) {
				String message = Resources.get("invalid_params");
				System.err.println(message);
				FileLogger.logError(message, e);
			}
		}

		if(options.showHelp()) {
			System.out.println(Resources.get("converter_introduction"));
			System.out.println(Resources.get("converter_usage"));
			return;
		}

		//LOGIC TIME

		CdfExecutor executor = new CdfExecutor();
		executor.setPrefix(Resources.get("converter_prefix"));
		executor.setErrorPrefix(Resources.get("error_prefix"));
		executor.execute(
				new CdfReducerExecutorArgs(
						options.getSource(),
						options.getOutputFile(),
						params
				));
		System.out.println("{nameRegex=nanameRegex, valueRegex=vavalueRegex}");
	}
}
