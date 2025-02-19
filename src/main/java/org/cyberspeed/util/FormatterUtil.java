package org.cyberspeed.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FormatterUtil {

    public static void printGameResponse(List<List<String>> matrix, int reward,
                                         Map<String, List<String>> winCombinations,
                                         List<String> bonusSymbol) {
        try {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("matrix", matrix);
            response.put("reward", reward);
            response.put("applied_winning_combinations", winCombinations);
            response.put("applied_bonus_symbol", bonusSymbol);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);

            System.out.println(jsonResponse);
        } catch (Exception e) {
            System.err.println("printGameResponse exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
