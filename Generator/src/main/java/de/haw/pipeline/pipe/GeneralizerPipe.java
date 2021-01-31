package de.haw.pipeline.pipe;

import de.haw.module.Generalizer;
import de.haw.pipeline.Pipe;
import de.haw.utils.Logging;

import java.util.logging.Logger;

public class GeneralizerPipe implements Pipe<PipelineContext, PipelineContext>, Logging {
    private static Logger logger;
    @Override
    public PipelineContext process(PipelineContext input) {
        if (logger == null) logger = getLogger();
        logger.info("Generalize L-System");
        input.lSystem = new Generalizer(input.lSystem, input.w0).generalize();
        logger.info(input.lSystem.toString());
        return input;
    }
}
