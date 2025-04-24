package RedSocial;

public class MainPerfil {

    public static void main(String[] args) {
        // Creación de un nuevo perfil
        Perfil perfil = new Perfil("Juan Pérez");

        // Actualización del estado del perfil
        perfil.actualizarEstado("Practicando Git!");

        // Agregar amigos
        perfil.agregarAmigo("María López");
        perfil.agregarAmigo("Carlos Gómez");

        // Mostrar perfil después de agregar amigos
        perfil.mostrarPerfil();

        // Enviar mensaje a un amigo
        perfil.enviarMensaje("María López", "Hola María, ¿cómo estás?");

        // Eliminar un amigo
        perfil.eliminarAmigo("Carlos Gómez");

        // Mostrar perfil después de eliminar un amigo
        perfil.mostrarPerfil();
    }
}
