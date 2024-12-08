package com.example.recetas_back.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import com.example.recetas_back.RecetasBackApplication;

import static org.mockito.Mockito.*;

class RecetasBackApplicationMainTest {

    @Test
    void testMain() {
        // Mock de SpringApplication
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(RecetasBackApplication.class, new String[] {}))
                    .thenReturn(null);

            // Llamada al método main
            RecetasBackApplication.main(new String[] {});

            // Verifica que SpringApplication.run fue invocado
            mockedSpringApplication.verify(() -> SpringApplication.run(RecetasBackApplication.class, new String[] {}));
        }
    }
}
