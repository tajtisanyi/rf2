package codemetropolis.toolchain.cdfreducer;

import codemetropolis.toolchain.commons.cdf.exceptions.CdfReaderException;
import codemetropolis.toolchain.commons.cmxml.BuildableTree;
import codemetropolis.toolchain.commons.cmxml.exceptions.CmxmlWriterException;
import codemetropolis.toolchain.commons.executor.AbstractExecutor;
import codemetropolis.toolchain.commons.executor.ExecutorArgs;
import codemetropolis.toolchain.commons.util.Resources;

import java.io.FileNotFoundException;

public class CdfExecutor extends AbstractExecutor {

	public static final double MIN_SCALE = 0.01;
	public static final double MAX_SCALE = 100;

	@Override
	public boolean execute(ExecutorArgs args) {
		CdfReducerExecutorArgs cdfArgs = (CdfReducerExecutorArgs) args;

		return true;
	}
	
}
