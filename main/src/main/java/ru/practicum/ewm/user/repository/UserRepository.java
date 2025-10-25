package ru.practicum.ewm.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE id IN :ids ORDER BY id LIMIT :size OFFSET :offset", nativeQuery = true)
    List<User> findByIdAndOffset(List<Long> ids, Integer offset, Integer size);

    @Query(value = "SELECT * FROM users ORDER BY id LIMIT :size OFFSET :offset", nativeQuery = true)
    List<User> findByOffset(Integer offset, Integer size);



    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    int deleteUser(Long userId);


}
