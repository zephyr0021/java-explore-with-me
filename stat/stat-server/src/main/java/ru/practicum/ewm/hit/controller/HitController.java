package ru.practicum.ewm.hit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;
import ru.practicum.ewm.hit.service.HitService;

@RestController
@RequestMapping(path = "/hits")
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody @Valid NewEndpointHitRequest request) {
        hitService.saveStat(request);
    }
}
