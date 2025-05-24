package RedSocial;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class PerfilTest {

    @Test
    void testConstructor() {
        Perfil perfil = new Perfil("Juan Perez");
        assertEquals("Juan Perez", perfil.getNombre());
        assertEquals("", perfil.getEstado());
        assertTrue(perfil.listaAmigos().isEmpty());
    }

    @Test
    void testActualizarEstado() {
        Perfil perfil = new Perfil("Test User");
        perfil.actualizarEstado("New Status");
        assertEquals("New Status", perfil.getEstado());
        perfil.actualizarEstado("");
        assertEquals("", perfil.getEstado());
    }

    @Test
    void testAgregarAmigo() {
        Perfil perfil = new Perfil("Test User");
        perfil.agregarAmigo("Maria Lopez");
        List<String> amigos = perfil.listaAmigos();
        assertTrue(amigos.contains("Maria Lopez"));
        assertEquals(1, amigos.size());

        perfil.agregarAmigo("Carlos Gomez");
        amigos = perfil.listaAmigos();
        assertTrue(amigos.contains("Maria Lopez"));
        assertTrue(amigos.contains("Carlos Gomez"));
        assertEquals(2, amigos.size());

        perfil.agregarAmigo("Maria Lopez"); // Adding duplicate
        amigos = perfil.listaAmigos();
        assertEquals(3, amigos.size());
        // Check occurrences of "Maria Lopez"
        int countMaria = 0;
        for (String amigo : amigos) {
            if (amigo.equals("Maria Lopez")) {
                countMaria++;
            }
        }
        assertEquals(2, countMaria);
    }

    @Test
    void testEliminarAmigo() {
        Perfil perfil = new Perfil("Test User");
        perfil.agregarAmigo("Ana");
        perfil.agregarAmigo("Beto");
        perfil.agregarAmigo("Carlos");

        perfil.eliminarAmigo("Beto");
        List<String> amigos = perfil.listaAmigos();
        assertFalse(amigos.contains("Beto"));
        assertEquals(2, amigos.size());
        assertTrue(amigos.contains("Ana"));
        assertTrue(amigos.contains("Carlos"));

        perfil.eliminarAmigo("David"); // Non-existent friend
        amigos = perfil.listaAmigos();
        assertEquals(2, amigos.size());
        assertTrue(amigos.contains("Ana"));
        assertTrue(amigos.contains("Carlos"));

        perfil.eliminarAmigo("Ana");
        amigos = perfil.listaAmigos();
        assertFalse(amigos.contains("Ana"));
        assertEquals(1, amigos.size());
        assertTrue(amigos.contains("Carlos"));

        perfil.eliminarAmigo("Carlos");
        amigos = perfil.listaAmigos();
        assertTrue(amigos.isEmpty());

        perfil.eliminarAmigo("Eva"); // On empty list
        amigos = perfil.listaAmigos();
        assertTrue(amigos.isEmpty());
    }

    @Test
    void testListaAmigos() {
        Perfil perfil = new Perfil("Test User");
        assertTrue(perfil.listaAmigos().isEmpty());

        perfil.agregarAmigo("Amigo1");
        List<String> amigos = perfil.listaAmigos();
        assertEquals(1, amigos.size());
        assertTrue(amigos.contains("Amigo1"));

        perfil.eliminarAmigo("Amigo1");
        assertTrue(perfil.listaAmigos().isEmpty());
    }

    @Test
    void testEnviarMensaje() {
        Perfil perfil = new Perfil("Test User");
        String mensaje1 = perfil.enviarMensaje("Maria Lopez", "Hola Maria!");
        assertEquals("Mensaje enviado a Maria Lopez: 'Hola Maria!'", mensaje1);

        String mensaje2 = perfil.enviarMensaje("Carlos Gomez", "");
        assertEquals("Mensaje enviado a Carlos Gomez: ''", mensaje2);
    }
}
