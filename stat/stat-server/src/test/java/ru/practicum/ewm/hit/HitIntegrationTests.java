package ru.practicum.ewm.hit;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.hit.dto.HitDto;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;
import ru.practicum.ewm.hit.mapper.HitMapperImpl;
import ru.practicum.ewm.hit.model.Hit;
import ru.practicum.ewm.hit.repository.HitRepository;
import ru.practicum.ewm.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@DataJpaTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import({HitService.class, HitMapperImpl.class})
public class HitIntegrationTests {
    @Autowired
    private HitService hitService;

    @Autowired
    private HitRepository hitRepository;


    @Test
    void saveHit() {
        NewEndpointHitRequest newEndpointHitRequest = Instancio.create(NewEndpointHitRequest.class);

        hitService.saveHit(newEndpointHitRequest);

        Optional<Hit> hit = hitRepository.findById(13L);

        assertTrue(hit.isPresent());
    }

    @Test
    void getStat() {
        List<HitDto> hits = hitService.getStat(
                LocalDateTime.of(2025, 8, 1, 11, 0, 0),
                LocalDateTime.of(2025, 8, 3, 13, 0), null, false);
        assertEquals(2, hits.size());
        assertEquals(3, hits.get(0).getHits());
        assertEquals("/events/1", hits.get(0).getUri());
        assertEquals(2, hits.get(1).getHits());
        assertEquals("/events", hits.get(1).getUri());
    }

    @Test
    void getUniqueStat() {
        List<HitDto> hits = hitService.getStat(
                LocalDateTime.of(2025, 8, 1, 11, 0, 0),
                LocalDateTime.of(2025, 8, 3, 13, 0), null, true);
        assertEquals(2, hits.size());
        assertEquals(2, hits.get(0).getHits());
        assertEquals(1, hits.get(1).getHits());
    }

    @Test
    void getHitsByUri() {
        List<HitDto> hits = hitService.getStat(
                LocalDateTime.of(2025, 8, 1, 11, 0, 0),
                LocalDateTime.of(2025, 8, 15, 13, 0),
                List.of("/events/3", "/events/2"), false);
        assertEquals(2, hits.size());
        assertEquals("/events/2", hits.get(0).getUri());
        assertEquals("/events/3", hits.get(1).getUri());
    }
}
