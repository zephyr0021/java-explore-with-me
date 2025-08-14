package ru.practicum.ewm.hit;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import ru.practicum.ewm.hit.model.Hit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HitModelTests {

    @Test
    void testEqualsAndHashCode() {
        Model<Hit> model = Instancio.of(Hit.class)
                .set(Select.field("id"), 1L)
                .toModel();
        Hit hit1 = Instancio.create(model);
        Hit hit2 = Instancio.create(model);

        assertEquals(hit1, hit2);
        assertEquals(hit1.hashCode(), hit2.hashCode());
    }
}
