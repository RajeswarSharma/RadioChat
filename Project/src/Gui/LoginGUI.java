package Gui;
import javax.swing.*;
import java.awt.*;
public class LoginGUI extends JPanel{

	//private static final long serialVersionUID = 1L;
	private JLabel head = new JLabel("RADIOchat");	private JTextField channel;
	private JButton connect;
	private GridBagConstraints gc;
	public LoginGUI()
	{
		setLayout(new GridBagLayout());
		setSize(360,640);
		setBackground(new Color(72, 135, 212));
		
		// heading 
		head.setFont(new Font("Tahoma",Font.BOLD,50));
		head.setForeground(Color.WHITE);
		gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets.bottom=64;
		add(head,gc);
		
		//channel connection
		channel = new JTextField("Enter the Channel ID");
		channel.setHorizontalAlignment(SwingConstants.CENTER);
		channel.setFont(new Font("Tahoma",Font.PLAIN,14));
		channel.setForeground(new Color(187, 189, 191));
		channel.setColumns(20);
		channel.setBorder(BorderFactory.createEmptyBorder());
		gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 1;
		gc.ipady = 16;
		add(channel,gc);
		
		//connect button
		connect = new JButton("Connect");
		connect.setBorder(BorderFactory.createEmptyBorder());
		connect.setBackground(new Color(31, 191, 63));
		connect.setForeground(Color.WHITE);
		connect.setPreferredSize(new Dimension(90,30));
		connect.setFont(new Font("Tahoma",Font.PLAIN,14));
		gc=new GridBagConstraints();
		gc.insets.top=50;
		gc.ipadx=5;
		gc.ipady=5;
		gc.gridx=0;
		gc.gridy=2;
		add(connect,gc);
		
	}
	//*************UTIL_FUNCTIONS**************//
	public JTextField getChannelTextBox()
	{
		return channel;
	}
	public JButton getLoginButton()
	{
		return connect;
	}
	//****************LISTENERS****************//
	
}
