package org.cyberspeed.util;

import org.cyberspeed.model.Probability;
import org.cyberspeed.model.Symbol;
import org.cyberspeed.model.WinCombination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScratchGameCalculationUtil {
    public static int columns;
    public static int rows;
    public static int reward;
    public static Map<String, Symbol> symbols = new HashMap<>();
    public static List<Probability> standardSymbolProbabilities = new ArrayList<>();
    public static Map<String, Integer> bonusSymbolProbabilities = new HashMap<>();
    public static Map<String, WinCombination> winCombinations = new HashMap<>();


    public static Map<String, List<String>> getWinningCombinations(List<List<String>> matrix) {
        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
        Map<String, Integer> symbolCounts = countSymbols(matrix);

        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();

            if (count >= 3) {
                List<String> appliedCombinations = findWinningCombinations(matrix, symbol, count);
                appliedWinningCombinations.put(symbol, appliedCombinations);
            }
        }
        return appliedWinningCombinations;
    }

    private static Map<String, Integer> countSymbols(List<List<String>> matrix) {
        Map<String, Integer> symbolCounts = new HashMap<>();
        for (List<String> row : matrix) {
            for (String symbol : row) {
                symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
            }
        }
        return symbolCounts;
    }

    private static List<String> findWinningCombinations(List<List<String>> matrix, String symbol, int count) {
        List<String> appliedCombinations = new ArrayList<>();

        for (Map.Entry<String, WinCombination> entry : winCombinations.entrySet()) {
            String winCombinationKey = entry.getKey();
            WinCombination winCombination = entry.getValue();

            if (winCombination.getCount() <= count) {
                if (winCombination.getWhen().equals("same_symbols") && symbol.equals("MISS")) {
                    continue;
                } else if (winCombination.getWhen().equals("linear_symbols") && !isLinearMatch(matrix, winCombination, symbol)) {
                    continue;
                }
                appliedCombinations.add(winCombinationKey);
            }
        }
        return appliedCombinations;
    }

    private static boolean isLinearMatch(List<List<String>> matrix, WinCombination winCombination, String symbol) {
        for (List<String> area : winCombination.getCoveredAreas()) {
            boolean match = true;
            for (String cell : area) {
                int row = Integer.parseInt(cell.split(":")[0]);
                int column = Integer.parseInt(cell.split(":")[1]);
                if (!matrix.get(row).get(column).equals(symbol)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return true;
            }
        }
        return false;
    }

    public static void calculateReward(Map<String, List<String>> appliedWinningCombinations,
                                                     int bettingAmount) {
        reward = 0;

        for (Map.Entry<String, List<String>> entry : appliedWinningCombinations.entrySet()) {
            String symbol = entry.getKey();
            List<String> winCombinationList = entry.getValue();

            Symbol symbolData = symbols.get(symbol);
            if (symbolData == null) {
                System.out.println("Symbol data not found for symbol: " + symbol);
                continue;
            }

            double winReward = bettingAmount * symbolData.getRewardMultiplier();
            double symbolReward = 0.0;

            for (String appliedList : winCombinationList) {
                WinCombination winCombination = winCombinations.get(appliedList);
                winReward *= winCombination.getRewardMultiplier();
                symbolReward += winReward;
            }

            reward += (int) symbolReward;
        }
    }
    public static List<String> getBonusSymbol(List<List<String>> matrix) {
        List<String> bonusSymbols = new ArrayList<>();
        int reward = 0;

        for (List<String> row : matrix) {
            for (String symbol : row) {
                if (isBonusSymbol(symbol)) {
                    bonusSymbols.add(symbol);
                    reward = calculateBonusSymbolReward(symbol, symbols.get(symbol), reward);
                }
            }
        }
        return bonusSymbols;
    }

    private static boolean isBonusSymbol(String symbol) {
        return !symbol.equalsIgnoreCase("MISS") && bonusSymbolProbabilities.containsKey(symbol);
    }

    private static int calculateBonusSymbolReward(String symbol, Symbol symbolDetails, int reward) {
        if (symbolDetails == null) {
            return reward;
        }

        if (symbol.equalsIgnoreCase("10x") || symbol.equalsIgnoreCase("5x")) {
            reward *= (int) symbolDetails.getRewardMultiplier();
        } else if (symbol.equalsIgnoreCase("+1000") || symbol.equalsIgnoreCase("+500")) {
            reward += symbolDetails.getExtra();
        }

        return reward;
    }


}
