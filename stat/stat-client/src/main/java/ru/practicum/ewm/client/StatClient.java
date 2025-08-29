package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {
    private static final String API_PREFIX_HITS = "/hit";
    private static final String API_PREFIX_STAT = "/stats";

    @Autowired
    public StatClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> saveHit(NewEndpointHitRequest request) {
        return post(API_PREFIX_HITS, request);
    }

    public ResponseEntity<Object> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> params = new HashMap<>();

        params.put("start", start);
        params.put("end", end);
        params.put("uris", uris);
        params.put("unique", unique);

        return get(API_PREFIX_STAT + "?start={start}&end={end}&uris={uris}&unique={unique}", params);
    }
}
