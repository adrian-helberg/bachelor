package de.haw.exception;

public class NodeAlreadyAttachedException extends RuntimeException {
    public NodeAlreadyAttachedException() {
        super("There is a node already attached to this anchor");
    }
}
