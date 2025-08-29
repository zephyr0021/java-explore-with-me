package ru.practicum.ewm.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("""
            SELECT h.uri as uri, h.app as app, count(h.ip) AS hits
                FROM Hit h
                WHERE timestamp BETWEEN :start AND :end
                AND (:uris IS NULL OR h.uri IN :uris)
                GROUP BY h.uri, h.app
                ORDER BY hits DESC
            """)
    List<HitShort> findHitsByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query("""
            SELECT h.uri as uri, h.app as app, count(DISTINCT h.ip) AS hits
                FROM Hit h
                WHERE timestamp BETWEEN :start AND :end
                AND (:uris IS NULL OR h.uri IN :uris)
                GROUP BY h.uri, h.app
                ORDER BY hits DESC
            """)
    List<HitShort> findUniqueIpHitsByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                  @Param("uris") List<String> uris);

}
