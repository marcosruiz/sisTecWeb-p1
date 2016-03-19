/**
 * Created by Marcos Ruiz García on 19/03/2016.
 * NIP: 648045
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
public class AtiendePeticion implements Runnable {
    // Heads
    private static final String HTTP_VERSION = "HTTP/1.0";

    private static final String OK = "200 OK";
    private static final String NOT_MODIFIED = "304 Not Modified";
    private static final String BAD_REQUEST = "400 Bad Request";
    private static final String UNAUTHORIZED = "401 Unautorized";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String NOT_IMPLEMENTED = "Not Implemented";

    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String CONTENT_LENGTH = "Content-Length: ";

    // Types
    private static final String MIME_HTML = "text/html";
    private static final String MIME_TXT = "text/plain";
    private static final String MIME_GIF = "image/gif";
    private static final String MIME_JPG = "image/jpeg";
    private static final String MIME_PDF = "application/pdf";
    private static final String MIME_OTHER = "application/octet-stream";

    // Tags html
    private static final String BODY = "<body>";
    private static final String BODY_END = "</body>";
    private static final String HEAD = "<head>";
    private static final String HEAD_END = "</head>";
    private static final String HTML = "<html>";
    private static final String HTML_END = "</html>";

    private byte[] buffer = new byte[1024];
    private int bytes;
    
    private Socket socket;
    private static Semaphore sem;

    /**
     * Constructor
     * @param socket
     */
    public AtiendePeticion(Socket socket,Semaphore sem) {
	this.socket = socket;
	AtiendePeticion.sem = sem;
    }
    
    /**
     * Atiende una peticion de un cliente
     */
    @Override
    public void run() {
	// nos aseguramos de que el fin de línea se ajuste al estandar
	System.setProperty("line.separator", "\r\n");
	Scanner lee;
	try {
	    lee = new Scanner(socket.getInputStream());
	    PrintWriter escribe = new PrintWriter(socket.getOutputStream(), true);
	    // esto debe ser el "GET"
	    String shouldBeGet = lee.next();
	    System.out.print(shouldBeGet + " ");
	    if (shouldBeGet.equals("GET")) {
		String nombreFichero = lee.next();
		System.out.println(nombreFichero);
		String rutaFichero = "." + nombreFichero;
		File fichero = new File(rutaFichero);
		// comprobamos si existe
		FileInputStream fis = null;
		boolean existe = true;
		try {
		    fis = new FileInputStream(fichero);
		} catch (FileNotFoundException e) {
		    existe = false;
		}
		if (existe && rutaFichero.length() > 2) {
		    escribe.println(HTTP_VERSION + " " + OK);
		    escribe.println(CONTENT_LENGTH + fichero.length());
		    if (rutaFichero.endsWith(".html")) {
			// HTML
			escribe.println(CONTENT_TYPE + MIME_HTML);
		    } else if (rutaFichero.endsWith(".gif")) {
			// GIF
			escribe.println(CONTENT_TYPE + MIME_GIF);
		    } else {
			// OTHER
			escribe.println(CONTENT_TYPE + MIME_OTHER);
		    }
		    escribe.println();
		    // enviar contenido del fichero
		    while ((bytes = fis.read(buffer)) != -1) {
			socket.getOutputStream().write(buffer, 0, bytes);
		    }
		    socket.getOutputStream().flush();
		} else {
		    // 404 not found: no existe el fichero que se pide
		    escribe.println(HTTP_VERSION + " " + NOT_FOUND);
		    escribe.println();
		    escribe.println(HTML + BODY + "<h1>");
		    escribe.println(NOT_FOUND);
		    escribe.println("</h1>" + BODY_END + HTML_END);
		    escribe.println();
		}
	    } else {
		// 501 not implemented: no es un GET
		escribe.println(HTTP_VERSION + " " + NOT_IMPLEMENTED);
		escribe.println();
		escribe.println(HTML + BODY + "<h1>");
		escribe.println(NOT_IMPLEMENTED);
		escribe.println("</h1>" + BODY_END + HTML_END);
		escribe.println();

	    }
	    socket.close();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	sem.release();
    }

}
