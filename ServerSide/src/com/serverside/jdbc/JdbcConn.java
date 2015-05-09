package com.serverside.jdbc;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class JdbcConn {
	
	
	public void verifyDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
	 
	}
	
	
	public Connection getConn() {
		Connection conn = null;
		try {
			conn = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/project_mysql","root", "root");
	 
		} catch (SQLException e) {
			System.out.println("Connection Failed!");
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	public void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
