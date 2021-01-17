package de.haw.lsystem;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Module {
    private final String symbol;
    private final Set<Parameter<?>> parameters;

    public Module(String sign) {
        this.symbol = sign;
        this.parameters = new HashSet<>();
    }

    public String getSymbol() {
        return symbol;
    }

    public Set<Parameter<?>> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter<?> parameter) {
        parameters.add(parameter);
    }

    @Override
    public String toString() {
        return symbol.isBlank() ? "_" : symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(symbol, module.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
