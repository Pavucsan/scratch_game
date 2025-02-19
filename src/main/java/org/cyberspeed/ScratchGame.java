package org.cyberspeed;

import org.cyberspeed.model.Probability;
import org.cyberspeed.service.LoadFileService;
import org.cyberspeed.util.MatrixUtil;
import org.cyberspeed.util.ScratchGameCalculationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScratchGame {
    public static void main(String[] args) {
        //java -jar scratch-game.jar config.json 100
        String configFile = args[0];
        int bettingAmount = Integer.parseInt(args[1]);

        LoadFileService.loadConfigFile(configFile);

        // get matrix
        List<List<String>> matrix = MatrixUtil.getRandomMatrix();
        // find wining combinations
        Map<String, List<String>> winCombinations = ScratchGameCalculationUtil.getWinningCombinations(matrix);
        //System.out.println("winCombinations:"+winCombinations);



        System.out.println("{\n\"matrix\": " + matrix + ",");
        System.out.println("\"applied_winning_combinations\": " + winCombinations + ",");



    }
}