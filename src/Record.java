	import java.io.*;

	public class Record 
 	{
 		public final int SIZE = 46;
		private int recordNumber, quantity;
		private String toolName;
		private double cost;
		
		public Record()
		{
			this( 0, "", 0, 0 );
		}
		
		public Record( int record, String name, int q, double c )
		{
			setRecordNumber( record );
			setToolName( name );
			setQuantity( q );
			setCost( c );
		}
		
		// read a record from the specified RandomAccessFile
		public void read( RandomAccessFile file ) throws IOException
		{
			setRecordNumber( file.readInt() );
			setToolName( padName( file ) );
			setQuantity( file.readInt() );
			setCost( file.readDouble() );
		}
		
		// ensure that name is proper length
		private String padName( RandomAccessFile file ) throws IOException
		{
			char name[] = new char[ 15 ];
			for ( int i = 0; i < name.length; i++ )
			name[ i ] = file.readChar();
			return new String( name ).replace( '\0', ' ' );
		}
		
		// write a record to the specified RandomAccessFile
		public void write( RandomAccessFile file ) throws IOException
		{
			file.writeInt( getRecordNumber() );
			StringBuffer buffer;
			if ( toolName != null )
				buffer = new StringBuffer( toolName );
			else
				buffer = new StringBuffer( 15 );
			
			buffer.setLength( 15 );
			file.writeChars( buffer.toString() );
			file.writeInt( getQuantity() );
			file.writeDouble( getCost() );
		}
		
		// accessor methods
		public void setRecordNumber( int n )
		{
			recordNumber = n;
		}
		
		public int getRecordNumber()
		{
			return recordNumber;
		}
		
		public void setToolName( String t )
		{
			toolName = t;
		}
		
		public String getToolName()
		{
			return toolName;
		}
		
		public void setQuantity( int q )
		{
			quantity = q;
		}
		
		public int getQuantity()
		{
			return quantity;
		}
		
		public void setCost( double c )
		{
			cost = c;
		}
		
		public double getCost()
		{
			return cost;
		}
 } 
