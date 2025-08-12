package ru.practicum.ewm.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.hit.dto.HitDto;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;
import ru.practicum.ewm.hit.mapper.HitMapper;
import ru.practicum.ewm.hit.repository.HitRepository;
import ru.practicum.ewm.hit.repository.HitShort;

import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitService {
    private final HitRepository hitRepository;
    private final HitMapper hitMapper;

    @Transactional
    public void saveHit(NewEndpointHitRequest request) {
        hitRepository.save(hitMapper.toHit(request));
    }

    public List<HitDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<HitShort> stat;
        if (Boolean.TRUE.equals(unique)) {
            stat = hitRepository.findUniqueIpHitsByDate(start, end, uris);
        } else {
            stat = hitRepository.findHitsByDate(start, end, uris);
        }
        return stat.stream()
                .map(hitMapper::toHitDto)
                .collect(Collectors.toList());
    }
}
