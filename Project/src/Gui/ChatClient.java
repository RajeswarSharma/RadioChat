package Gui;
import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

public class ChatClient
{

	volatile public boolean isActive; 
	volatile private Socket server;
	volatile InputStream inputStream;
	volatile OutputStream outputStream;
	volatile ClientListener clientListener;
	volatile LoginGUI loginGUI;
	volatile ChatUI chatUI;
	
	public ChatClient(LoginGUI loginGUI,ChatUI chatUI)
	{
		try //trying to connect the server
		{ 
			isActive=true;
			server = new Socket("localhost",80); 
			inputStream = server.getInputStream();
			outputStream = server.getOutputStream();
			this.chatUI=chatUI;
			this.loginGUI=loginGUI;
			clientListener = new ClientListener(this,inputStream,this.loginGUI,this.chatUI);
			clientListener.start();
		} 	 
		catch (Exception e) 
		{			
			//********Uncomment It later
			String errorMsg="Unable to connect (Server is Offline or Under Maintenance)";
			int ops = JOptionPane.showConfirmDialog(null,errorMsg,"Connection Error!",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);;
			if(ops==JOptionPane.OK_OPTION || ops == JOptionPane.CANCEL_OPTION) System.exit(0);
		} 
	 }
	public void login( String ID )
	{
	  	 try {
			outputStream.write(("login "+ID+"\n").getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			String errorMsg="Unable to connect (Server is Offline or Under Maintenance)";
			int ops = JOptionPane.showConfirmDialog(null,errorMsg,"Connection Error!",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);;
			if(ops==JOptionPane.OK_OPTION || ops == JOptionPane.CANCEL_OPTION) System.exit(0);
		}
	}
	public void connect(String ID)
	{
		try {
			outputStream.write(("connect "+ID+"\n").getBytes());
		} catch (IOException e) {
			String errorMsg="Unable to connect (Server is Offline or Under Maintenance)";
			int ops = JOptionPane.showConfirmDialog(null,errorMsg,"Connection Error!",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);;
			if(ops==JOptionPane.OK_OPTION || ops == JOptionPane.CANCEL_OPTION) System.exit(0);
		}
	}
	public void disconnect()
	{
       try {
		outputStream.write(("disconnect "+"\n").getBytes());
       } catch (IOException e) {
		e.printStackTrace();
       }		
	}	
	public void sendMessage(String message)
	{
		try 
		{
			outputStream.write(("msg "+message+"\n").getBytes());
		}
		catch(Exception e )
		{
			String errorMsg="Unable to connect (Server is Offline or Under Maintenance)";
			int ops = JOptionPane.showConfirmDialog(null,errorMsg,"Connection Error!",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);;
			if(ops==JOptionPane.OK_OPTION || ops == JOptionPane.CANCEL_OPTION) System.exit(0);
		}
	}
	public void close()
	{
		try {
			isActive = false;
			Thread.sleep(200);
			server.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void command(String cmd)
	{
		try {
			outputStream.write((cmd+" "+"\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}