	package parts;
	
	import java.io.*;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;

	public class Record 
 	{
		private String recordNumber;
		private int quantity;
		private String toolName;
		private double cost;
		private String location;

		
		public Record()
		{
			this( "0", "", 0, 0, "0" );
		}
		
		public Record( String record, String name, int q, double c ,String loc)
		{
			setRecordNumber( record );
			setToolName( name );
			setQuantity( q );
			setCost( c );
			setLocation(loc);
			
		}
		
		// read a record from the database
		public void read(Statement statement, String recordNumber) throws SQLException
		{
			String query = "select * from [Car_Parts].[dbo].[Available_Parts] where Part_Number = " + "'" + recordNumber + "'";				
			ResultSet rowSet = statement.executeQuery(query);
			if(rowSet.next())
			{
				setRecordNumber( rowSet.getString("Part_Number") );
				setToolName( rowSet.getString("Part_Name") );
				setQuantity( rowSet.getInt("Quantity") );
				setCost( rowSet.getDouble("Cost") );
				setLocation( rowSet.getString("Location") );
			}
						
		}
		
		// write a record to the specified RandomAccessFile
		public void write( Statement statement, String description ) throws IOException, SQLException
		{
			String sqlStatement = null;
			if(description.equals("create"))
			{
				sqlStatement = "Insert into [Car_Parts].[dbo].[Available_Parts] " +
						"values " + "(" + "'" + getRecordNumber()  + "'" + ", " + "'" + getToolName()  + "'" + ", " + "'" + getQuantity()  + "'" + ", " +
						 getCost() + ", "  + "'" + getLocation() + "'" + " )";
			}
			else 
			{
				sqlStatement = "Update [Car_Parts].[dbo].[Available_Parts]" +
								" set Part_Number = " + "'" + description + "'," + 
								" Part_Name = " + "'" + getToolName() + "'," +
								" Quantity = " + getQuantity() + "," +
								" Cost = " + getCost() + "," +
								" Location = " + "'" + getLocation() + "'" + 
								" where Part_Number = " + "'" + getRecordNumber() + "'";				
			}
					
			statement.executeUpdate(sqlStatement);
		}
		
		// accessor methods
		public void setRecordNumber( String n )
		{
			recordNumber = n;
		}
		
		public String getRecordNumber()
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
		
		public String getLocation()
		{
			return location;
		}
		
		public void setLocation(String location)
		{
			this.location = location;
		}
 } 
