package codemetropolis.toolchain.converter;

import codemetropolis.toolchain.commons.util.FileLogger;
import codemetropolis.toolchain.commons.util.Resources;
import codemetropolis.toolchain.commons.util.Settings;
import codemetropolis.toolchain.converter.control.ConverterType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {

		FileLogger.load(Settings.get("converter_log_file"));
		if (args.length == 1) {
			InputStream is = Main.class.getResourceAsStream(args[0]);
			if (is == null) {
				String errorMessage = String.format("Cannot find resource file %s", args[0]);
				FileLogger.logError(errorMessage, new FileNotFoundException());
				System.err.println(errorMessage);
				return;
			}
			JSONTokener jsonTokener = new JSONTokener(is);
			JSONObject object = new JSONObject(jsonTokener);
			args = convertJsonToArgs(object);
		}

		CommandLineOptions options = new CommandLineOptions();
		CmdLineParser parser = new CmdLineParser(options);

		try {
			parser.parseArgument(args);
			if (options.getType() == null || options.getSource() == null) {
				throw new IllegalArgumentException();
			}
		} catch (CmdLineException | IllegalArgumentException e) {
			String message = Resources.get("command_line_error");
			FileLogger.logError(message, e);
			System.err.println(message);
			System.err.println(Resources.get("converter_usage"));
			return;
		}

		ConverterType converterType;
		try {
			converterType = ConverterType.valueOf(options.getType().toUpperCase());
		} catch (IllegalArgumentException e) {
			String message = String.format("%s%s", Resources.get("error_prefix"), Resources.get("invalid_converter_type"));
			System.err.println(message);
			FileLogger.logError(message, e);
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

		if (options.showHelp()) {
			System.out.println(Resources.get("converter_introduction"));
			System.out.println(Resources.get("converter_usage"));
			return;
		}

		ConverterExecutor executor = new ConverterExecutor();
		executor.setPrefix(Resources.get("converter_prefix"));
		executor.setErrorPrefix(Resources.get("error_prefix"));
		executor.execute(
				new ConverterExecutorArgs(
						converterType,
						options.getSource(),
						options.getOutputFile(),
						params
				));

	}

	private static String[] convertJsonToArgs(JSONObject jsonObject) {
		List<String> result = new ArrayList<>();
		if (jsonObject.has("help")) {
			result.add("-h");
			result.add(String.valueOf(jsonObject.optBoolean("help")));
		}
		if (jsonObject.has("type")) {
			result.add("-t");
			result.add(jsonObject.optString("type"));
		}
		if (jsonObject.has("source")) {
			result.add("-s");
			result.add(jsonObject.optString("source"));
		}
		if (jsonObject.has("output")) {
			result.add("-o");
			result.add(jsonObject.optString("output"));
		}
		if (jsonObject.has("params")) {
			JSONArray params = jsonObject.getJSONArray("params");
			result.add("-p");
			for (int i = 0; i < params.length(); i++) {
				result.add(params.get(i).toString());
			}
		}
		return result.toArray(new String[0]);
	}
}
