package codemetropolis.toolchain.converter;

import codemetropolis.toolchain.commons.cdf.CdfTree;
import codemetropolis.toolchain.commons.cdf.converter.CdfConverter;
import codemetropolis.toolchain.commons.cdf.converter.ConverterEvent;
import codemetropolis.toolchain.commons.cdf.converter.ConverterEventListener;
import codemetropolis.toolchain.commons.cdf.exceptions.CdfWriterException;
import codemetropolis.toolchain.commons.exceptions.CodeMetropolisException;
import codemetropolis.toolchain.commons.executor.AbstractExecutor;
import codemetropolis.toolchain.commons.executor.ExecutorArgs;
import codemetropolis.toolchain.commons.util.Resources;
import codemetropolis.toolchain.converter.control.ConverterLoader;

import java.io.FileNotFoundException;
	/** In the ConverterExecutor class there are some try - catch blocks to handle errors. A new block is added to
	 * 	handle the option where there is not any input graph.
	 * 	@author Horváth Bendegúz/983246
	 * */
public class ConverterExecutor extends AbstractExecutor {
	@Override
	public boolean execute(ExecutorArgs args) {
		ConverterExecutorArgs converterArgs = (ConverterExecutorArgs) args;
		
		CdfConverter converter = ConverterLoader.load(converterArgs.getType(), converterArgs.getParams());
		converter.addConverterEventListener(new ConverterEventListener() {
			
			@Override
			public void onConverterEvent(ConverterEvent event) {
				print(event.getMessage());
			}
			
		});
		print(Resources.get("converting_to_cdf"));
		CdfTree cdfTree = null;
		try {
			cdfTree = converter.createElements(converterArgs.getSource());
		} catch (CodeMetropolisException e) {
			printError(e, e.getMessage());
			return false;
		/**
		 * Returns false when the error is caught.
		 * @param e is the the exception to catch
		 * */
		} catch(FileNotFoundException e) {
			print("Warning: File not found");
			return false;
		} catch (Exception e) {
			printError(e, Resources.get("converter_error"));
			return false;
		}
		print(Resources.get("converting_to_cdf_done"));
		
		print(Resources.get("printing_cdf"));
		try {
			cdfTree.writeToFile(converterArgs.getOutputFile());
		} catch (CdfWriterException e) {
			printError(e, Resources.get("cdf_writer_error"));
			return false;
		}
		print(Resources.get("printing_cdf_done"));
		
		return true;
	}

}
