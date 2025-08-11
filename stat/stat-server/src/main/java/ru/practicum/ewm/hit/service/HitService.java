package ru.practicum.ewm.hit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;
import ru.practicum.ewm.hit.mapper.HitMapper;
import ru.practicum.ewm.hit.repository.HitRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HitService {
    private final HitRepository hitRepository;
    private final HitMapper hitMapper;

    @Transactional
    public void saveStat(NewEndpointHitRequest request) {
        hitRepository.save(hitMapper.toHit(request));
    }
}
