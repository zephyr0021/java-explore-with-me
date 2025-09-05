package ru.practicum.ewm.user;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapperImpl;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@DataJpaTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import({UserService.class, UserMapperImpl.class})
public class UserIntegrationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser() {
        NewUserRequest newUserRequest = Instancio.create(NewUserRequest.class);

        userService.createUser(newUserRequest);

        Optional<User> user = userRepository.findById(26L);

        assertTrue(user.isPresent());
    }

    @Test
    void deleteUser() {
        userService.deleteUser(25L);

        Optional<User> user = userRepository.findById(25L);

        assertTrue(user.isEmpty());
    }

    @Test
    void getUsersByIds() {
        List<Long> ids = new ArrayList<>();

        ids.add(1L);
        ids.add(2L);
        ids.add(3L);

        List<UserDto> users = userService.getUsers(ids, 0, 10);

        assertEquals(3, users.size());
        assertEquals(users.getFirst().getName(), "User1");
        assertEquals(users.get(1).getEmail(), "user2@example.com");
        assertEquals(users.getLast().getId(), 3L);
    }

    @Test
    void getUsers() {
        List<UserDto> users = userService.getUsers(null, 0, 10);

        assertEquals(10, users.size());
        assertEquals(users.getFirst().getEmail(), "user1@example.com");
        assertEquals(users.getLast().getName(), "User10");
    }

    @Test
    void getUsersFrom() {
        List<UserDto> users = userService.getUsers(null, 3, 10);

        assertEquals(10, users.size());
        assertEquals(4L, users.getFirst().getId());
    }
}
