package util;

import org.cyberspeed.model.WinCombination;
import org.cyberspeed.util.ScratchGameCalculationUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScratchGameCalculationUtilTest {

    @Test
    void testGetWinningCombinations() {

        List<List<String>> matrix = Arrays.asList(
                Arrays.asList("A", "A", "A"),
                Arrays.asList("B", "C", "D"),
                Arrays.asList("E", "F", "G")
        );
        List<List<String>> coveredAreas = Arrays.asList(
                Arrays.asList("0:0", "0:1", "0:2")
        );

        WinCombination combination = new WinCombination();
        combination.setWhen("same_symbols");
        combination.setCount(3);
        combination.setRewardMultiplier(2.0);
        combination.setCoveredAreas(coveredAreas);
        ScratchGameCalculationUtil.winCombinations.put("winA", combination);


        Map<String, List<String>> winningCombinations = ScratchGameCalculationUtil.getWinningCombinations(matrix);

        assertTrue(winningCombinations.containsKey("A"), "Expected winning combination for symbol 'A'");
        assertEquals(1, winningCombinations.get("A").size(), "Incorrect number of winning combinations for 'A'");
    }
}
