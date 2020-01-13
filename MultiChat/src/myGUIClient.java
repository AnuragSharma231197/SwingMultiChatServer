import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class myGUIClient extends JFrame implements ActionListener, WindowListener{

	private JFrame frame;
	private JTextField serverAddressTextField;
	private JTextField serverPortTextField;
	private JTextField messageTextField;
	private JTextArea messageTextArea; 
	private JTextArea eventTextArea;
	private Client theClient;
	private String username;
	private String address;
	private int port;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new myGUIClient();
	}

	/**
	 * Create the application.
	 */
	public myGUIClient() {
		
		String userName=JOptionPane.showInputDialog("What's Your Name?");
        String serverAddress=JOptionPane.showInputDialog("Please Enter Server Address");
        int portNumber=Integer.parseInt(JOptionPane.showInputDialog("Please Enter Port Number"));
        
        this.username=userName;
        this.address=serverAddress;
        this.port=portNumber;
        
		initialize();
		
		this.theClient = new Client(serverAddress, portNumber, userName, this);
		
		if (!theClient.run())
            return;
	}

	/**
	 * Append method
	 * 
	 */
		void appendEvent(String str) 
		{
			eventTextArea.append(str);
		}
	/**
	 * Append Room
	 *
	 */
		
		void appendRoom(String msg) 
		{
			messageTextArea.append(msg);
		}	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 459, 602);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 432, 38);
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblServerAddress = new JLabel("Server Address");
		panel_1.add(lblServerAddress);
		
		serverAddressTextField = new JTextField(this.address);
		serverAddressTextField.setEditable(false);
		panel_1.add(serverAddressTextField);
		serverAddressTextField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		panel_1.add(lblPort);
		
		serverPortTextField = new JTextField(Integer.toString(this.port));
		serverPortTextField.setEditable(false);
		panel_1.add(serverPortTextField);
		serverPortTextField.setColumns(10);
		
		JLabel lblEnterYourMessage = new JLabel("Enter Your Message Here");
		lblEnterYourMessage.setBounds(10, 40, 410, 16);
		panel.add(lblEnterYourMessage);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(20, 69, 400, 38);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		messageTextField = new JTextField();
		messageTextField.setBounds(0, 0, 400, 38);
		panel_2.add(messageTextField);
		messageTextField.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 125, 422, 348);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		messageTextArea = new JTextArea();
		messageTextArea.setBounds(0, 0, 422, 348);
		panel_3.add(messageTextArea);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(10, 486, 422, 56);
		panel.add(panel_4);
		panel_4.setLayout(null);
		
		JButton btnWhoIsIn = new JButton("Who is in");
		btnWhoIsIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				theClient.sendMessage("WHOISIN");
			}
		});
		btnWhoIsIn.setBounds(295, 13, 85, 25);
		panel_4.add(btnWhoIsIn);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				theClient.sendMessage("LOGOUT");
				System.exit(0);
			}
		});
		btnLogout.setBounds(148, 13, 97, 25);
		panel_4.add(btnLogout);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				String messageUser=messageTextField.getText();
				if(messageUser.equals("")) 
				{
					JOptionPane.showMessageDialog(frame, "Please Enter Something");
				}
				else 
				{
					theClient.sendMessage(messageUser);
					messageTextField.setText("");
				}
				
				
			}
		});
		btnSend.setBounds(27, 13, 97, 25);
		panel_4.add(btnSend);
		
		JLabel usernameLabel = new JLabel(this.username);
		usernameLabel.setBounds(285, 40, 147, 16);
		panel.add(usernameLabel);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(20, 604, 412, 231);
		panel.add(panel_5);
		panel_5.setLayout(null);
		
		eventTextArea = new JTextArea();
		eventTextArea.setBounds(0, 0, 412, 231);
		panel_5.add(eventTextArea);
		
		JLabel eventlabel = new JLabel("Events");
		eventlabel.setBounds(82, 575, 271, 16);
		panel.add(eventlabel);
		
		frame.setVisible(true);
		frame.setSize(467, 895);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
