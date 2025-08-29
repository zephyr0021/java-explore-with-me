package ru.practicum.ewm.hit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.hit.dto.HitDto;
import ru.practicum.ewm.hit.mapper.HitMapper;
import ru.practicum.ewm.hit.repository.HitRepository;
import ru.practicum.ewm.hit.repository.HitShort;
import ru.practicum.ewm.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HitServiceTests {
    @Mock
    private HitRepository hitRepository;

    @Mock
    private HitMapper hitMapper;

    @InjectMocks
    private HitService hitService;


    private List<HitShort> stat;
    private HitShort hitShort;
    private HitShort hitShort2;
    private HitShort hitShort3;

    @BeforeEach
    void setUp() {
        hitShort = mock(HitShort.class);
        lenient().when(hitShort.getUri()).thenReturn("/events");
        lenient().when(hitShort.getApp()).thenReturn("myApp");
        lenient().when(hitShort.getHits()).thenReturn(125L);

        hitShort2 = mock(HitShort.class);
        lenient().when(hitShort2.getUri()).thenReturn("/events/2");
        lenient().when(hitShort2.getApp()).thenReturn("myApp");
        lenient().when(hitShort2.getHits()).thenReturn(200L);

        hitShort3 = mock(HitShort.class);
        lenient().when(hitShort3.getUri()).thenReturn("/events/10");
        lenient().when(hitShort3.getApp()).thenReturn("myApp");
        lenient().when(hitShort3.getHits()).thenReturn(50L);

        stat = List.of(hitShort, hitShort2, hitShort3);

        lenient().when(hitMapper.toHitDto(any(HitShort.class))).thenAnswer(invocation -> {
            HitShort hit = invocation.getArgument(0);
            HitDto dto = new HitDto();
            dto.setUri(hit.getUri());
            dto.setApp(hit.getApp());
            dto.setHits(hit.getHits());
            return dto;
        });
    }

    @Test
    void getStat() {
        when(hitRepository.findHitsByDate(any(), any(), anyList())).thenReturn(stat);
        List<HitDto> actualStat = hitService.getStat(LocalDateTime.now(), LocalDateTime.now(), List.of(), false);
        assertEquals(actualStat.size(), stat.size());
        assertEquals(actualStat.getFirst().getUri(), stat.getFirst().getUri());
        assertEquals(actualStat.getLast().getUri(), stat.getLast().getUri());

        Mockito.verify(hitRepository, Mockito.times(1)).findHitsByDate(any(), any(), anyList());
        Mockito.verify(hitRepository, Mockito.never()).findUniqueIpHitsByDate(any(), any(), anyList());
    }

    @Test
    void getUniqueStat() {
        when(hitRepository.findUniqueIpHitsByDate(any(), any(), anyList())).thenReturn(stat);
        List<HitDto> actualStat = hitService.getStat(LocalDateTime.now(), LocalDateTime.now(), List.of(), true);
        assertEquals(actualStat.size(), stat.size());
        assertEquals(actualStat.getFirst().getUri(), stat.getFirst().getUri());
        assertEquals(actualStat.getLast().getUri(), stat.getLast().getUri());
    }

    @Test
    void getStatWithNullUnique() {
        when(hitRepository.findHitsByDate(any(), any(), anyList())).thenReturn(stat);
        List<HitDto> actualStat = hitService.getStat(LocalDateTime.now(), LocalDateTime.now(), List.of(), null);
        assertEquals(actualStat.size(), stat.size());
        assertEquals(actualStat.getFirst().getUri(), stat.getFirst().getUri());
        assertEquals(actualStat.getLast().getUri(), stat.getLast().getUri());

        Mockito.verify(hitRepository, Mockito.times(1)).findHitsByDate(any(), any(), anyList());
        Mockito.verify(hitRepository, Mockito.never()).findUniqueIpHitsByDate(any(), any(), anyList());
    }


}
