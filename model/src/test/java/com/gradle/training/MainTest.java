package com.gradle.training;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    void mainHasMessage() {
        Main classUnderTest = new Main();
        assertNotNull(classUnderTest.getMessage(), "Hello and welcome!");
    }
}
