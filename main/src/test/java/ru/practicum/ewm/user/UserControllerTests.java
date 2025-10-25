package ru.practicum.ewm.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.user.controller.UserController;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import java.util.ArrayList;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.doNothing;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final String usersUrl = "/admin/users";

    @Test
    void createUser() throws Exception {
        NewUserRequest request = Instancio.of(NewUserRequest.class)
                .set(field(NewUserRequest::getEmail), "text@example.com")
                .create();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail(request.getEmail());
        userDto.setName(request.getName());

        when(userService.createUser(request)).thenReturn(userDto);
        mockMvc.perform(post(usersUrl).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);
        mockMvc.perform(delete(usersUrl + "/1"))
                .andExpect(status().isNoContent());

    }

    @Test
    void getUsers() throws Exception {
        when(userService.getUsers(anyList(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
        mockMvc.perform(get(usersUrl))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ArrayList<>())));
    }
}
