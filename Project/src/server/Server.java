package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
private volatile ArrayList<ServerWorker> workers = new ArrayList<ServerWorker>();	
private int port;

public Server(int port)
{
	this.port = port;
}
public  ArrayList<ServerWorker>getWorkerLists()
{
	return workers;
}
public void run()
	{
		try 
		{ 
			ServerSocket serverSocket = new ServerSocket(80);
			while(true)
			{
				Socket clientSocket =  serverSocket.accept();
				ServerWorker worker = new ServerWorker(this,clientSocket);
				workers.add(worker);
				worker.start();
			}
		}
	catch(Exception e )
		{
			e.printStackTrace();
		}

	}
public void removeWorker(ServerWorker worker)
	{
		workers.remove(worker);
	}
}
