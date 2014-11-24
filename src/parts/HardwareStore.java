package parts;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.event.*;
import java.beans.PropertyVetoException;

import javax.swing.*;
	
public class HardwareStore extends JFrame {
	
	private UpdateDialog updateDialog;
	private NewDialog newDialog;
	private DeleteDialog deleteDialog;
	private InventoryDialog inventoryDialog;
	private JMenuItem newItem, updateItem, deleteItem,
	inventoryItem, exitItem;
	private JDesktopPane desktop;
	private Connection connection;
	private Statement statement;
	
	public HardwareStore()
	{
		super( "Hardware Store" );
		
		try 
		{
			connection = DriverManager.getConnection("jdbc:sqlserver://DCSRV1.nwtraders.msft;user=carPartsApp;password=1234567;database=Car_Parts");
			statement = connection.createStatement();
		}
		catch (SQLException e) 
		{
			System.err.println( e.toString() );
			System.exit(1);
		}
		
		newDialog = new NewDialog( statement );
		updateDialog = new UpdateDialog( statement );
		deleteDialog = new DeleteDialog( statement );
		// set up GUI
		Container container = getContentPane();
		desktop = new JDesktopPane();
		container.add( desktop );
		desktop.add( newDialog );
		desktop.add( updateDialog );
		desktop.add( deleteDialog );

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar( menuBar );
		JMenu fileMenu = new JMenu( "File" );
		menuBar.add( fileMenu );
		newItem = new JMenuItem( "New Record" );
		newItem.addActionListener(
				new ActionListener() { 
				public void actionPerformed( ActionEvent event )
				{
					try {
						newDialog.setIcon( false );
					} catch (PropertyVetoException e) {
						System.exit(1);
					}
				}
				} 
				); 
		
		updateItem = new JMenuItem( "Update Record" );
		updateItem.addActionListener(
			new ActionListener() 
			{ 
				public void actionPerformed( ActionEvent event )
				{
					try 
					{
						updateDialog.setIcon(false);
					} 
					catch (PropertyVetoException e) 
					{
						System.exit(1);
					}
				}
			} 
		); 
		
		deleteItem = new JMenuItem( "Delete Record" );
		deleteItem.addActionListener(
			new ActionListener() 
			{ 
				public void actionPerformed( ActionEvent event )
				{
					try 
					{
						deleteDialog.setIcon( false );
					} 
					catch (PropertyVetoException e) 
					{
						System.exit(1);
					}
				}
			} 
		); 
			
		 inventoryItem = new JMenuItem( "Show Inventory" );
		 inventoryItem.addActionListener(
	
			 new ActionListener() 
			 { 
			
				 public void actionPerformed( ActionEvent event )
				 {
					inventoryDialog = new InventoryDialog( statement );
					desktop.add( inventoryDialog );
					inventoryDialog.setVisible( true );
				 	inventoryDialog.display();
				 }
			
			 } 
		); 
	
		 exitItem = new JMenuItem( "Exit" );
		 exitItem.addActionListener(
	
				 new ActionListener() 
				 { 
	
					 public void actionPerformed( ActionEvent event )
					 {
						 System.exit( 0 );
					 }
				
				 } 
	
			); 
	
		 fileMenu.add( newItem );
		 fileMenu.add( updateItem );
		 fileMenu.add( deleteItem );
		 fileMenu.addSeparator();
		 fileMenu.add( inventoryItem );
		 fileMenu.addSeparator();
		 fileMenu.add( exitItem );
		
		 setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		 setLocationRelativeTo(null);
		 setSize( 490, 330 );
		 setVisible( true );

			try 
			{				
				updateDialog.setIcon(true);
				newDialog.setIcon(true);
				deleteDialog.setIcon(true);
			} catch (PropertyVetoException e) {
				System.exit(1);
			}

			
	 } 
	
