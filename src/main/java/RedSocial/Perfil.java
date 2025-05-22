package RedSocial;

import java.util.ArrayList;
import java.util.List;


public class Perfil {
    // Atributos del perfil (ejemplo)
    private String nombre;
    private String estado;
    private List<String> listaAmigos;
    
    // Constructor
    public Perfil(String nombre) {
        this.nombre = nombre;
        this.estado = "";
        this.listaAmigos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }
    
    // Método para actualizar el estado del perfil
    public void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }
    
    // Método para agregar un amigo
    public void agregarAmigo(String amigo) {
        listaAmigos.add(amigo);
    }
    
    // Método para eliminar un amigo 
    public void eliminarAmigo(String amigo) {
    	if (listaAmigos.contains(amigo)) {
    		listaAmigos.remove(amigo);  
    	} else {
            // Optionally, you might want to log this or throw an exception
            // For now, it just does nothing if the friend is not found,
            // which is consistent with removing the println.
    	}
    }
    
    public List<String> listaAmigos ()
    {
    	return this.listaAmigos;
    }
    
    // Método para enviar un mensaje a un amigo
    public String enviarMensaje(String amigo, String mensaje) {
        // In a real application, this would store the message or send it.
        // For now, just return a confirmation.
        return "Mensaje enviado a " + amigo + ": '" + mensaje + "'";
    }
    
    // Método para mostrar la información del perfil
    public void mostrarPerfil() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Estado: " + estado);
        System.out.println("Amigos (" + listaAmigos.size() + "): " + listaAmigos);
    }
}
