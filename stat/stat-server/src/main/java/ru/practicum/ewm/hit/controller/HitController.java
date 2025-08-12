package ru.practicum.ewm.hit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.hit.dto.HitDto;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;
import ru.practicum.ewm.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody @Valid NewEndpointHitRequest request) {
        hitService.saveHit(request);
    }

    @GetMapping("/stats")
    public List<HitDto> getStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                @RequestParam(required = false) List<String> uris,
                                @RequestParam(required = false) Boolean unique) {
        return hitService.getStat(start, end, uris, unique);

    }
}
