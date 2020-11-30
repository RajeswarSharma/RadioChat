package Gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.lang3.StringUtils;

public class ClientListener extends Thread
{
	
	private LoginGUI loginGUI;
	private ChatUI chatUI;
	private ChatClient chatClient;
	volatile InputStream inputStream;
	volatile BufferedReader reader;
	// login Page instances//
	volatile JTextField loginPage_Channel=null;
	volatile JButton loginPage_loginButton=null;
	// chat Page instances//
	volatile JTextField chatPage_ReciverChannel=null;
	volatile JButton chatPage_tuneButton = null;
	//volatile JTextArea chatPage_MessageBox=null;
	volatile JTextPane chatPage_MessageBox=null;
	volatile JTextField chatPage_SendTextBox=null; 
	volatile JButton chatPage_DisconnectButton=null;
	volatile JButton chatPage_Send=null;
	public ClientListener(ChatClient chatClient,InputStream reader,LoginGUI loginGUI,ChatUI chatUI)
	{
		this.inputStream = reader;
		this.chatUI=chatUI;
		this.loginGUI=loginGUI;
		this.chatClient=chatClient;
	}

	public void run()
	{
		try 
			{
			setLoginPage();
			setChatPage();
			listener();
			}
		catch(Exception e)
			{
			String errorMsg="Unable to connect (Server is Offline or Under Maintenance)";
			int ops = JOptionPane.showConfirmDialog(null,errorMsg,"Connection Error!",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);;
			if(ops==JOptionPane.OK_OPTION || ops == JOptionPane.CANCEL_OPTION) System.exit(0);
			}
	}
	
