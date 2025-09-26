package ru.practicum.ewm.common.hit;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;

import java.time.LocalDateTime;

public class HitCreator {

    public static NewEndpointHitRequest createHit(HttpServletRequest request, LocalDateTime timestamp) {
        NewEndpointHitRequest hitRequest = new NewEndpointHitRequest();

        hitRequest.setApp("main-app");
        hitRequest.setIp(request.getRemoteAddr());
        hitRequest.setUri(request.getRequestURI());
        hitRequest.setTimestamp(timestamp);

        return hitRequest;

    }
}
