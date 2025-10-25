package ru.practicum.ewm.category;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import ru.practicum.ewm.category.model.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryModelTests {
    @Test
    void testEqualsAndHashCode() {
        Model<Category> model = Instancio.of(Category.class)
                .set(Select.field("id"), 1L)
                .toModel();
        Category category = Instancio.create(model);
        Category category2 = Instancio.create(model);

        assertEquals(category, category2);
        assertEquals(category.hashCode(), category2.hashCode());
    }

    @Test
    void equalsSelf() {
        Model<Category> model = Instancio.of(Category.class)
                .set(Select.field("id"), 1L)
                .toModel();
        Category category = Instancio.create(model);
        assertEquals(category, category);
    }
}
