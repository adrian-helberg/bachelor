package de.haw.lsystem;

import java.util.List;
import java.util.stream.Collectors;

public class ProductionRule {
    private String lhs;
    private String rhs;

    public ProductionRule(String lhs, String rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String getLhs() {
        return lhs;
    }

    public String getRhs() {
        return rhs;
    }

    public void setRhs(String rhs) {
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return lhs + " -> " + rhs;
    }

    public void removeSymbol(String symbol) {
        rhs = rhs.replace(symbol, "");
    }
}
