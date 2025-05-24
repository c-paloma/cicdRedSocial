package RedSocial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

// Ensuring all necessary static imports are present
import static org.junit.jupiter.api.Assertions.*; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PerfilController.class)
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        // Reset perfilActual before each test using the new static method
        PerfilController.setPerfilActualForTest(null);
        // It's good practice to re-initialize mockMvc before each test if it depends on state that might change
        // However, for this controller, the primary state is static (perfilActual)
        // If PerfilController had @Autowired services, we might need @MockBean and further setup.
        // For now, simple static reset is the main concern.
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testMostrarFormularioCreacion() throws Exception {
        mockMvc.perform(get("/crear"))
                .andExpect(status().isOk())
                .andExpect(view().name("crear-perfil"));
    }

    @Test
    void testCrearPerfil() throws Exception {
        mockMvc.perform(post("/crear").param("nombre", "Test User"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"));
        // We can also check if perfilActual was set, though this is an internal detail
        assertNotNull(PerfilController.getPerfilActualForTest());
        assertEquals("Test User", PerfilController.getPerfilActualForTest().getNombre());
    }

    @Test
    void testVerPerfil_cuandoPerfilNoExiste() throws Exception {
        PerfilController.setPerfilActualForTest(null); // Ensure it's null
        mockMvc.perform(get("/ver-perfil"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crear"));
    }

    @Test
    void testVerPerfil_cuandoPerfilExiste() throws Exception {
        Perfil perfilDePrueba = new Perfil("Existing User");
        perfilDePrueba.actualizarEstado("Feeling good");
        perfilDePrueba.agregarAmigo("Friend 1");
        PerfilController.setPerfilActualForTest(perfilDePrueba);

        mockMvc.perform(get("/ver-perfil"))
                .andExpect(status().isOk())
                .andExpect(view().name("ver-perfil"))
                .andExpect(model().attribute("perfil", is(perfilDePrueba)))
                .andExpect(model().attribute("nombre", is("Existing User")))
                .andExpect(model().attribute("estado", is("Feeling good")))
                .andExpect(model().attribute("amigos", contains("Friend 1")));
    }

    @Test
    void testActualizarEstado_cuandoPerfilExiste() throws Exception {
        Perfil perfilDePrueba = new Perfil("Test User");
        PerfilController.setPerfilActualForTest(perfilDePrueba);

        mockMvc.perform(post("/actualizar-estado").param("estado", "New Status"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"))
                .andExpect(flash().attribute("mensajeFeedback", "Estado actualizado correctamente."));
        
        assertEquals("New Status", PerfilController.getPerfilActualForTest().getEstado());
    }

    @Test
    void testActualizarEstado_cuandoPerfilNoExiste() throws Exception {
        PerfilController.setPerfilActualForTest(null);
        mockMvc.perform(post("/actualizar-estado").param("estado", "New Status"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crear"))
                .andExpect(flash().attribute("errorFeedback", "Debes crear un perfil primero."));
    }

    @Test
    void testAgregarAmigo_cuandoPerfilExiste_nombreValido() throws Exception {
        Perfil perfilDePrueba = new Perfil("Test User");
        PerfilController.setPerfilActualForTest(perfilDePrueba);

        mockMvc.perform(post("/agregar-amigo").param("nombreAmigo", "New Friend"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"))
                .andExpect(flash().attribute("mensajeFeedback", "Amigo 'New Friend' agregado correctamente."));
        
        assertTrue(PerfilController.getPerfilActualForTest().listaAmigos().contains("New Friend"));
    }

    @Test
    void testAgregarAmigo_cuandoPerfilExiste_nombreInvalido() throws Exception {
        Perfil perfilDePrueba = new Perfil("Test User");
        PerfilController.setPerfilActualForTest(perfilDePrueba);

        mockMvc.perform(post("/agregar-amigo").param("nombreAmigo", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"))
                .andExpect(flash().attribute("errorFeedback", "El nombre del amigo no puede estar vacío."));
        
        assertFalse(PerfilController.getPerfilActualForTest().listaAmigos().contains(""));
    }

    @Test
    void testAgregarAmigo_cuandoPerfilNoExiste() throws Exception {
        PerfilController.setPerfilActualForTest(null);
        mockMvc.perform(post("/agregar-amigo").param("nombreAmigo", "New Friend"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crear"))
                .andExpect(flash().attribute("errorFeedback", "Debes crear un perfil primero."));
    }

    @Test
    void testEliminarAmigo_cuandoPerfilExiste_amigoExistente() throws Exception {
        Perfil perfilDePrueba = new Perfil("Test User");
        perfilDePrueba.agregarAmigo("FriendToRemove");
        PerfilController.setPerfilActualForTest(perfilDePrueba);

        mockMvc.perform(post("/eliminar-amigo").param("nombreAmigo", "FriendToRemove"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"))
                .andExpect(flash().attribute("mensajeFeedback", "Amigo 'FriendToRemove' eliminado."));
        
        assertFalse(PerfilController.getPerfilActualForTest().listaAmigos().contains("FriendToRemove"));
    }

    @Test
    void testEliminarAmigo_cuandoPerfilExiste_amigoNoExistente() throws Exception {
        Perfil perfilDePrueba = new Perfil("Test User");
        PerfilController.setPerfilActualForTest(perfilDePrueba);

        mockMvc.perform(post("/eliminar-amigo").param("nombreAmigo", "NonExistentFriend"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"))
                // The controller currently gives this message even if friend was not found, as it calls remove which doesn't throw error
                .andExpect(flash().attribute("mensajeFeedback", "Amigo 'NonExistentFriend' eliminado."));
        
        assertTrue(PerfilController.getPerfilActualForTest().listaAmigos().isEmpty()); // Assuming no other friends were added
    }

    @Test
    void testEliminarAmigo_cuandoPerfilNoExiste() throws Exception {
        PerfilController.setPerfilActualForTest(null);
        mockMvc.perform(post("/eliminar-amigo").param("nombreAmigo", "AnyFriend"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crear"))
                .andExpect(flash().attribute("errorFeedback", "Debes crear un perfil primero."));
    }

    @Test
    void testEnviarMensaje_cuandoPerfilExiste_parametrosValidos() throws Exception {
        Perfil perfilDePrueba = new Perfil("Test User");
        perfilDePrueba.agregarAmigo("FriendToMessage");
        PerfilController.setPerfilActualForTest(perfilDePrueba);

        mockMvc.perform(post("/enviar-mensaje")
                .param("nombreAmigoMensaje", "FriendToMessage")
                .param("mensaje", "Hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"))
                .andExpect(flash().attribute("mensajeFeedback", "Mensaje enviado a FriendToMessage: 'Hello'"));
    }

    @Test
    void testEnviarMensaje_cuandoPerfilExiste_parametrosInvalidos() throws Exception {
        Perfil perfilDePrueba = new Perfil("Test User");
        PerfilController.setPerfilActualForTest(perfilDePrueba);

        // Test with empty friend name
        mockMvc.perform(post("/enviar-mensaje")
                .param("nombreAmigoMensaje", "")
                .param("mensaje", "Hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"))
                .andExpect(flash().attribute("errorFeedback", "El nombre del amigo y el mensaje no pueden estar vacíos."));

        // Test with empty message
        mockMvc.perform(post("/enviar-mensaje")
                .param("nombreAmigoMensaje", "SomeFriend")
                .param("mensaje", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ver-perfil"))
                .andExpect(flash().attribute("errorFeedback", "El nombre del amigo y el mensaje no pueden estar vacíos."));
    }

    @Test
    void testEnviarMensaje_cuandoPerfilNoExiste() throws Exception {
        PerfilController.setPerfilActualForTest(null);
        mockMvc.perform(post("/enviar-mensaje")
                .param("nombreAmigoMensaje", "AnyFriend")
                .param("mensaje", "Hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crear"))
                .andExpect(flash().attribute("errorFeedback", "Debes crear un perfil primero."));
    }
}
