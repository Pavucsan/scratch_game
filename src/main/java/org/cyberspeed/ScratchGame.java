package org.cyberspeed;

import org.cyberspeed.service.LoadFileService;

public class ScratchGame {
    public static void main(String[] args) {
        //java -jar scratch-game.jar config.json 100
        String configFile = args[0];
        int bettingAmount = Integer.parseInt(args[1]);

        LoadFileService.loadConfigFile(configFile);

    }
}