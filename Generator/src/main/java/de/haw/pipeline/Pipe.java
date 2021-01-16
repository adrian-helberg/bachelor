package de.haw.pipeline;

public interface Pipe<IN, OUT> {
    public OUT process(IN input);
}