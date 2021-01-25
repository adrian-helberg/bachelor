package de.haw.pipeline;

import de.haw.lsystem.LSystem;
import de.haw.pipeline.pipe.PipelineContext;

public class RandomizerPipe implements Pipe<PipelineContext, PipelineContext> {

    @Override
    public PipelineContext process(PipelineContext input) {
        return input;
    }
}
