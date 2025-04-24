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
    
    // Método para actualizar el estado del perfil
    public void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println(nombre + " actualizó su estado a: " + nuevoEstado);
    }
    
    // Método para agregar un amigo
    public void agregarAmigo(String amigo) {
        listaAmigos.add(amigo);
        System.out.println(amigo + " ha sido agregado a la lista de amigos de " + nombre);
    }
    
    // Método para eliminar un amigo 
    public void eliminarAmigo(String amigo) {
    		listaAmigos.remove(amigo);  
        System.out.println("Amigo eliminado: " + amigo);
    }
    
    public List<String> listaAmigos ()
    {
    	return this.listaAmigos;
    }
    
    // Método para enviar un mensaje a un amigo
    public void enviarMensaje(String amigo, String mensaje) {
        System.out.println(nombre + " envía mensaje a " + amigo + ": " + mensaje);
    }
    
    // Método para mostrar la información del perfil
    public void mostrarPerfil() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Estado: " + estado);
        System.out.println("Amigos (" + listaAmigos.size() + "): " + listaAmigos);
    }
}
