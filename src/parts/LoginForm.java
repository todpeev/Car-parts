package parts;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm  extends JFrame implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField userNameField;
	JPasswordField passwordField;
	private JLabel userLabel, passwordLabel;
	private JButton OkButton, cancelButton;
	private Connection connection;
	private Statement statement;
	
	public LoginForm()
	{
		super("Login Window");
		
		try 
		{
			connection = DriverManager.getConnection("jdbc:sqlserver://DCSRV1.nwtraders.msft;user=carPartsApp;password=1234567;database=Car_Parts");
			statement = connection.createStatement();
		} 
		catch (SQLException e) 
		{
			 JOptionPane.showMessageDialog( this,
					 "Error connecting to the database",
					 "Connection Error", JOptionPane.ERROR_MESSAGE );
			 System.exit(ERROR);
		}		
		
		userNameField = new JTextField( 10 );
		passwordField = new JPasswordField( 10 );
		userLabel = new JLabel( "Enter User Name: " );
		passwordLabel = new JLabel( "Enter Password: " );
		
		OkButton = new JButton( "OK" );
		OkButton.addActionListener(this);
		cancelButton = new JButton( "Cancel" );
		cancelButton.addActionListener( this );
		
		Container container = getContentPane();
		container.setLayout( new GridLayout( 3, 2 ) );			
		container.add(userLabel);
		container.add(userNameField);
		container.add(passwordLabel);
		container.add(passwordField);
		container.add(OkButton);
		
		container.add(cancelButton);
		
		setLocationRelativeTo(null);
		setSize( 250, 100 );
		setVisible( true );
		
	}
		
	public void submit()
	{
		String userName = userNameField.getText();
		char[] passwordChars = passwordField.getPassword();
		String password = "";
		for(char character:passwordChars)
		{
			password = password + character;
		}
		
		String query = "Select * from [Car_Parts].[dbo].[Users] where user_name = " + "'" + userName + "'";
		
		try 
		{
			ResultSet rowSet = statement.executeQuery(query);
			if(rowSet.next())
			{
				if(password.equals(rowSet.getString("password")))
				{
					setVisible(false);
					new HardwareStore();
					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog( this,
							 "Wrong credentials",
							 "Authetication Error", JOptionPane.ERROR_MESSAGE );
					clear();
					return;
				}
			}
			else
			{
				JOptionPane.showMessageDialog( this,
						 "Wrong credentials",
						 "Authetication Error", JOptionPane.ERROR_MESSAGE );
				clear();
				return;
			}
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog( this,
					 "Error connecting to the database",
					 "Connection Error", JOptionPane.ERROR_MESSAGE );
			 System.exit(ERROR);
		}	
	}
	
	public void clear()
	{
		userNameField.setText("");
		passwordField.setText("");
	}
	
	public static void main(String[] args)
	{
		new LoginForm();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == OkButton)
		{
			submit();
		}
		else if(e.getSource() == cancelButton)
		{
			System.exit(0);
		}		
	}
}
