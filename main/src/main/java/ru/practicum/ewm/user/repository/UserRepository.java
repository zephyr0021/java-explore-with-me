package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByIdIn(List<Long> ids, Pageable pageable);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    int deleteUser(Long userId);
}
