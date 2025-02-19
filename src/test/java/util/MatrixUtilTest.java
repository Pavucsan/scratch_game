package util;

import org.cyberspeed.util.MatrixUtil;
import org.cyberspeed.util.ScratchGameCalculationUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixUtilTest {
    @Test
    void testGetRandomMatrix() {
        List<List<String>> matrix = MatrixUtil.getRandomMatrix();

        assertEquals(ScratchGameCalculationUtil.rows, matrix.size(), "Matrix row count mismatch");
        assertNotEquals(ScratchGameCalculationUtil.rows, 1000, "Matrix row count not mismatch");

        for (List<String> row : matrix) {
            assertEquals(ScratchGameCalculationUtil.columns, row.size(), "Matrix column count mismatch");
        }

        Set<String> validSymbols = new HashSet<>(ScratchGameCalculationUtil.symbols.keySet());
        validSymbols.addAll(ScratchGameCalculationUtil.bonusSymbolProbabilities.keySet());

        for (List<String> row : matrix) {
            for (String symbol : row) {
                assertTrue(validSymbols.contains(symbol) || symbol.equals("MISS"), "Unexpected symbol found: " + symbol);
            }
        }
    }
}
