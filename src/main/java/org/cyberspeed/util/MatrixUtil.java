package org.cyberspeed.util;

import org.cyberspeed.model.Probability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixUtil {

    public static List<List<String>> getRandomMatrix() {
        List<List<String>> matrix = new ArrayList<>();
        Random random = new Random();
        int totalStandardProbabilities = ScratchGameCalculationUtil.standardSymbolProbabilities.stream()
                .mapToInt(prob -> prob.getSymbolProbabilities().values().stream().mapToInt(Integer::intValue).sum()).sum();

        int totalBonusProbabilities = ScratchGameCalculationUtil.bonusSymbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();

        List<String> symbolList = new ArrayList<>(ScratchGameCalculationUtil.symbols.keySet());
        for (int i = 0; i < ScratchGameCalculationUtil.rows; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < ScratchGameCalculationUtil.columns; j++) {

                String symbol = createRandomSymbol(symbolList, totalStandardProbabilities, totalBonusProbabilities, random);

                row.add(symbol);
            }
            matrix.add(row);
        }

        return matrix;
    }

    private static String createRandomSymbol(List<String> symbolList, int totalStandardProbabilities,
                                             int totalBonusProbabilities, Random random) {
        int totalProbabilities = totalStandardProbabilities + totalBonusProbabilities;
        int randomNumber = random.nextInt(totalProbabilities) + 1;
        if (randomNumber <= totalStandardProbabilities) {
            for (Probability probability : ScratchGameCalculationUtil.standardSymbolProbabilities) {
                for (String symbol : symbolList) {
                    Integer symbolProbability = probability.getSymbolProbabilities().get(symbol);
                    if (symbolProbability != null) {
                        if (randomNumber <= symbolProbability) {
                            return symbol;
                        }
                        randomNumber -= symbolProbability;
                    }
                }
            }
        } else {
            randomNumber -= totalStandardProbabilities;
            for (String symbol : ScratchGameCalculationUtil.bonusSymbolProbabilities.keySet()) {
                if (randomNumber-- <= 0) {
                    return symbol;
                }
            }
        }
        return "MISS";
    }
}
