package de.haw.lsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductionRule {
    private final List<Module> lhs;
    private final List<Module> rhs;

    public ProductionRule() {
        lhs = new ArrayList<>();
        rhs = new ArrayList<>();
    }

    public ProductionRule(List<Module> lhs, List<Module> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public List<Module> getLhs() {
        return lhs;
    }

    public List<Module> getRhs() {
        return rhs;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("ProductionRule{");
        sb.append(lhs.stream().map(Object::toString).collect(Collectors.joining()));
        sb.append(" -> ");
        sb.append(rhs.stream().map(Object::toString).collect(Collectors.joining()));
        sb.append("}");
        return sb.toString();
    }
}
