package de.haw.pipeline.pipe;

import de.haw.module.Compressor;
import de.haw.pipeline.Pipe;
import de.haw.utils.Logging;

public class CompressorPipe implements Pipe<PipelineContext, PipelineContext>, Logging {
    @Override
    public PipelineContext process(PipelineContext input) {
        var logger = getLogger();
        logger.info("Compress L-System");
        input.lSystem = new Compressor(input.tree, input.lSystem, input.wL).compress();
        logger.info(input.lSystem.toString());
        return input;
    }
}