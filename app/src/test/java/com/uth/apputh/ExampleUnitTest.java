package com.uth.apputh;

import org.junit.Test;
import com.uth.apputh.models.Cliente;

import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void cliente_model_isCorrect() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Carlos");
        cliente.setApellido("Lopez");
        cliente.setEdad(30);
        cliente.setCorreo("carlos@example.com");

        assertEquals(1, cliente.getId());
        assertEquals("Carlos", cliente.getNombre());
        assertEquals("Lopez", cliente.getApellido());
        assertEquals(30, cliente.getEdad());
        assertEquals("carlos@example.com", cliente.getCorreo());
        assertTrue(cliente.toString().contains("Carlos Lopez"));
    }
}