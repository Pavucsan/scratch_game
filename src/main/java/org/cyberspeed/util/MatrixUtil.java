package org.cyberspeed.util;

import org.cyberspeed.model.Probability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixUtil {

    public static List<List<String>> getRandomMatrix() {
        Random random = new Random();
        List<String> symbolList = new ArrayList<>(ScratchGameCalculationUtil.symbols.keySet());

        int totalStandardProbabilities = calculateTotalStandardProbabilities();
        int totalBonusProbabilities = calculateTotalBonusProbabilities();

        return generateMatrix(symbolList, totalStandardProbabilities, totalBonusProbabilities, random);
    }

    private static int calculateTotalStandardProbabilities() {
        return ScratchGameCalculationUtil.standardSymbolProbabilities.stream()
                .mapToInt(prob -> prob.getSymbolProbabilities().values().stream()
                        .mapToInt(Integer::intValue).sum())
                .sum();
    }

    private static int calculateTotalBonusProbabilities() {
        return ScratchGameCalculationUtil.bonusSymbolProbabilities.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static List<List<String>> generateMatrix(List<String> symbolList, int totalStandardProb, int totalBonusProb, Random random) {
        List<List<String>> matrix = new ArrayList<>();

        for (int i = 0; i < ScratchGameCalculationUtil.rows; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < ScratchGameCalculationUtil.columns; j++) {
                row.add(createRandomSymbol(symbolList, totalStandardProb, totalBonusProb, random));
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
            return getStandardSymbol(symbolList, randomNumber);
        }
        return getBonusSymbol(randomNumber - totalStandardProbabilities);
    }

    private static String getStandardSymbol(List<String> symbolList, int randomNumber) {
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
        return "MISS";
    }

    private static String getBonusSymbol(int randomNumber) {
        for (String symbol : ScratchGameCalculationUtil.bonusSymbolProbabilities.keySet()) {
            if (--randomNumber < 0) {
                return symbol;
            }
        }
        return "MISS";
    }
}
