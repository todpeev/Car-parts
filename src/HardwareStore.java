
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
	
public class HardwareStore extends JFrame {
	
	private UpdateDialog updateDialog;
	private NewDialog newDialog;
	private DeleteDialog deleteDialog;
	private InventoryDialog inventoryDialog;
	private JMenuItem newItem, updateItem, deleteItem,
	inventoryItem, exitItem;
	private JDesktopPane desktop;
	private RandomAccessFile file;
	private Record records[];
	
	public HardwareStore()
	{
		super( "Hardware Store" );
		records = new Record[ 100 ];
		// open file
		try {
			file = new RandomAccessFile( "hardware.dat", "rw" );
			for ( int i = 0; i < 100; i++ ) 
			{
			records[ i ] = new Record();
			records[ i ].write( file );
			}
		}
		catch ( IOException ioException ) 
		{
			System.err.println( ioException.toString() );
			System.exit( 1 );
		}
		
		newDialog = new NewDialog( file );
		updateDialog = new UpdateDialog( file );
		deleteDialog = new DeleteDialog( file );
		inventoryDialog = new InventoryDialog( file );
		// set up GUI
		Container container = getContentPane();
		desktop = new JDesktopPane();
		container.add( desktop );
		desktop.add( newDialog );
		desktop.add( updateDialog );
		desktop.add( deleteDialog );
		desktop.add( inventoryDialog );
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar( menuBar );
		JMenu fileMenu = new JMenu( "File" );
		menuBar.add( fileMenu );
		newItem = new JMenuItem( "New Record" );
		newItem.addActionListener(
				new ActionListener() { // anonymous inner class
				public void actionPerformed( ActionEvent event )
				{
				newDialog.setVisible( true );
				}
				} // end anonymous inner class
				); // end call to addActionListener
		
		updateItem = new JMenuItem( "Update Record" );
		updateItem.addActionListener(
			new ActionListener() 
			{ // anonymous inner class
				public void actionPerformed( ActionEvent event )
				{
					updateDialog.setVisible( true );
				}
			} // end anonymous inner class
		); // end call to addActionListener
		
		deleteItem = new JMenuItem( "Delete Record" );
		deleteItem.addActionListener(
			new ActionListener() 
			{ // anonymous inner class
				public void actionPerformed( ActionEvent event )
				{
					deleteDialog.setVisible( true );
				}
			} // end anonymous inner class
		); // end call to addActionListener
			
		 inventoryItem = new JMenuItem( "Show Inventory" );
		 inventoryItem.addActionListener(
	
			 new ActionListener() 
			 { // anonymous inner class
			
				 public void actionPerformed( ActionEvent event )
				 {
				 	inventoryDialog.setVisible( true );
				 	inventoryDialog.display();
				 }
			
			 } // end anonymous inner class
		); // end call to addActionListener
	
		 exitItem = new JMenuItem( "Exit" );
		 exitItem.addActionListener(
	
				 new ActionListener() 
				 { // anonymous inner class
	
					 public void actionPerformed( ActionEvent event )
					 {
						 try {
							 file.close();
						 }
						 catch ( IOException ioException ) {
							 System.exit( 1 );
						 }
				
						 System.exit( 0 );
					 }
				
				 } // end anonymous inner class
	
			); // end call to addActionListener
	
		 fileMenu.add( newItem );
		 fileMenu.add( updateItem );
		 fileMenu.add( deleteItem );
		 fileMenu.addSeparator();
		 fileMenu.add( inventoryItem );
		 fileMenu.addSeparator();
		 fileMenu.add( exitItem );
		
		 setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
		
		 setSize( 370, 330 );
		 setVisible( true );
	
	 } // end constructor
	
