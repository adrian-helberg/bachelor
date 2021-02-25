package de.haw.pipeline.pipe;

import de.haw.tool.Inferer;
import de.haw.pipeline.Pipe;
import de.haw.utils.Logging;

import java.util.logging.Logger;

/**
 * Pipe for executing the infer algorithm.
 * It takes the pipeline context, set it to the result and returns it to the next pipe
 */
public class InfererPipe implements Pipe<PipelineContext, PipelineContext>, Logging {
    private static Logger logger;
    @Override
    public PipelineContext process(PipelineContext input) {
        if (logger == null) logger = getLogger();
        logger.info("Infer L-System from tree");
        logger.info(input.tree.toString());
        // Update pipeline context
        input.lSystem = new Inferer(input.tree).infer();
        logger.info(input.lSystem.toString());
        // Pass pipeline context to next pipe
        return input;
    }
}
