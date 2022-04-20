package codemetropolis.toolchain.cdfreducer;

import codemetropolis.toolchain.cdfreducer.model.CdfReducerElement;
import codemetropolis.toolchain.commons.executor.AbstractExecutor;
import codemetropolis.toolchain.commons.executor.ExecutorArgs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;

public class CdfReducerExecutor extends AbstractExecutor {

	public static final double MIN_SCALE = 0.01;
	public static final double MAX_SCALE = 100;

	/**
	 * First it unmarshalls the source .xml document,
	 * then calls the writeToFile function to create the new .xml document
	 * @param args contains the input file, output file and params from the -p switch
	 * @return
	 */
	@Override
	public boolean execute(ExecutorArgs args){
		CdfReducerExecutorArgs cdfArgs = (CdfReducerExecutorArgs) args;

		File inputFile = new File(cdfArgs.getInputFile());

		try {
			JAXBContext context = JAXBContext.newInstance(CdfReducerElement.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			CdfReducerElement cdfReducerElement = (CdfReducerElement) unmarshaller.unmarshal(inputFile);
			cdfReducerElement.writeToFile(cdfArgs.getOutputFile(), cdfArgs.getParams());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
	
}
