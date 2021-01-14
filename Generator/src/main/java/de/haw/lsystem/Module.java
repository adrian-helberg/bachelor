package de.haw.lsystem;

import java.util.HashSet;
import java.util.Set;

public class Module {
    private final char literal;
    private final Set<Parameter<?>> parameters;

    public Module(char sign) {
        this.literal = sign;
        this.parameters = new HashSet<>();
    }

    public char getLiteral() {
        return literal;
    }

    public Set<Parameter<?>> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter<?> parameter) {
        parameters.add(parameter);
    }
}
