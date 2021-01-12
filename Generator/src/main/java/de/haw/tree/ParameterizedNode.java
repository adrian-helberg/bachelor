package de.haw.tree;

import de.haw.lsystem.Parameter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParameterizedNode extends Node<Template> {

    private final Set<Parameter<?>> parameters;

    public ParameterizedNode(Template data) {
        super(data);
        parameters = new HashSet<>();
    }

    public ParameterizedNode(Template data, Set<Parameter<?>> parameters) {
        super(data);
        this.parameters = parameters;
    }

    public Set<Parameter<?>> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter<?> parameter) {
        parameters.add(parameter);
    }

    public void addParameter(List<Parameter<?>> parameters) {
        this.parameters.addAll(parameters);
    }
}
