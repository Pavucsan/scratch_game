package org.cyberspeed.service;

import org.cyberspeed.model.Probability;
import org.cyberspeed.model.Symbol;
import org.cyberspeed.model.WinCombination;
import org.cyberspeed.util.ScratchGameCalculationUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LoadFileService {
    private static final Logger log = Logger.getLogger(LoadFileService.class.getName());

    public static void loadConfigFile(String configFile) {
        try {
            InputStream inputStream = LoadFileService.class.getClassLoader().getResourceAsStream(configFile);
            if (inputStream == null) {
                throw new RuntimeException("Configuration file not found: " + configFile);
            }

            JSONParser parser = new JSONParser();
            JSONObject config = (JSONObject) parser.parse(new InputStreamReader(inputStream));

            ScratchGameCalculationUtil.columns = config.containsKey("columns") ? ((Long) config.get("columns")).intValue() : 3;
            ScratchGameCalculationUtil.rows = config.containsKey("rows") ? ((Long) config.get("rows")).intValue() : 3;

            JSONObject symbolConfig = (JSONObject) config.get("symbols");
            for (Object key : symbolConfig.keySet()) {
                String symbolName = (String) key;
                JSONObject symbolData = (JSONObject) symbolConfig.get(symbolName);

                Symbol symbol = new Symbol();
                symbol.setRewardMultiplier(symbolData.containsKey("reward_multiplier") ? ((Number) symbolData.get("reward_multiplier")).doubleValue() : 0.0);
                symbol.setType((String) symbolData.get("type"));
                symbol.setExtra(symbolData.containsKey("extra") ? ((Long) symbolData.get("extra")).intValue() : 0);
                symbol.setImpact((String) symbolData.get("impact"));

                //log.info("symbolName: " + symbolName);
                //log.info("symbol: " + symbol.toString());
                ScratchGameCalculationUtil.symbols.put(symbolName, symbol);

                standardSymbolProbabilities(config);
                bonusSymbolProbabilities(config);
                winCombinations(config);
            }

        } catch (IOException | ParseException e) {
            log.warning("Error loading configuration file: "+ configFile);
            e.printStackTrace();
        }
    }

    private static void winCombinations(JSONObject config) {
        //log.info("winCombinations: " + config);

        JSONObject winCombinationsConfig = (JSONObject) config.get("win_combinations");
        for (Object winCombinationName : winCombinationsConfig.keySet()) {
            JSONObject winCombinationData = (JSONObject) winCombinationsConfig.get(winCombinationName);
            WinCombination winCombination = new WinCombination();
            winCombination.setRewardMultiplier(winCombinationData.containsKey("reward_multiplier") ? ((Number) winCombinationData.get("reward_multiplier")).doubleValue() : 0.0);

            //log.info("winCombinationData-when: " + (String) winCombinationData.get("when"));
            winCombination.setCount(((Long) winCombinationData.get("count")).intValue());
            winCombination.setGroup((String) winCombinationData.get("group"));
            winCombination.setWhen((String) winCombinationData.get("when"));

            JSONArray coveredAreasArray = (JSONArray) winCombinationData.get("covered_areas");

            if (coveredAreasArray != null) {
                List<List<String>> coveredAreas = new ArrayList<>();
                for (Object areaObj : coveredAreasArray) {
                    JSONArray areaArray = (JSONArray) areaObj;
                    List<String> area = new ArrayList<>();
                    for (Object cell : areaArray) {
                        area.add((String) cell);
                    }
                    coveredAreas.add(area);
                    winCombination.setCoveredAreas(coveredAreas);
                }
            }
            ScratchGameCalculationUtil.winCombinations.put((String) winCombinationName, winCombination);
        }
    }

    private static void standardSymbolProbabilities(JSONObject config) {
        JSONObject probabilityConfig = (JSONObject) config.get("probabilities");
        JSONArray standardSymbolProbabilitiesArray = (JSONArray) probabilityConfig.get("standard_symbols");

        for (Object obj : standardSymbolProbabilitiesArray) {
            JSONObject probabilityData = (JSONObject) obj;
            int column = ((Long) probabilityData.get("column")).intValue();
            int row = ((Long) probabilityData.get("row")).intValue();

            Map<String, Integer> symbolProbabilities = symbolProbabilities((JSONObject) probabilityData.get("symbols"));

            ScratchGameCalculationUtil.standardSymbolProbabilities.add(new Probability(column, row, symbolProbabilities));
        }
    }

    private static Map<String, Integer> symbolProbabilities(JSONObject symbolProbabilitiesData) {
        Map<String, Integer> symbolProbabilities = new HashMap<>();
        for (Object symbol : symbolProbabilitiesData.keySet()) {
            symbolProbabilities.put((String) symbol, ((Long) symbolProbabilitiesData.get(symbol)).intValue());
        }
        return symbolProbabilities;
    }

    private static void bonusSymbolProbabilities(JSONObject config) {
        JSONObject probabilityConfig = (JSONObject) config.get("probabilities");

        JSONObject bonusSymbolData = (JSONObject) probabilityConfig.get("bonus_symbols");
        JSONObject bonusSymbolProbabilitiesData = (JSONObject) bonusSymbolData.get("symbols");

        for (Object symbol : bonusSymbolProbabilitiesData.keySet()) {
            Object value = bonusSymbolProbabilitiesData.get(symbol);
            if (value instanceof Number) {
                int intValue = ((Number) value).intValue();
                ScratchGameCalculationUtil.bonusSymbolProbabilities.put((String) symbol, intValue);
            } else {
                throw new RuntimeException(
                        "Invalid value type for bonus symbol probability");
            }
        }
    }
}
