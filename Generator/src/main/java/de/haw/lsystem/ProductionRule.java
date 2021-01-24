package de.haw.lsystem;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductionRule {
    private String lhs;
    private String rhs;
    private final float probability;

    public ProductionRule(String lhs, String rhs) {
        this(lhs, rhs, 1f);
    }

    public ProductionRule(String lhs, String rhs, float probability) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.probability = probability;
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
        return lhs + " -> " + (rhs.isBlank() ? "_" : rhs) + (probability > 0 ? " (" + probability + ")" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionRule that = (ProductionRule) o;
        return Float.compare(that.probability, probability) == 0 &&
                Objects.equals(lhs, that.lhs) &&
                Objects.equals(rhs, that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs, probability);
    }
}
