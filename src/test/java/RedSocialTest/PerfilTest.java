package RedSocialTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import RedSocial.Perfil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PerfilTest {
    private Perfil perfil;

    @BeforeEach
    void setUp() {
        perfil = new Perfil("TestUser");
    }
    
    @Test
    void testAgregarAmigo() {
        perfil.agregarAmigo("Bob");
        List<String> amigos = perfil.listaAmigos();
        assertTrue(amigos.contains("Bob"));
    }

    @Test
    void testEliminarAmigo() {
        // Perfil instance is already created in setUp() and available as 'perfil'

        // Add a few friends to the profile's friend list
        perfil.agregarAmigo("Amigo1");
        perfil.agregarAmigo("Amigo2");

        // Test deleting an existing friend
        perfil.eliminarAmigo("Amigo1");
        List<String> amigos = perfil.listaAmigos();
        assertFalse(amigos.contains("Amigo1"), "Amigo1 should be removed");
        assertTrue(amigos.contains("Amigo2"), "Amigo2 should still be in the list");

        // Test deleting a non-existing friend
        perfil.eliminarAmigo("Amigo3"); // Amigo3 was never added
        amigos = perfil.listaAmigos(); // Re-fetch the list to ensure it's current
        assertTrue(amigos.contains("Amigo2"), "Amigo2 should still be in the list after trying to remove a non-existing friend");
        assertEquals(1, amigos.size(), "Friend list size should be 1 (only Amigo2) after attempting to remove a non-existing friend");
    }


}
