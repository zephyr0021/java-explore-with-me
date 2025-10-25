package ru.practicum.ewm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class MainServerTests {
    @Test
    void mainMethodRuns() {
        assertDoesNotThrow(() ->
                EwmMainServer.main(new String[] {})
        );
    }
}
