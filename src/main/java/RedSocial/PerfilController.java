package RedSocial;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Assuming Perfil class exists in the same package
// import RedSocial.Perfil; 

@Controller
public class PerfilController {

    private static Perfil perfilActual = null;

    // Package-private static method for setting perfilActual in tests
    static void setPerfilActualForTest(Perfil perfil) {
        perfilActual = perfil;
    }

    // Package-private static method for getting perfilActual in tests
    static Perfil getPerfilActualForTest() {
        return perfilActual;
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion() {
        return "crear-perfil"; // Name of the Thymeleaf template
    }

    @PostMapping("/crear")
    public String crearPerfil(@RequestParam String nombre) {
        perfilActual = new Perfil(nombre); // Assumes Perfil(String nombre) constructor
        // For now, we'll print to console to verify, will be removed later
        return "redirect:/ver-perfil";
    }

    @GetMapping("/ver-perfil")
    public String verPerfil(Model model) {
        if (perfilActual == null) {
            return "redirect:/crear"; // If no profile, redirect to creation
        }
        model.addAttribute("perfil", perfilActual);
        // This will eventually use Perfil's methods to get data
        model.addAttribute("nombre", perfilActual.getNombre()); 
        model.addAttribute("estado", perfilActual.getEstado()); 
        model.addAttribute("amigos", perfilActual.listaAmigos()); 
        return "ver-perfil"; // Name of the Thymeleaf template
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstado(@RequestParam String estado, RedirectAttributes redirectAttributes) {
        if (perfilActual != null) {
            perfilActual.actualizarEstado(estado);
            redirectAttributes.addFlashAttribute("mensajeFeedback", "Estado actualizado correctamente.");
        } else {
            // This case should ideally not be reached if /crear is always the entry point for a null perfilActual
            redirectAttributes.addFlashAttribute("errorFeedback", "Debes crear un perfil primero.");
            return "redirect:/crear";
        }
        return "redirect:/ver-perfil";
    }

    @PostMapping("/agregar-amigo")
    public String agregarAmigo(@RequestParam String nombreAmigo, RedirectAttributes redirectAttributes) {
        if (perfilActual != null) {
            if (nombreAmigo != null && !nombreAmigo.trim().isEmpty()) {
                perfilActual.agregarAmigo(nombreAmigo.trim());
                redirectAttributes.addFlashAttribute("mensajeFeedback", "Amigo '" + nombreAmigo.trim() + "' agregado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorFeedback", "El nombre del amigo no puede estar vacío.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorFeedback", "Debes crear un perfil primero.");
            return "redirect:/crear";
        }
        return "redirect:/ver-perfil";
    }

    @PostMapping("/eliminar-amigo")
    public String eliminarAmigo(@RequestParam String nombreAmigo, RedirectAttributes redirectAttributes) {
        if (perfilActual != null) {
            if (nombreAmigo != null && !nombreAmigo.trim().isEmpty()) {
                // Assuming Perfil.eliminarAmigo handles the case where the friend doesn't exist.
                // We might want to modify Perfil.eliminarAmigo to return a boolean for better feedback.
                perfilActual.eliminarAmigo(nombreAmigo.trim());
                redirectAttributes.addFlashAttribute("mensajeFeedback", "Amigo '" + nombreAmigo.trim() + "' eliminado.");
            } else {
                redirectAttributes.addFlashAttribute("errorFeedback", "El nombre del amigo a eliminar no puede estar vacío.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorFeedback", "Debes crear un perfil primero.");
            return "redirect:/crear";
        }
        return "redirect:/ver-perfil";
    }

    @PostMapping("/enviar-mensaje")
    public String enviarMensaje(@RequestParam String nombreAmigoMensaje, @RequestParam String mensaje, RedirectAttributes redirectAttributes) {
        if (perfilActual != null) {
            if (nombreAmigoMensaje != null && !nombreAmigoMensaje.trim().isEmpty() && mensaje != null && !mensaje.trim().isEmpty()) {
                String feedbackMensaje = perfilActual.enviarMensaje(nombreAmigoMensaje.trim(), mensaje.trim()); // mensaje already trimmed if it's not empty
                redirectAttributes.addFlashAttribute("mensajeFeedback", feedbackMensaje);
            } else {
                redirectAttributes.addFlashAttribute("errorFeedback", "El nombre del amigo y el mensaje no pueden estar vacíos.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorFeedback", "Debes crear un perfil primero.");
            return "redirect:/crear";
        }
        return "redirect:/ver-perfil";
    }
}
