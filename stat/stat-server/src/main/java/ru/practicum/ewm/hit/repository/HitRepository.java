package ru.practicum.ewm.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.hit.model.Hit;

public interface HitRepository extends JpaRepository<Hit, Long> {
}
