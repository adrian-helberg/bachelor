package de.haw.pipeline.pipe;

import de.haw.inferer.Inferer;
import de.haw.pipeline.Pipe;

public class InfererPipe implements Pipe<PipelineContext, PipelineContext> {
    @Override
    public PipelineContext process(PipelineContext input) {
        var inferer = new Inferer(input.tree);
        // Update pipeline context
        input.lSystem = inferer.infer();

        // Pass pipeline context to next pipe
        return input;
    }
}