	 public static void main( String args[] )
	 {
		 new HardwareStore();
	 }
	
	 } // end class HardwareStore


	
	 // class for updating records
	 class UpdateDialog extends JInternalFrame implements ActionListener
	 {
	
		 private RandomAccessFile file;
		 private JTextField recordField, toolField, quantityField, costField;
		 private JLabel recordLabel, toolLabel, quantityLabel, costLabel;
		 private JButton saveButton, cancelButton;
		 private Record data;
		 private int recordNumber;
		
		 public UpdateDialog( RandomAccessFile updateFile )
		 {
			 super( "Update Record" );
			
			 file = updateFile;
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
			
			 saveButton = new JButton( "Save Changes" );
			 cancelButton = new JButton( "Cancel" );
			
			 recordField.addActionListener( this );
			 saveButton.addActionListener( this );
			 cancelButton.addActionListener( this );
			
			 Container container = getContentPane();
			 container.setLayout( new GridLayout( 5, 2 ) );
			 container.add( recordLabel );
			 container.add( recordField );
			 container.add( toolLabel );
			 container.add( toolField );
			 container.add( quantityLabel );
			 container.add( quantityField );
			 container.add( costLabel );
			 container.add( costField );
			 container.add( saveButton );
			 container.add( cancelButton );
			
			 setSize( 300, 150 );
			 setVisible( false );
		 }
		
		 // process events
		 public void actionPerformed( ActionEvent event )
		 {
			 // text field
			 if ( event.getSource() == recordField ) 
			 {
				 recordNumber = Integer.parseInt( recordField.getText() );
				 if ( recordNumber < 1 || recordNumber > 100 ) 
				 {
					 JOptionPane.showMessageDialog( this, "Invalid record number",
					 "Account Error", JOptionPane.ERROR_MESSAGE );
					 clear();
					 return;
				 }
		
				 // set file-pointer to appropriate location
				 try 
				 {
					 file.seek( ( recordNumber - 1 ) * data.SIZE );
					 data.read( file );
				 }	
				 catch ( IOException ioException ) 
				 {
					 JOptionPane.showMessageDialog( this,
					 "Error while reading from file",
					 "Read Error", JOptionPane.ERROR_MESSAGE );
				 }
		
				 // retrieve current record information
				 if ( data.getRecordNumber() != 0 ) 
				 {
					 toolField.setText( data.getToolName() );
					 toolField.setEditable( true );
					 quantityField.setText(
					 Integer.toString( data.getQuantity() ) );
					 quantityField.setEditable( true );
					 costField.setText( String.valueOf( data.getCost() ) );
					 costField.setEditable( true );
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
					 file.seek( ( recordNumber - 1 ) * data.SIZE );
					 data.setRecordNumber( recordNumber );
					 data.setToolName( toolField.getText() );
					 data.setQuantity(
					 Integer.parseInt( quantityField.getText() ) );
					 data.setCost( ( Double.valueOf(
					 costField.getText() ) ).doubleValue() );
					 data.write( file );
				 }
				 catch ( IOException ioException ) 
				 {
					 JOptionPane.showMessageDialog( this,
					 "Error while writing to file",
					 "Write Error", JOptionPane.ERROR_MESSAGE );
					 return;
				 }
			
				 setVisible( false );
				 clear();
			 }		
			 // cancel button
			 else if ( event.getSource() == cancelButton ) 
			 {
				 setVisible( false );
				 clear();
			 }
		
		 } // end method actionPerformed
		
		 // clear text fields
		 private void clear()
		 {
			 recordField.setText( "" );
			 toolField.setText( "" );
			 quantityField.setText( "" );
			 costField.setText( "" );
		 }
	
	 } // end class UpdateDialog
	
	 // class for creating new records
	 class NewDialog extends JInternalFrame implements ActionListener {
	
		 private RandomAccessFile file;
		 private JTextField recordField, toolField, quantityField, costField;
		 private JLabel recordLabel, toolLabel, quantityLabel, costLabel;
		 private JButton saveButton, cancelButton;
		 private Record data;
		 private int recordNumber;
	
	 public NewDialog( RandomAccessFile newFile )
	 {
		 super( "New Record" );
		
		 file = newFile;
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
		
		 saveButton = new JButton( "Save Changes" );
		 cancelButton = new JButton( "Cancel" );
		
		 saveButton.addActionListener( this );
		 cancelButton.addActionListener( this );
		
		 Container container = getContentPane();
		 container.setLayout( new GridLayout( 5, 2 ) );
		 container.add( recordLabel );
		 container.add( recordField );
		 container.add( toolLabel );
		 container.add( toolField );
		 container.add( quantityLabel );
		 container.add( quantityField );
		 container.add( costLabel );
		 container.add( costField );
		 container.add( saveButton );
		 container.add( cancelButton );
		
		 setSize( 300, 150 );
		 setVisible( false );
	 }
	
	 // process events
	 public void actionPerformed( ActionEvent event )
	 {
		 // save button
		 if ( event.getSource() == saveButton ) 
		 {
		
			 recordNumber = Integer.parseInt( recordField.getText() );
			
			 // check if record number is within range
			 if ( recordNumber < 1 || recordNumber > 100 )
			 {
				 JOptionPane.showMessageDialog( this, "Invalid record number",
				 "Account Error", JOptionPane.ERROR_MESSAGE );
				 clear();
				 return;
			 }
	
		 // set file-pointer to appropriate location
			 try 
			 {
				 file.seek( ( recordNumber - 1 ) * data.SIZE );
				 data.read( file );
			 }
			 catch ( IOException ioException ) 
			 {
				 JOptionPane.showMessageDialog( this,
				 "Error while reading from file",
				 "Read Error", JOptionPane.ERROR_MESSAGE );
			 }
			 
			 // record number already exists
			 if ( data.getRecordNumber() != 0 ) 
			 {
				 JOptionPane.showMessageDialog( this,
				 "Record number already exists",
				 "Duplicate record number", JOptionPane.ERROR_MESSAGE );
				 clear();
				 return;
			 }
			
			 // set values of new record and write to file
			 try 
			 {
				 data.setRecordNumber( recordNumber );
				 data.setToolName( toolField.getText() );
				 data.setQuantity(
				 Integer.parseInt( quantityField.getText() ) );
				 data.setCost( ( new Double(
				 costField.getText() ) ).doubleValue() );
				 file.seek( ( recordNumber - 1 ) * data.SIZE );
				 data.write( file );
			 }
			 catch ( IOException ioException ) 
			 {
				 JOptionPane.showMessageDialog( this,
				 "Error while writing to file",
				 "Write Error", JOptionPane.ERROR_MESSAGE );
				 return;
			 }
			
				 setVisible( false );
				 clear();
			 }
			
			 // cancel button
			 else if ( event.getSource() == cancelButton ) 
			 {
				 setVisible( false );
				 clear();
			 }
		
		 } // end method actionPerformed
		
		 // clear text fields
		 private void clear()
		 {
			 recordField.setText( "" );
			 toolField.setText( "" );
			 quantityField.setText( "" );
			 costField.setText( "" );
		 }
		
	} // end class NewDialog
		
	 
	 
	 
	 
		 // class for deleting records
		 class DeleteDialog extends JInternalFrame implements ActionListener 
		 {
			 private RandomAccessFile file;
			 private JTextField recordField;
			 private JLabel recordLabel;
			 private JButton cancelButton, deleteButton;
			 private Record data;
			 private int recordNumber;
		
			 public DeleteDialog( RandomAccessFile deleteFile )
			 {
				 super( "Delete Record" );
				
				 file = deleteFile;
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
				 setVisible( false );
		 }
		
		 // process events
		 public void actionPerformed( ActionEvent event )
		 {
			 // delete button
			 if ( event.getSource() == deleteButton ) 
			 {
				 recordNumber =
				 Integer.parseInt( recordField.getText() );
				
				 if ( recordNumber < 1 || recordNumber > 100 ) 
				 {
					 recordField.setText( "Invalid part number" );
					 return;
				 }
				
				 try 
				 {
					 file.seek( ( recordNumber - 1 ) * data.SIZE );
					 data.read( file );
				 }				
				 catch ( IOException ioException ) 
				 {
					 recordField.setText( "Error reading file" );
				 }
				 
				 if ( data.getRecordNumber() == 0 ) 
				 {
					 recordField.setText( recordNumber + " does not exist" );
					 return;
				 }
				
				 try 
				 {
					 file.seek( ( recordNumber - 1 ) * data.SIZE );
					 data.setRecordNumber( 0 );
					 data.setToolName( "" );
					 data.setQuantity( 0 );
					 data.setCost( 0.0 );
					 data.write( file );
				 }
				
				 catch ( IOException ioException ) 
				 {
					 JOptionPane.showMessageDialog( this,
					 "Error while writing to file",
					 "Write Error", JOptionPane.ERROR_MESSAGE );
					 return;
				 }
				
				 setVisible( false );
				 recordField.setText( "" );
			 }
			 // cancel button
			 else if ( event.getSource() == cancelButton ) 
			 {
				 setVisible( false );
				 recordField.setText( "" );
			 }
		
		 } // end method actionPerformed
		
	 } // end class DeleteDialog
	
		 
		 
	 // class for displaying records
	 class InventoryDialog extends JInternalFrame 
	 {
	
		 private RandomAccessFile file;
		 private JTextArea displayArea;
		 private JButton closeButton;
		 private JScrollPane scroller;
		 private Record data;
		
		 public InventoryDialog( RandomAccessFile deleteFile )
		 {
			 super( "Inventory" );
			
			 file = deleteFile;
			 data = new Record();
			
			 displayArea = new JTextArea( 300, 200 );
			 displayArea.setEditable( false );
			 scroller = new JScrollPane( displayArea );
			
			 closeButton = new JButton( "Close" );
			 closeButton.addActionListener(
			
					 new ActionListener() 
					 { // anonymous inner class
			
						 public void actionPerformed( ActionEvent event )
						 {
							 setVisible( false );
						 }
				
					 } // end anonymous inner class
			
			 ); // end call to addActionListener
			
			 Container container = getContentPane();
			 container.setLayout( new BorderLayout() );
			 container.add( scroller, BorderLayout.CENTER );
			 container.add( closeButton, BorderLayout.SOUTH );
			
			 setSize( 350, 275 );
			 setVisible( false );
		 }
		
		 // display tool inventory
		 public void display()
		 {
			 displayArea.setText( "Record #\tTool Name\tQuantity\tCost\n" );
			
			 try 
			 {
				 for ( int i = 0; i < 100; i++ ) 
				 {
				
					 file.seek( ( i ) * data.SIZE );
					 data.read( file );
					
					 if ( data.getRecordNumber() != 0 )
						 	displayArea.append( data.getRecordNumber() + "\t" +
							data.getToolName() + "\t" + data.getQuantity() + "\t" +
							data.getCost() + "\n" );
				 }
			 }
			 // error reading from file
			 catch ( IOException ioException ) 
			 {
				 JOptionPane.showMessageDialog( this,
				 "Error during read from file",
				 "Read Error", JOptionPane.ERROR_MESSAGE );
			 }
		 }

	 } // end class InventoryDialog

