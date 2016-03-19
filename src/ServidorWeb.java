
/**
 * Created by Marcos Ruiz García on 26/02/2016.
 * NIP: 648045
 */
import java.net.*;
import java.util.concurrent.Semaphore;
import java.io.*;

class ServidorWeb {
    /*
     * El puerto debe ser mayor de 1023 ya que no tenemos permisos de
     * administrador
     */
    private static int PORT = 9000;
    /*
     * Limitamos el numero de peticiones a 6500 ya que he leido que a partir de
     * esa cifra en Windows la maquina virtual empieza a ser inestable
     */
    private static int MAX_THREADS = 6500;

    /**
     * Este metodo principal se encarga de recibir peticiones de los clientes y
     * lanzar hilos los cuales se encargaran de antender dichas peticiones
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
	@SuppressWarnings("resource")
	ServerSocket servidor = new ServerSocket(PORT);
	// Controlamos con un semaforo el numero de hilos
	Semaphore sem = new Semaphore(MAX_THREADS);
	while (true) {
	    // espero a que venga un cliente
	    Socket cliente = servidor.accept();
	    // lanzo thread para atender cliente
	    try {
		sem.acquire();
		AtiendePeticion peticion = new AtiendePeticion(cliente, sem);
		Thread t = new Thread(peticion);
		t.start();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
}
