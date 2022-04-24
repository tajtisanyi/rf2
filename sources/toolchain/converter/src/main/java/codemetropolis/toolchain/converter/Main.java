package codemetropolis.toolchain.converter;

import codemetropolis.toolchain.commons.util.FileLogger;
import codemetropolis.toolchain.commons.util.Resources;
import codemetropolis.toolchain.commons.util.Settings;
import codemetropolis.toolchain.converter.control.ConverterType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {

		FileLogger.load(Settings.get("converter_log_file"));
		if (args.length == 1 && args[0].endsWith(".json")) {
			args = processFileInput(args);
			if (args == null) return;
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

	/**
	 * @param args json filepath representing the command line parameters
	 * @return a String[] parsed from the input json file by {@link #convertJsonToArgs(JSONObject)}
	 * @author N6L972 Levente Soóky
	 */
	protected static String[] processFileInput(String[] args) {
		File jsonFile = new File(args[0]);
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(jsonFile);
		} catch (FileNotFoundException e) {
			String errorMessage = String.format("%s%s%s", Resources.get("error_prefix"), Resources.get("invalid_file_path"), args[0]);
			System.err.println(errorMessage);
			FileLogger.logError(errorMessage, new FileNotFoundException());
			return null;
		}
		JSONObject object;
		try {
			JSONTokener jsonTokener = new JSONTokener(inputStream);
			object = new JSONObject(jsonTokener);
		} catch (JSONException e) {
			String errorMessage = String.format("%s%s", Resources.get("error_prefix"), Resources.get("invalid_json_file"));
			System.err.println(errorMessage);
			FileLogger.logError(errorMessage, new FileNotFoundException());
			return null;
		}
		args = convertJsonToArgs(object);
		return args;
	}

	/**
	 * @param jsonObject JSONObject representing the parameters
	 * @return a String[] parsed from the parameter jsonObject
	 * @author N6L972 Levente Soóky
	 */
	protected static String[] convertJsonToArgs(JSONObject jsonObject) {
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
			JSONArray params = jsonObject.optJSONArray("params");
			if (params != null) {
				result.add("-p");
				for (int i = 0; i < params.length(); i++) {
					result.add(params.get(i).toString());
				}
			}
		}
		return result.toArray(new String[0]);
	}
}
