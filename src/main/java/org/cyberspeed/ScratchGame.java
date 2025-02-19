package org.cyberspeed;

import org.cyberspeed.service.LoadFileService;
import org.cyberspeed.util.FormatterUtil;
import org.cyberspeed.util.MatrixUtil;
import org.cyberspeed.util.ScratchGameCalculationUtil;

import java.util.List;
import java.util.Map;

public class ScratchGame {
    public static void main(String[] args) {
        //java -jar scratch-game.jar config.json 100
        String configFile = null;// = args[0];
        int bettingAmount = 0;// = Integer.parseInt(args[1]);


        for (int i = 0; i < args.length; i++) {
            if ("--config".equals(args[i])) {
                configFile = args[++i];
            } else if ("--betting-amount".equals(args[i])) {
                try {
                    bettingAmount = Integer.parseInt(args[++i]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid betting amount: " + args[i]);
                }
            }
        }

        LoadFileService.loadConfigFile(configFile);

        // get matrix
        List<List<String>> matrix = MatrixUtil.getRandomMatrix();

        // find wining combinations
        Map<String, List<String>> winCombinations = ScratchGameCalculationUtil.getWinningCombinations(matrix);
        //System.out.println("winCombinations:"+winCombinations);

        //reward calculate
        ScratchGameCalculationUtil.calculateReward(winCombinations, bettingAmount);

        // bonus
        List<String> bonusSymbol = ScratchGameCalculationUtil.getBonusSymbol(matrix);

        FormatterUtil.printGameResponse(matrix, ScratchGameCalculationUtil.reward, winCombinations, bonusSymbol);
    }

}