	private void listener() throws IOException
	{
		reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while((line = reader.readLine())!=null)
			{
				if(chatClient.isActive==false)
				{
					reader.close();
					break;
				}
				String[] tokens = StringUtils.split(line);
				if(tokens.length>0 && tokens!=null)
				{
					String cmd = tokens[0];
					if("tune".equalsIgnoreCase(cmd))
					{
						handleTunned(tokens[1]);
						// block tune button and connect 
					}
					else if("msg".equalsIgnoreCase(cmd))
					{
						String[] tokenMessage= StringUtils.split(line,null,2);
						handleMessage(tokenMessage[1]);
						//handle msg
					}
					else if("error".equalsIgnoreCase(cmd)) //Login Error
					{
						String errorMsg="User with ID already Exist.\nRetry!";
						JOptionPane.showConfirmDialog(null,errorMsg,"Login Error!",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
					}
					else if ("ok".equalsIgnoreCase(cmd))
					{
						// Successful login
						loginGUI.setVisible(false);
						chatUI.setVisible(true);
					}
					else if ("disconnect".equalsIgnoreCase(cmd))
					{
						chatPage_MessageBox.setText("");
						chatPage_ReciverChannel.setText("");
						chatPage_tuneButton.setEnabled(true);
						chatPage_ReciverChannel.setEnabled(true);
						chatPage_SendTextBox.setEnabled(false);
						chatPage_DisconnectButton.setEnabled(false);
						chatPage_Send.setEnabled(false);
						// enable the tune button
						//handle disconnect
					}
					else if("tuned".equalsIgnoreCase(cmd))
					{
						// handle tuned
						chatPage_ReciverChannel.setText("Connected to :"+tokens[1]);
						chatPage_tuneButton.setEnabled(false);
						chatPage_ReciverChannel.setEnabled(false);
						chatPage_SendTextBox.setEnabled(true);
						chatPage_DisconnectButton.setEnabled(true);
						chatPage_Send.setEnabled(true);
					}
					else if("error2".equalsIgnoreCase(cmd))
					{
						// Tuning failed due to no receiver exist
						String errorMsg="No User Found!";
						JOptionPane.showConfirmDialog(null,errorMsg,"Tuning Error",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}
	}

//***************Utility Functions****************//
	public void handleMessage(String msg)
	{
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left,new Color(133, 252, 104));
		StyledDocument doc = chatPage_MessageBox.getStyledDocument();
		try 
			{
				doc.insertString(doc.getLength(),"\n"+msg,left);
				doc.setParagraphAttributes(doc.getLength(), 1, left, false);
			} 
		catch (BadLocationException e1) 
		{
				e1.printStackTrace();
		}
	}
	public void handleTunned(String ID)
	{
		System.out.println("Tunned to: "+ID);
	}
//*******************Connect to the GUI*********************//
	private void setLoginPage()

	{
		loginPage_Channel=loginGUI.getChannelTextBox();
		loginPage_Channel.addMouseListener(clickedOnChannel);
		
		loginPage_loginButton = loginGUI.getLoginButton();
		loginPage_loginButton.addActionListener(onClickLogin);
	}
	private void setChatPage()
	{
		chatPage_ReciverChannel=chatUI.getReciverChannel();
		chatPage_ReciverChannel.addMouseListener(clickedOnReciver);
		
		chatPage_tuneButton = chatUI.getTuneButton();
		chatPage_tuneButton.addActionListener(onClickTune);
		
		chatPage_MessageBox = chatUI.getMessageTextBox();
		
		chatPage_SendTextBox = chatUI.getSendText();
		
		chatPage_DisconnectButton = chatUI.getDisconnectButton();
		chatPage_DisconnectButton.addActionListener(onClickDisonnect);
		
		chatPage_Send = chatUI.getSendButton();
		chatPage_Send.addActionListener(onClickSend);
		
		chatPage_SendTextBox.setEnabled(false);
		chatPage_SendTextBox.addMouseListener(clickedOnSendTextBox);
		
		chatPage_DisconnectButton.setEnabled(false);
		chatPage_Send.setEnabled(false);
	}
	//*****************assigning Listeners***********************//
	// Login Page
	private ActionListener onClickLogin = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e)
		{
			boolean flag = true;
			String freq = loginPage_Channel.getText();
		try {
				Integer.parseInt(freq);
			}
		catch(Exception ex )
			{
				flag = false;
			}
		finally 
			{	if(flag)
				{
				chatClient.login(loginPage_Channel.getText());
				}
			}
		}
	};
	// Login Page
	private	MouseAdapter clickedOnChannel = new MouseAdapter() 
	{
		public void mouseClicked(MouseEvent e)
		{
			loginPage_Channel.setForeground(Color.BLACK);
			loginPage_Channel.setText("");
		}
	};
	// chat Page
	private	MouseAdapter clickedOnReciver = new MouseAdapter() 
	{
		public void mouseClicked(MouseEvent e)
		{
		
				if(chatPage_ReciverChannel.isEnabled())
				{
					chatPage_ReciverChannel.setForeground(Color.BLACK);
					chatPage_ReciverChannel.setText("");
				}
		}
		
	};
	// chat Page
	private ActionListener onClickTune = new ActionListener()

	{
		public void actionPerformed(ActionEvent e)
		{
			chatClient.connect(chatPage_ReciverChannel.getText());
			// send the id to the Server
		}
	};
	// chat Page
	private ActionListener onClickSend= new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			// writing to the local text box
			String msg = chatPage_SendTextBox.getText();
			SimpleAttributeSet right = new SimpleAttributeSet();
			StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
			StyleConstants.setForeground(right,Color.WHITE);
			StyledDocument doc = chatPage_MessageBox.getStyledDocument();
			try 
				{
					chatClient.sendMessage(msg);
					doc.insertString(doc.getLength(),"\n"+msg,right);
					doc.setParagraphAttributes(doc.getLength(), 1, right, false);
					chatPage_SendTextBox.setText("");
				} 
			catch (BadLocationException e1) 
			{
					e1.printStackTrace();
			}
			//chatPage_MessageBox.append(msg);
		}
	};
	// chat Page
	private ActionListener onClickDisonnect = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e)
		{
			chatClient.disconnect();
			
		}
	};
	private	MouseAdapter clickedOnSendTextBox = new MouseAdapter() 
	{
		public void mouseClicked(MouseEvent e)
		{
		
				if(chatPage_SendTextBox.isEnabled())
				{
					chatPage_SendTextBox.setForeground(Color.BLACK);
					chatPage_SendTextBox.setText("");
				}
		}
		
	};
	// chat Page
	
}
