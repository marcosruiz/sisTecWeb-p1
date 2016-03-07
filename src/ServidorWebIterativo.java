/**
 * Created by Marcos on 26/02/2016.
 */
import java.net.*;
import java.util.Scanner;
import java.io.*;

class ServidorWebIterativo {
    public static void main(String args[]) throws UnknownHostException, IOException {
        byte[] buffer = new byte[1024];
        int bytes;
        int puerto = 9000;
        ServerSocket servidor = new ServerSocket(puerto);
        while (true) {
            // espero a que venga un cliente
            Socket cliente = servidor.accept();
            // nos aseguramos de que el fin de línea se ajuste al estandar
            System.setProperty("line.separator", "\r\n");
            Scanner lee = new Scanner(cliente.getInputStream());
            PrintWriter escribe = new PrintWriter(cliente.getOutputStream(), true);
            // esto debe ser el "GET"
            String shouldBeGet = lee.next();
            if(shouldBeGet.equals("GET")){
                // esto es el fichero
                String fichero = "." + lee.next();
                // comprobamos si existe
                FileInputStream fis=null;
                boolean existe = true;
                try {
                    fis = new FileInputStream(fichero);
                } catch (FileNotFoundException e) {
                    existe = false;
                }
                if (existe && fichero.length() > 2)
                    while ((bytes = fis.read(buffer)) != -1) // enviar fichero
                        cliente.getOutputStream().write(buffer, 0, bytes);
                else {
                    escribe.println("HTTP/1.0 404 Not Found");
                    escribe.println();
                }
            }else{
                escribe.println("HTTP/1.0 501 Not Implemented");
                escribe.println();
            }
            cliente.close();
        }
    }
}
