package Utilities;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.mysql.jdbc.Driver;



public class DButil {


	private Connection SQL_con;
	private Statement SQL_stmt;
	private String host;
	private String port;
	private String dbname;
	private String username;
	private String password;
	
	public DButil(String host, String port, String dbname, String username, String password)
	{
		this.host = host;
		this.port = port;
		this.dbname = dbname;
		this.username = username;
		this.password = password;
	}
	
	public List<HashMap<String, Object>> executeQuery(String query) throws SQLException, ClassNotFoundException
	{

		createConnectionAndStatement();
		
		ResultSet rs = SQL_stmt.executeQuery(query);
		System.out.println("Query executed");
		
		List<HashMap<String, Object>> retlst = convertResultSetToList(rs);
		closeConnection();
	
		return retlst;
	}
	
	public int executeUpdate(String Updatequery) throws SQLException, ClassNotFoundException
	{
		createConnectionAndStatement();
		
		int rowsupdated = SQL_stmt.executeUpdate(Updatequery);
		System.out.println("Update Query executed");
		
		closeConnection();
		return rowsupdated;
	}
	
	private void createConnectionAndStatement() throws SQLException, ClassNotFoundException
	{
		String dbClass ="com.mysql.jdbc.Driver";
		Class.forName(dbClass);
				
		SQL_con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+dbname, username, password);		
		System.out.println ("Database connection established");
		
		SQL_stmt = SQL_con.createStatement();
		System.out.println("Database Statement Created");
	}

	private void closeConnection() throws SQLException
	{
		if(SQL_stmt != null && !SQL_stmt.isClosed())	
		{
			System.out.println("Closing Database Statement");
			SQL_stmt.close();
		}
		
		if(SQL_con != null && !SQL_con.isClosed())
		{
			System.out.println("Closing Database Connection");
			SQL_con.close();			
		}
		
	}
	private static List<HashMap<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		while (rs.next()) {
			HashMap<String, Object> row = new HashMap<String, Object>(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException 
	{		
		String dbClass ="com.mysql.jdbc.Driver";
		Class.forName(dbClass);				
		
		Connection SQL_con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdatadb", "maqdoom", "qatest");		
		
		System.out.println("hi");
		
		Statement SQL_stmt = SQL_con.createStatement();
		
		String query ="select * from pricecompare;";
		ResultSet rs = SQL_stmt.executeQuery(query );
		
		List<HashMap<String, Object>> rsList = convertResultSetToList(rs);
		
		System.out.println(rsList.size());		
		
		SQL_stmt.close();
		SQL_con.close();
	}
}
