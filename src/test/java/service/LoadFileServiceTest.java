package service;

import org.cyberspeed.service.LoadFileService;
import org.cyberspeed.util.ScratchGameCalculationUtil;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LoadFileServiceTest {
    /*@InjectMocks
    private LoadFileService loadFileService;

    @Mock
    private ScratchGameCalculationUtil scratchGameCalculationUtil;

    private JSONObject mockConfig;*/

    @BeforeEach
    void setUp() throws ParseException {
        //JSONParser parser = new JSONParser();
        //mockConfig = (JSONObject) parser.parse(jsonConfig);
    }

    @Test
    void testLoadConfigFile() {
        //InputStream mockInputStream = new ByteArrayInputStream(mockConfig.toJSONString().getBytes(StandardCharsets.UTF_8));
        //when(LoadFileService.class.getClassLoader().getResourceAsStream(anyString())).thenReturn(mockInputStream);

        assertDoesNotThrow(() -> LoadFileService.loadConfigFile("config.json"));
        assertEquals(3, ScratchGameCalculationUtil.columns);
        assertEquals(3, ScratchGameCalculationUtil.rows);
    }
}
