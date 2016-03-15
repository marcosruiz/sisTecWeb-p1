import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

public class ServidorWebIterativoTest {
    private Socket socket;
    private String host = "localhost";
    private int port = 9000;
    OutputStream os;
    InputStream is;

    @Before
    public void before() {
	try {
	    socket = new Socket(host, port);
	    os = socket.getOutputStream();
	    is = socket.getInputStream();
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /*@Test
    public void test1() throws IOException {
	String mensaje = "blabla";
	os.write(mensaje.getBytes());
	byte[] arrayBytes = new byte[100];
	int readedBytes = is.read(arrayBytes);
	String recibido = "";
	while (readedBytes > 0) {// Leer del socket
	    recibido = new String(arrayBytes, 0, readedBytes, Charset.forName("UTF-8"));
	    readedBytes = is.read(arrayBytes);
	}
	String expected = "HTTP/1.0 404 Not Found";
	assertTrue(expected.equals(recibido));
    }*/
    @Test
    public void test1(){
	
    }

}
