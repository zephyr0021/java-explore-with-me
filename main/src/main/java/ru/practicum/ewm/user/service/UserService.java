package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findByOffset(from, size);
        } else {
            users = userRepository.findByIdAndOffset(ids, from, size);
        }
        return users.stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public User getUserModel(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " was not found"));
    }

    @Transactional
    public UserDto createUser(NewUserRequest request) {
        User user = userRepository.save(userMapper.toUser(request));
        return userMapper.toUserDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        int rows = userRepository.deleteUser(userId);
        if (rows == 0) {
            throw new NotFoundException("User with id = " + userId + " was not found");
        }
    }
}
