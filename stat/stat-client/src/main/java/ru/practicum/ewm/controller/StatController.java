package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.StatClient;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatClient statClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> saveHit(@RequestBody @Valid NewEndpointHitRequest request) {
        return statClient.saveHit(request);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam LocalDateTime start,
                                           @RequestParam LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false) Boolean unique) {
        return statClient.getStat(start, end, uris, unique);

    }
}
