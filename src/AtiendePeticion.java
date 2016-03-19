import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AtiendePeticion implements Runnable {
    // Heads
    protected static final String HTTP_VERSION = "HTTP/1.0";

    protected static final String OK = "200 OK";
    protected static final String NOT_MODIFIED = "304 Not Modified";
    protected static final String BAD_REQUEST = "400 Bad Request";
    protected static final String UNAUTHORIZED = "401 Unautorized";
    protected static final String NOT_FOUND = "404 Not Found";
    protected static final String NOT_IMPLEMENTED = "Not Implemented";

    protected static final String CONTENT_TYPE = "Content-Type: ";
    protected static final String CONTENT_LENGTH = "Content-Length: ";

    // Types
    protected static final String MIME_HTML = "text/html";
    protected static final String MIME_TXT = "text/plain";
    protected static final String MIME_GIF = "image/gif";
    protected static final String MIME_JPG = "image/jpeg";
    protected static final String MIME_PDF = "application/pdf";
    protected static final String MIME_OTHER = "application/octet-stream";

    // Tags html
    protected static final String BODY = "<body>";
    protected static final String BODY_END = "</body>";
    protected static final String HEAD = "<head>";
    protected static final String HEAD_END = "</head>";
    protected static final String HTML = "<html>";
    protected static final String HTML_END = "</html>";

    private Socket socket;
    byte[] buffer = new byte[1024];
    int bytes;

    /*
     * Constructor
     */
    public AtiendePeticion(Socket socket) {
	this.socket = socket;
    }

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
		// esto es el fichero
		String rutaFichero = "." + lee.next();
		System.out.print(rutaFichero.substring(1));
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
		    while ((bytes = fis.read(buffer)) != -1) {
			// enviar fichero
			socket.getOutputStream().write(buffer, 0, bytes);
		    }
		    socket.getOutputStream().flush();
		} else {
		    // 404 not found
		    escribe.println(HTTP_VERSION + " " + NOT_FOUND);
		    escribe.println();
		    escribe.println(HTML + BODY + "<h1>");
		    escribe.println(NOT_FOUND);
		    escribe.println("</h1>" + BODY_END + HTML_END);
		    escribe.println();
		}
	    } else {
		// 501 not implemented
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

    }

}
