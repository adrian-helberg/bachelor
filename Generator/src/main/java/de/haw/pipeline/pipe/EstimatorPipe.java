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
        logger.info(input.tree.toString());
        var e = new Estimator(input.randomizer);
        e.estimateParameters(input.tree);
        logger.info(e.toString());
        input.estimator = e;
        return input;
    }
}
