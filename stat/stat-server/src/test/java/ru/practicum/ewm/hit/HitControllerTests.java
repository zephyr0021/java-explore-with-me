package ru.practicum.ewm.hit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.hit.controller.HitController;
import ru.practicum.ewm.hit.dto.HitDto;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;
import ru.practicum.ewm.hit.service.HitService;

import java.time.LocalDateTime;

import java.util.List;

import static org.instancio.Select.field;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HitController.class)
public class HitControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HitService hitService;

    private NewEndpointHitRequest request;
    private List<HitDto> stat;
    private HitDto hit;
    private HitDto hit2;
    private HitDto hit3;

    private final String hitsUrl = "/hit";
    private final String statUrl = "/stats";

    @BeforeEach
    void setUp() {
        request = Instancio.of(NewEndpointHitRequest.class)
                .set(field(NewEndpointHitRequest::getTimestamp),
                        LocalDateTime.now().withNano(0))
                .create();
        hit = Instancio.create(HitDto.class);
        hit2 = Instancio.create(HitDto.class);
        hit3 = Instancio.create(HitDto.class);
        stat = List.of(hit, hit2, hit3);
    }

    @Test
    void createHit() throws Exception {
        doNothing().when(hitService).saveHit(request);
        mockMvc.perform(post(hitsUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Mockito.verify(hitService, Mockito.times(1)).saveHit(request);
    }

    @Test
    void getStat() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(stat);
        when(hitService.getStat(any(), any(), anyList(), any())).thenReturn(stat);
        mockMvc.perform(get(statUrl)
                        .param("start", "2020-05-05T00:00:00")
                        .param("end", "2035-05-05T00:00:00")
                        .param("uris", "")
                        .param("unique", "false")).andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }


}
