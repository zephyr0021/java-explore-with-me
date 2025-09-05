package ru.practicum.ewm.user;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    User user1 = Instancio.create(User.class);
    User user2 = Instancio.create(User.class);
    User user3 = Instancio.create(User.class);

    List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
    }

    @Test
    void getUsersNullIds() {
        when(userRepository.findByOffset(anyInt(), anyInt()))
                .thenReturn(users);

        List<UserDto> usersDto = userService.getUsers(null, 0, 10);

        verify(userRepository, times(1)).findByOffset(anyInt(), anyInt());
        assertEquals(users.size(), usersDto.size());
    }

    @Test
    void getUsersByEmptyIds() {

        when(userRepository.findByOffset(anyInt(), anyInt()))
                .thenReturn(users);

        List<UserDto> usersDto = userService.getUsers(List.of(), 0, 10);

        verify(userRepository, times(1)).findByOffset(anyInt(), anyInt());
        assertEquals(users.size(), usersDto.size());
    }

    @Test
    void getUsersByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);

        when(userRepository.findByIdAndOffset(anyList(), anyInt(), anyInt()))
                .thenReturn(users);

        List<UserDto> usersDto = userService.getUsers(ids, 0, 10);

        verify(userRepository, times(1)).
                findByIdAndOffset(anyList(), anyInt(), anyInt());
        assertEquals(users.size(), usersDto.size());
    }


}
