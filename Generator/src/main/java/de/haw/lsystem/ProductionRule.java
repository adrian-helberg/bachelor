package de.haw.lsystem;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductionRule {
    private String lhs;
    private String rhs;

    public ProductionRule(String lhs, String rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    // Copy constructor
    public ProductionRule(ProductionRule rule) {
        this(rule.getLhs(), rule.getRhs());
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

    public void removeSymbol(String symbol) {
        rhs = rhs.replace(symbol, "");
    }

    @Override
    public String toString() {
        return lhs + " -> " + (rhs.isBlank() ? "_" : rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionRule that = (ProductionRule) o;
        return Objects.equals(lhs, that.lhs) &&
                Objects.equals(rhs, that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}
