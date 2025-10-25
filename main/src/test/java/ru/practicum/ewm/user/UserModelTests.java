package ru.practicum.ewm.user;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import ru.practicum.ewm.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserModelTests {

    @Test
    void testEqualsAndHashCode() {
        Model<User> model = Instancio.of(User.class)
                .set(Select.field("id"), 1L)
                .toModel();
        User user1 = Instancio.create(model);
        User user2 = Instancio.create(model);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void equalsSelf() {
        Model<User> model = Instancio.of(User.class)
                .set(Select.field("id"), 1L)
                .toModel();
        User user1 = Instancio.create(model);
        assertEquals(user1, user1);
    }
}
