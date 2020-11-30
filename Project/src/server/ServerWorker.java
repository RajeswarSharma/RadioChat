package server;
import org.apache.commons.lang3.StringUtils;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
public class ServerWorker extends Thread 
{
  public boolean isConnected;
  private Socket clientSocket;
  private Server server;
  private String loginID=null;
  private String reciverID=null;
  private OutputStream reciverOutputStream=null;
  private InputStream inputStream; 
  private OutputStream outputStream;
  private ServerWorker coWorker=null;
  public ServerWorker(Server server,Socket clientSocket)
  	{
	  this.clientSocket = clientSocket;
	  this.server = server;
	  this.isConnected=false;
  	}
	public void run()
	{
				try 
				{
					handelClientSocket(clientSocket);
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
	}
private void handelClientSocket  (Socket clientSocket) throws Exception
	{
			inputStream = clientSocket.getInputStream();
			outputStream = clientSocket.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while((line = reader.readLine())!=null)
			{
				String[]tokens = StringUtils.split(line);
				if(tokens!=null && tokens.length>0)
				{
					String cmd = tokens[0];
					if(cmd.equals("quit")||cmd.equals("logoff")) // logoff or quit
					{
						try 
						{
							handlelogoff();
							break;
						}
						catch(Exception e)
						{
							e.printStackTrace();
							break;
						}
						//handle logoff
					}
					else if(cmd.equals("login")) //login user
					{
						handleLogin(tokens);
						//call handle login
					}
					else if(cmd.equals("connect")) // connect user
					{
						System.out.println("Recived tuning request: "+this.getLogin()+" to "+tokens[1]);
						try{handleConnect(tokens);}// handle connectrequest 
						catch(Exception e) {e.printStackTrace();}
					}
					else if(cmd.equals("msg")) //send message
					{
						
						String[] tokenMessage= StringUtils.split(line,null,2);
						handlemsg(tokenMessage);
					}

					else if(cmd.equals("disconnect"))
					{
						handleDisconnect();//handle Disconnect
					}
					//debugging
					else if(cmd.equals("list"))
					{
						ArrayList<ServerWorker> workers = server.getWorkerLists();
						for (ServerWorker worker : workers) 
						{
							System.out.println(worker.loginID);
						}
						
					}
					else if(cmd.equals("clear"))
					{
					 server.getWorkerLists().clear();
					}
				}
				
			}
			clientSocket.close();		
}
public void setCoWorker(ServerWorker s)
{
	this.coWorker=s;
}
public ServerWorker getCoWorker()
{
	return this.coWorker;
}
public OutputStream getOutputStream()
	{
		return this.outputStream;
	}
public OutputStream getReciverOutputStream ()
{
	return this.reciverOutputStream;
}
public void setReciverOutputStream(OutputStream routputStream)
{
	this.reciverOutputStream=routputStream;
}
public String getLogin()
{
	  return this.loginID;
}
public void setReciverID(String ID)
{
	this.reciverID=ID;
}
private void handleConnect(String[]tokens)throws Exception
{
		ArrayList<ServerWorker> workers = server.getWorkerLists();
		String resiverID = tokens[1];
		boolean flag = false;
		for (ServerWorker worker : workers) {
			if (worker.getLogin().equals(resiverID) && worker.isConnected == false) {
				flag = true;
				this.isConnected = true;
				this.send("tuned " + tokens[1]+"\n");
				this.setReciverID(reciverID);
				this.setReciverOutputStream(worker.getOutputStream());
				this.setCoWorker(worker);
				worker.isConnected = true;
				worker.send("tuned " + this.loginID+"\n");
				worker.setReciverID(this.loginID);
				worker.setReciverOutputStream(this.getOutputStream());
				worker.setCoWorker(this);
				break;
			}
		}
		if (!flag) {
			this.send("error2"); // error2 : User Does not exist
		} 
	
}

private void handleLogin(String[]tokens)
	{
	ArrayList<ServerWorker>workers = server.getWorkerLists();
	for(ServerWorker worker : workers)
	{
		if (worker.getLogin()!=null) 
		{
			if (worker.getLogin().equals(tokens[1])) {
				send("error "+"\n");
				return;
			} 
		}
	}
	this.loginID = tokens[1];	
	send("ok "+"\n");
	System.out.println("Logged User : "+tokens[1]);
		
	}
private void send(String msg)
	{
	msg=msg+"\n";
	try {this.outputStream.write(msg.getBytes());}
	catch(Exception e ) {e.printStackTrace();}
	}
private void handlemsg(String[] tokens) 
	{
	OutputStream reciver = getReciverOutputStream(); 
	//coWorker.send("hi");	
	if(!isConnected && reciver!=null)
			{
				send("msg No reciver Connected");
			}
		else
			{
				try {
					reciver.write(("msg "+tokens[1]+"\n").getBytes());
				} catch (IOException e) {
				
					e.printStackTrace();
				}
			}
		
	}
private void handlelogoff() throws Exception
	{
		
		if(this.isConnected)
		{
			String[] logoffmsg = {"msg ","User "+this.getLogin()+" Disconnected\n"};
			ServerWorker reciver=this.getCoWorker();
			reciver.isConnected=false;
			reciver.setCoWorker(null);
			reciver.setReciverID(null);
			reciver.setReciverOutputStream(null);
			handlemsg(logoffmsg);
			//reciver.send("disconnect ");
			server.removeWorker(this);
			this.clientSocket.close();			
		}
		server.removeWorker(this);
		this.clientSocket.close();
	}
private void handleDisconnect() throws Exception
	{
	  //String[] disconnectMsg = {"User "+this.getLogin()+" Disconnected\n"};
	  //handlemsg(disconnectMsg);
	  if(this.isConnected)
	  	{
		  coWorker.send("disconnect "+"\n");
		  ServerWorker reciver =  this.getCoWorker();
		  reciver.isConnected=false;
		  reciver.setCoWorker(null);
		  reciver.setReciverID(null);
		  reciver.setReciverOutputStream(null); 
		  send("disconnect "+"\n");
		  isConnected=false;
		  setCoWorker(null);
		  setReciverID(null);
		  setReciverOutputStream(null);
	  	}
	 }
}
