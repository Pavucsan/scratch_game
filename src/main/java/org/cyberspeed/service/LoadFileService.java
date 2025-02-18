package org.cyberspeed.service;

import org.cyberspeed.model.Symbol;
import org.cyberspeed.util.ScratchGameCalculationUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

//                log.info("symbolName: " + symbolName);
//                log.info("symbol: " + symbol.toString());
                ScratchGameCalculationUtil.symbols.put(symbolName, symbol);
            }

        } catch (IOException | ParseException e) {
            log.warning("Error loading configuration file: "+ configFile);
            e.printStackTrace();
        }
    }
}
