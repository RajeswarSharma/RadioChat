package Gui;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public class MainGUI
{

	private volatile ChatClient chatClient;
	private volatile LoginGUI loginGUI;
	private volatile ChatUI chatUI;
	private volatile JFrame parentFrame;
	public MainGUI()
	{
		loginGUI = new LoginGUI();
		loginGUI.setVisible(true);
		chatUI = new ChatUI();
		chatClient = new ChatClient(loginGUI,chatUI);
		parentFrame = new JFrame();
		parentFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				
				chatClient.disconnect();
				chatClient.command("quit");
				chatClient.close();
				System.exit(0);
			}
		});
		parentFrame.setTitle("RADIOchat");
		parentFrame.setSize(360,640);
		parentFrame.add(loginGUI);
		parentFrame.add(chatUI);
		parentFrame.setVisible(true);
		parentFrame.setResizable(false);	
	}
	public static void main(String[]args) throws InterruptedException
	{
			MainGUI driver = new MainGUI();			
	}
}
