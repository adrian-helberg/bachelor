package de.haw.pipeline.pipe;

import de.haw.tool.Compressor;
import de.haw.pipeline.Pipe;
import de.haw.utils.Logging;
import java.util.logging.Logger;

public class CompressorPipe implements Pipe<PipelineContext, PipelineContext>, Logging {
    private static Logger logger;
    @Override
    public PipelineContext process(PipelineContext input) {
        if (logger == null) logger = getLogger();
        logger.info("Compress L-System");
        input.lSystem = new Compressor(input.tree, input.lSystem, input.wL, input.randomizer).compress();
        logger.info(input.lSystem.toString());
        return input;
    }
}