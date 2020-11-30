package server;
import java.net.*;

public class ServerMain {
	
	public static void main(String[]args)
		{
			Server s = new Server(80);
			s.start();
		}
}
