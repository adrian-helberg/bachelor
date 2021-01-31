package de.haw.pipeline.pipe;

import de.haw.module.Estimator;
import de.haw.pipeline.Pipe;
import de.haw.utils.Logging;
import java.util.logging.Logger;

public class EstimatorPipe implements Pipe<PipelineContext, PipelineContext>, Logging {
    private static Logger logger;
    @Override
    public PipelineContext process(PipelineContext input) {
        if (logger == null) logger = getLogger();
        logger.info("Estimate parameters from tree");
        input.estimator = new Estimator(input.tree, input.randomizer);
        logger.info(input.estimator.toString());
        return input;
    }
}
