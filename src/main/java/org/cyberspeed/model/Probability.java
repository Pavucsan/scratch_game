package org.cyberspeed.model;

import java.util.Map;

public class Probability {
    private int column;
    private int row;
    private Map<String, Integer> symbolProbabilities;

    public Probability(int column, int row, Map<String, Integer> symbolProbabilities) {
        this.column = column;
        this.row = row;
        this.symbolProbabilities = symbolProbabilities;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Map<String, Integer> getSymbolProbabilities() {
        return symbolProbabilities;
    }

    public void setSymbolProbabilities(Map<String, Integer> symbolProbabilities) {
        this.symbolProbabilities = symbolProbabilities;
    }
}
