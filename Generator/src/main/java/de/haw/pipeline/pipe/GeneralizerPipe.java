package de.haw.pipeline.pipe;

import de.haw.pipeline.Pipe;

public class GeneralizerPipe implements Pipe<PipelineContext, PipelineContext> {
    @Override
    public PipelineContext process(PipelineContext input) {
        return input;
    }
}
