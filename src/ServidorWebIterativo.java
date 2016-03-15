
/**
 * Created by Marcos on 26/02/2016.
 */
import java.net.*;
import java.util.Scanner;
import java.io.*;

class ServidorWebIterativo {
    //Heads
    protected static final String OK = "HTTP/1.1 200 OK";
    protected static final String CONTENT_TYPE = "Content-Type: ";
    protected static final String CONTENT_LENGTH = "Content-Length: ";
    
    //Bodies
    protected static final String MIME_HTML = "text/html";
    protected static final String MIME_TXT = "text/plain";
    protected static final String MIME_GIF = "image/gif";
    protected static final String MIME_JPG = "image/jpeg";
    protected static final String MIME_PDF = "application/pdf";
    protected static final String MIME_OTHER = "application/octet-stream";
    
    //Tags
    protected static final String BODY = "<body>";
    protected static final String BODY_END = "</body>";
    protected static final String HEAD = "<head>";
    protected static final String HEAD_END = "</head>";
    protected static final String HTML = "<html>";
    protected static final String HTML_END = "</html>";
    
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
	    if (shouldBeGet.equals("GET")) {
		// esto es el fichero
		String fichero = "." + lee.next();
		// comprobamos si existe
		FileInputStream fis = null;
		boolean existe = true;
		try {
		    fis = new FileInputStream(fichero);
		} catch (FileNotFoundException e) {
		    existe = false;
		}
		if (existe && fichero.length() > 2){
		    if(fichero.endsWith(".html")){
			
		    }
		    else if(fichero.endsWith(".gif")){
			escribe.println(OK);
			escribe.println(CONTENT_TYPE + MIME_GIF);
			//escribe.println(CONTENT_LENGTH + fichero.length());
			escribe.println();
		    }
		    else{
			
		    }
		    escribe.println(HTML + BODY);
		    while ((bytes = fis.read(buffer)) != -1){
			// enviar fichero
			cliente.getOutputStream().write(buffer, 0, bytes);
		    }
		    escribe.println(BODY_END + HTML_END);
		}
		else {
		    //send not found
		    escribe.println(HTML + BODY + "<h1>");
		    escribe.println("HTTP/1.0 404 Not Found");
		    escribe.println("</h1>" + BODY_END + HTML_END);
		    escribe.println();
		}
	    } else {
		escribe.println("HTTP/1.0 501 Not Implemented");
		escribe.println();
	    }
	    cliente.close();
	}
    }
}
