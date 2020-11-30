package Gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
public class ChatUI extends JPanel
{
//	private static final long serialVersionUID = 1L;	
	private JPanel tuningPanel;
	private JTextField reciverChannel;
	private JButton tune;
	private GridBagConstraints gc;
	private JTextPane chatBox;
	private JScrollPane chatScroll;
	private JTextField message;
	private JButton sendButton;
	private JButton disconnectButton;
	private JPanel  buttons;
	
	public ChatUI()
	{
		setVisible(false);
		setLayout(new GridBagLayout());
		setSize(360,640);
		setBackground(new Color(54, 91, 135));
		setLayout(new GridBagLayout());
		
		//**************Top tuning Panel*******************
		tuningPanel = new JPanel();
		tuningPanel.setLayout(new GridBagLayout());
		tuningPanel.setOpaque(false);
		
		reciverChannel = new JTextField("Enter the Reciver's ID");
		reciverChannel.setBorder(BorderFactory.createEmptyBorder());
		reciverChannel.setColumns(20);
		reciverChannel.setHorizontalAlignment(SwingConstants.CENTER);
		reciverChannel.setForeground(new Color(187, 189, 191));
		gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=0;
		gc.ipady=14;
		gc.ipadx=10;
		tuningPanel.add(reciverChannel,gc);
		
		tune = new JButton("Tune");
		tune.setForeground(Color.WHITE);
		tune.setBackground(new Color(73, 214, 82));
		tune.setFont(new Font("Tahoma",Font.PLAIN,14));
		tune.setBorder(BorderFactory.createEmptyBorder());
		tune.setPreferredSize(new Dimension(100,30));
		gc = new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=0;
		tuningPanel.add(tune,gc);
		
		gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=0;
		//gc.insets.bottom=120;
		add(tuningPanel,gc);
		//*************Tuning Panel Finished******************* 
		
		//*********************TextBox*************************
		//chatBox = new JTextArea(24,30);
		chatBox = new JTextPane();
		chatBox.setPreferredSize(new Dimension(329,400));
		chatBox.setEnabled(true);
		chatBox.setEditable(false);
		chatBox.setBorder(BorderFactory.createEmptyBorder());
		chatBox.setBackground(new Color(26, 40, 61));
		chatBox.setFont(new Font("Tahoma",Font.BOLD,16));
		chatScroll = new JScrollPane(chatBox);
		chatScroll.setBorder(BorderFactory.createEmptyBorder());
		gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=1;
		add(chatScroll,gc);
	   //*************TextBox Finished**************************
   	   
		//*****************MessageBox****************************
		message= new JTextField("Type a message");	
		//message.setEnabled(false);
		message.setBorder(BorderFactory.createEmptyBorder());
		message.setPreferredSize(new Dimension(220,20));
		//message.setColumns(20);
		message.setForeground(new Color(187, 189, 191));
		gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=2;
		gc.ipadx=110;
		gc.ipady=14;
		add(message,gc);
		//******************Message Box Finished****************
		
		//*****************Send-Disconnect Buttons********************
		buttons = new JPanel();
		buttons.setLayout(new GridBagLayout());
		buttons.setOpaque(false);
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setFont(new Font("Tahoma",Font.PLAIN,14));
		disconnectButton.setBorder(BorderFactory.createEmptyBorder());
		disconnectButton.setBackground(new Color(227, 50, 50));
		disconnectButton.setForeground(Color.WHITE);
		disconnectButton.setPreferredSize(new Dimension(100,30));
		disconnectButton.setFocusable(false);
		gc = new GridBagConstraints();
		gc.anchor= GridBagConstraints.WEST;
		gc.gridx=0;
		gc.gridy=0;
		gc.insets.right=130;
		buttons.add(disconnectButton,gc);
		
		sendButton = new JButton("Send");
		sendButton.setFont(new Font("Tahoma",Font.PLAIN,14));
		sendButton.setBorder(BorderFactory.createEmptyBorder());
		sendButton.setBackground(new Color(31, 191, 63));
		sendButton.setForeground(Color.WHITE);
		sendButton.setPreferredSize(new Dimension(100,30));
		gc = new GridBagConstraints();
		gc.anchor=GridBagConstraints.EAST;
		gc.gridx=1;
		gc.gridy=0;
		buttons.add(sendButton,gc);
		
		gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=3;
		gc.insets.top=15;
		add(buttons,gc);
	}

	//****************Utility Functions**********//
	public JTextField getReciverChannel()
	{
		return this.reciverChannel;
	}
	public JButton getTuneButton()
	{
		return this.tune;
	}
	public JTextPane getMessageTextBox()
	{
		return this.chatBox;
	}
	public JTextField getSendText()
	{
		return this.message;
	}
	public JButton getDisconnectButton()
	{
		return this.disconnectButton;
	}
	public JButton getSendButton()
	{
		return this.sendButton;
	}		
}
