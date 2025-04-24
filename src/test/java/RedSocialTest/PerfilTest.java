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
        perfil.agregarAmigo("Bob");
        perfil.eliminarAmigo("Bob");
        List<String> amigos = perfil.listaAmigos();
        assertFalse(amigos.contains("Bob"));
    }


}