	 public static void main( String args[] )
	 {
		 EventQueue.invokeLater(
				 new Runnable() 
				 { 
					 public void run() 
					 {
						 new HardwareStore();
					 }
				});
	 }
	
	 } 


	
	 // class for updating records
	 class UpdateDialog extends JInternalFrame implements ActionListener
	 {
	
		 private JTextField recordField, toolField, quantityField, costField, locationField;
		 private JLabel recordLabel, toolLabel, quantityLabel, costLabel, locationLabel;
		 private JButton saveButton, cancelButton;
		 private Record data;
		 private String recordNumber;
		 private Statement statement;
		
		 public UpdateDialog( Statement state ) 
		 {
			 super( "Update Record", false, false , false, true );
			 
			 statement = state;
			 data = new Record();
			
			 // set up GUI
			 recordLabel = new JLabel( "Record Number" );
			 recordField= new JTextField( 10 );
			 toolLabel = new JLabel( "Tool Name" );
			 toolField = new JTextField( 10 );
			 toolField.setEditable( false );
			 quantityLabel = new JLabel( "Quantity" );
			 quantityField = new JTextField( 10 );
			 quantityField.setEditable( false );
			 costLabel = new JLabel( "Cost" );
			 costField = new JTextField( 10 );
			 costField.setEditable( false );
			 locationLabel = new JLabel( "Location" );
			 locationField = new JTextField( 10 );
			 locationField.setEditable(false);			 
			 
			 saveButton = new JButton( "Save Changes" );
			 saveButton.setEnabled(false);
			 cancelButton = new JButton( "Cancel" );
			
			 recordField.addActionListener( this );
			 saveButton.addActionListener( this );
			 cancelButton.addActionListener( this );
			
			 Container container = getContentPane();
			 container.setLayout( new GridLayout( 6, 2 ) );
			 container.add( recordLabel );
			 container.add( recordField );
			 container.add( toolLabel );
			 container.add( toolField );
			 container.add( quantityLabel );
			 container.add( quantityField );
			 container.add( costLabel );
			 container.add( costField );
			 container.add( locationLabel );
			 container.add( locationField );
			 container.add( saveButton );
			 container.add( cancelButton );
			
			 setSize( 300, 150 );
			 setVisible( true );
		 }
		
		 // process events
		 public void actionPerformed( ActionEvent event )
		 {
			 // text field
			 if ( event.getSource() == recordField ) 
			 {				 
				 recordNumber = recordField.getText();
				 if ( !recordNumber.matches("\\w{3}-\\d{3}") )
				 {
					 JOptionPane.showMessageDialog( this, "Invalid record number entered",
					 "Account Error", JOptionPane.ERROR_MESSAGE );
					 clear();
					 return;
				 }
		
				 try 
				 {
					 data = new Record();
					 clear();
					 data.read(statement, recordNumber);
				 }	
				 catch ( SQLException ex ) 
				 {
					 JOptionPane.showMessageDialog( this,
					 "Error while reading from database",
					 "Read Error", JOptionPane.ERROR_MESSAGE );
				 }
		
				 // retrieve current record information
				 if ( data.getRecordNumber() != "0" ) 
				 {
					 saveButton.setEnabled(true);
					 recordField.setText(data.getRecordNumber());
					 toolField.setText( data.getToolName() );
					 toolField.setEditable( true );
					 quantityField.setText(
					 Integer.toString( data.getQuantity() ) );
					 quantityField.setEditable( true );
					 costField.setText( String.valueOf( data.getCost() ) );
					 costField.setEditable( true );
					 locationField.setText(data.getLocation());
					 locationField.setEditable(true);	
				 }
				 else 
				 {
					 recordField.setText( recordNumber + " does not exist" );
					 toolField.setText( "" );
					 quantityField.setText( "" );
					 costField.setText( "" );
				 }
			 }
			 // save button
			 else if ( event.getSource() == saveButton ) 
			 {			
				 // update record information
				 try 
				 {
					 data.setRecordNumber( recordNumber );
					 data.setToolName( toolField.getText() );
					 data.setQuantity(
					 Integer.parseInt( quantityField.getText() ) );
					 data.setCost( ( Double.valueOf(
					 costField.getText() ) ).doubleValue() );
					 data.setLocation(locationField.getText());
					 data.write( statement , recordField.getText());
				 }
				 catch (SQLException | IOException e) 
				 {
					 e.printStackTrace();
					 JOptionPane.showMessageDialog( this,
							 "Error while writing to database",
							 "Write Error", JOptionPane.ERROR_MESSAGE );
					 return;
				}
			
				 data = new Record();
				 clear();
			 }		
			 // cancel button
			 else if ( event.getSource() == cancelButton ) 
			 {
					try 
					{
						setIcon(true);
					} 
					catch (PropertyVetoException e) 
					{
						System.exit(1);
					}
				 data = new Record();
				 clear();
			 }
		
		 }
		
		 // clear text fields
		 private void clear()
		 {
			 toolField.setEditable( false );
			 quantityField.setEditable( false );
			 costField.setEditable( false );
			 locationField.setEditable(false);
			 saveButton.setEnabled(false);
			 recordField.setText( "" );
			 toolField.setText( "" );
			 quantityField.setText( "" );
			 costField.setText( "" );
			 locationField.setText("");
		 }
	
	 } 
	
	 // class for creating new records
	 class NewDialog extends JInternalFrame implements ActionListener {
	
		 private JTextField recordField, toolField, quantityField, costField, locationField;
		 private JLabel recordLabel, toolLabel, quantityLabel, costLabel, locationLabel;
		 private JButton saveButton, cancelButton;
		 private Record data;
		 private String recordNumber;
		 private Statement statement;
		 
	 public NewDialog( Statement state)
	 {
		 super( "New Record" , false, false, false, true );
		 statement = state;
		 data = new Record();
		
		 // set up GUI
		 recordLabel = new JLabel( "Record Number" );
		 recordField= new JTextField( 10 );
		 toolLabel = new JLabel( "Tool Name" );
		 toolField = new JTextField( 10 );
		 quantityLabel = new JLabel( "Quantity" );
		 quantityField = new JTextField( 10 );
		 costLabel = new JLabel( "Cost" );
		 costField = new JTextField( 10 );
		 locationLabel = new JLabel( "Location" );
		 locationField = new JTextField( 10 );
		
		 saveButton = new JButton( "Save Changes" );
		 cancelButton = new JButton( "Cancel" );
		 saveButton.addActionListener( this );
		 cancelButton.addActionListener( this );
		
		 Container container = getContentPane();
		 container.setLayout( new GridLayout( 6, 2 ) );
		 container.add( recordLabel );
		 container.add( recordField );
		 container.add( toolLabel );
		 container.add( toolField );
		 container.add( quantityLabel );
		 container.add( quantityField );
		 container.add( costLabel );
		 container.add( costField );
		 container.add( locationLabel );
		 container.add( locationField );
		 container.add( saveButton );
		 container.add( cancelButton );
		
		 setSize( 300, 150 );
		 setVisible( true );
	 }
	
	 
	 public void actionPerformed( ActionEvent event )
	 {
		 	// save button
		 	if ( event.getSource() == saveButton ) 
		 	{		
				 recordNumber = recordField.getText() ;
			
				 // check if part number is valid
				 if ( !recordNumber.matches("\\w{3}-\\d{3}") )
				 {
					 JOptionPane.showMessageDialog( this, "Invalid record number entered",
					 "Account Error", JOptionPane.ERROR_MESSAGE );
					 data = new Record();
					 clear();
					 return;
				 }	
		 
				 try 
				 {
					 data.read( statement , recordNumber );
				 } 
				 catch (SQLException e1) 
				 {
					 JOptionPane.showMessageDialog( this,
					 "Error",
					 "Unable to read from database!", JOptionPane.ERROR_MESSAGE );
					 clear();
					 return;
				 }				 
				 // record number already exists
				 if ( data.getRecordNumber() != "0" ) 
				 {
					 JOptionPane.showMessageDialog( this,
					 "Record number already exists",
					 "Duplicate record number", JOptionPane.ERROR_MESSAGE );
					 clear();
					 data = new Record();
					 return;
				 }
				 
				 if(toolField.getText().equals("") || locationField.getText().equals(""))
				 {
						JOptionPane.showMessageDialog( this,
					 			"Error , please fill all fields",
					 			"Write Error", JOptionPane.ERROR_MESSAGE );
						data = new Record();
						return;
				 }
			 	// set values of new record and write to database
			 	try 
			 	{
					 data.setRecordNumber( recordNumber );
					 data.setToolName( toolField.getText() );
					 data.setQuantity(
					 Integer.parseInt( quantityField.getText() ) );
					 data.setCost( ( new Double(
					 costField.getText() ) ).doubleValue() );
					 data.setLocation(locationField.getText());
					 data.write( statement, "create" );
			 	}
			 	catch ( NumberFormatException e) 
			 	{
				 	JOptionPane.showMessageDialog( this,
				 			"Error , incorrect data type entered in field",
				 			"Write Error", JOptionPane.ERROR_MESSAGE );
				 	clear();
				 	data = new Record();
				 	return;				 
			 	} 
			 	catch (IOException|SQLException  e) 
			 	{				 
			 		e.printStackTrace();
				 	JOptionPane.showMessageDialog( this,
						 "Unable to write to database",
				 	"Write Error", JOptionPane.ERROR_MESSAGE );
				 	clear();
				 	data = new Record();
				 	return;
			 	}
			
				 setVisible( false );
				 clear();
		     }			
			 // cancel button
			 else if ( event.getSource() == cancelButton ) 
			 {
				try 
				{
					setIcon( true );
				}
				catch (PropertyVetoException e) 
				{
					System.exit(1);
				}
				 clear();
			 }
		
		 } 
		
		 // clear text fields
		 private void clear()
		 {
			 recordField.setText( "" );
			 toolField.setText( "" );
			 quantityField.setText( "" );
			 costField.setText( "" );
			 locationField.setText("");
		 }
		
	} 
		
	 		 // class for deleting records
		 class DeleteDialog extends JInternalFrame implements ActionListener 
		 {
			 private JTextField recordField;
			 private JLabel recordLabel;
			 private JButton cancelButton, deleteButton;
			 private Record data;
			 private String recordNumber;
			 private Statement statement;
			 
			 public DeleteDialog( Statement state )
			 {
				 super( "Delete Record", false, false, false, true );
				
				 statement = state;
				 data = new Record();
				
				 recordLabel = new JLabel( "Record Number" );
				 recordField = new JTextField( 10 );
				 deleteButton = new JButton( "Delete Record" );
				 cancelButton = new JButton( "Cancel" );
				
				 cancelButton.addActionListener( this );
				 deleteButton.addActionListener( this );
				
				 Container container = getContentPane();
				 container.setLayout( new GridLayout( 2, 2 ) );
				 container.add( recordLabel );
				 container.add( recordField );
				 container.add( deleteButton );
				 container.add( cancelButton );
				
				 setSize( 300, 80 );
				 setVisible( true );
		 }
		
		 public void actionPerformed( ActionEvent event )
		 {
			 // delete button
			 if ( event.getSource() == deleteButton ) 
			 {
				 recordNumber = recordField.getText();
				
				 if ( !recordNumber.matches("\\w{3}-\\d{3}") ) 
				 {
					 recordField.setText( "Invalid part number" );
					 return;
				 }
				
				 try 
				 {
					 data.read(statement, recordNumber);
				 }				
				 catch ( SQLException ex ) 
				 {
					 recordField.setText( "Error reading database" );
				 }
				 
				 if ( data.getRecordNumber() == "0" ) 
				 {
					 recordField.setText(recordNumber + " does not exist");
					 return;
				 }
				
				 try 
				 {
					 String deleteRecord = "delete from [Car_Parts].[dbo].[Available_Parts] where Part_Number = " + "'" + data.getRecordNumber() + "'";
					 statement.executeUpdate(deleteRecord);
				 }
				 catch (SQLException e)
				 {
					 e.printStackTrace();
					 JOptionPane.showMessageDialog( this,
					 "Error while deleting from database",
					 "Write Error", JOptionPane.ERROR_MESSAGE );
					 return;
				 }
				
				 setVisible( false );
				 recordField.setText( "" );
			 }
			 // cancel button
			 else if ( event.getSource() == cancelButton ) 
			 {
				try 
				{
					setIcon( true );
				}
				catch (PropertyVetoException e)
				{
					System.exit(1);
				}
				 recordField.setText( "" );
			 }
		
		 } 
		
	 } 
		 
	 // class for displaying records
	 class InventoryDialog extends JInternalFrame 
	 {	
		 private JTextArea currentDisplayArea;
		 private JButton closeButton;
		 private JScrollPane scroller;
		 private Statement statement;
		 private JPanel southPanel;
		 private JPanel pagePanel;
		 private ArrayList<JTextArea> displayAreas;
		 private ArrayList<JButton> pageButtons;
				 
		 public InventoryDialog( Statement state )
		 {
			 super( "Inventory" );
			 statement = state;
			 			 
			 southPanel = new JPanel();
			 southPanel.setLayout(new BorderLayout());
			 
			 pagePanel  = new JPanel();
			 pagePanel.setLayout(new FlowLayout());
			 
			 displayAreas = new ArrayList<>();
			 pageButtons = new ArrayList<>();
			 
			 createTextArea();
			 addPageButton(this);
			 
			 closeButton = new JButton( "Close" );
			 closeButton.addActionListener(
			
					 new ActionListener() 
					 { 
			
						 public void actionPerformed( ActionEvent event )
						 {
							 setVisible( false );
							 dispose();
						 }
				
					 } 
			
			 ); 
			 
			 setLayout( new BorderLayout() );
			 southPanel.add(closeButton, BorderLayout.SOUTH);
			 southPanel.add(pagePanel, BorderLayout.NORTH);
			 
			 Container container = getContentPane();
			 container.setLayout( new BorderLayout() );
			 container.add( scroller, BorderLayout.CENTER );
			 container.add( southPanel,BorderLayout.SOUTH );
			 			 			 
			 setSize( 450, 250 );
			 setVisible( false );
		 }
		
		 public void createTextArea()
		 {			  
			  JTextArea displayArea = new JTextArea( 325, 350 );
			  displayArea.setText( "Number\tRecord #\tTool Name\tQuantity\tCost\tLocation\n" );
			  displayArea.setEditable( false );
			  displayAreas.add(displayArea);
			  
			  if(scroller == null)
			  {
				  currentDisplayArea = displayArea;
				  scroller = new JScrollPane( currentDisplayArea );
				  this.add(scroller,BorderLayout.CENTER);				  
			  }

		 }
		 
		 // display tool inventory
		 public void display()
		 {			 
			
			 try 
			 {
				 String query = "Select * from [Car_Parts].[dbo].[Available_Parts]";
				 ResultSet rowSet = statement.executeQuery(query);	
				 int counter = 1;
				 int recordsPerPage = 2;
				
				 while(rowSet.next())
				 {		

					 if(counter%recordsPerPage == 1 && counter != 1)
					 {
						  createTextArea();
						  addPageButton(this);						 
					 }
					 displayAreas.get( (counter - 1) / recordsPerPage ).append( counter + "\t" + rowSet.getString("Part_Number") + "\t" +
							 rowSet.getString("Part_Name") + "\t" + rowSet.getInt("Quantity") + "\t" +
							 rowSet.getDouble("Cost") +"\t" + rowSet.getString("Location") + "\n" );
		 
					 counter++;					 					 
				 }
				 
				 for(JButton button: pageButtons)
				 {
					pagePanel.add(button); 
				 }
			 }
			 // error reading from db
			 catch (SQLException e) 
			 {
				 e.printStackTrace();
				 JOptionPane.showMessageDialog( this,
				 "Error during read from database",
				 "Read Error", JOptionPane.ERROR_MESSAGE );
			 }
		 }
		 
		 public void addPageButton(final InventoryDialog frame)
		 {
			 int p = displayAreas.size();
			 String pageNumber = p +"";
			 JButton newButton = new JButton( pageNumber );
			 newButton.addActionListener(
					 
					 new ActionListener() 
					 { 			
						 public void actionPerformed( ActionEvent event )
						 {
							 frame.remove(scroller);
							 JButton sourceButton = (JButton) event.getSource();
							 int sourceIndex = pageButtons.indexOf(sourceButton);
							 currentDisplayArea = displayAreas.get(sourceIndex);
							 scroller = new JScrollPane( currentDisplayArea );
							 scroller.repaint();
							 frame.add(scroller,BorderLayout.CENTER);
							 revalidate();							 						 
						 }			
					 } 
					 
					 );
			 pageButtons.add(newButton);
		 }
	 } 

