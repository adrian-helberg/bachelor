package de.haw.pipeline.pipe;

import de.haw.module.Generalizer;
import de.haw.pipeline.Pipe;
import de.haw.utils.Logging;

public class GeneralizerPipe implements Pipe<PipelineContext, PipelineContext>, Logging {
    @Override
    public PipelineContext process(PipelineContext input) {
        var logger = getLogger();
        logger.info("Generalize L-System");
        input.lSystem = new Generalizer(input.lSystem, input.w0).generalize();
        logger.info(input.lSystem.toString());
        return input;
    }
}
