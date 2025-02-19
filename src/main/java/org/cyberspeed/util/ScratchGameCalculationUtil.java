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
}
