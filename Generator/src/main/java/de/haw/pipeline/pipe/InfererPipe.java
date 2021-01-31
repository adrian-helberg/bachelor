package de.haw.pipeline.pipe;

import de.haw.module.Inferer;
import de.haw.pipeline.Pipe;
import de.haw.utils.Logging;

import java.util.logging.Logger;

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
