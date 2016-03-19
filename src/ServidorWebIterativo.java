
/**
 * Created by Marcos on 26/02/2016.
 */
import java.net.*;
import java.util.Scanner;
import java.io.*;

class ServidorWebIterativo {
    
    public static void main(String args[]) throws UnknownHostException, IOException {
	int puerto = 9000;
	ServerSocket servidor = new ServerSocket(puerto);
	while (true) {
	    // espero a que venga un cliente
	    Socket cliente = servidor.accept();
	    // lanzo thread para atender cliente
	    AtiendePeticion peticion = new AtiendePeticion(cliente);
	    Thread t = new Thread(peticion);
	    t.start();
	}
    }
}
