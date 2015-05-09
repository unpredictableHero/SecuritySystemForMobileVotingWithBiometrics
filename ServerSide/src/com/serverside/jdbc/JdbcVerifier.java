package com.serverside.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcVerifier implements IJdbcVerifier {

	private JdbcConn jdbcConn;
	
	@Override
	public int verifyCnp(String id, String cnp) {
		jdbcConn = new JdbcConn();
		jdbcConn.verifyDriver();
		Connection conn = jdbcConn.getConn();
		String sql = "select cnp from people where id = ?";
		String sqlCnp = "";
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, id);
			ResultSet result = prepStmt.executeQuery();
			if(result.next()) {
				sqlCnp = result.getString("CNP");				
			}
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		jdbcConn.closeConn(conn);
		if(cnp.equals(sqlCnp)) {
			return 1;
		}
		
		return 0;
	}
	
	public int castVote(String vote) {
		jdbcConn = new JdbcConn();
		jdbcConn.verifyDriver();
		Connection conn = jdbcConn.getConn();
		Statement stm = null;
		String insertSql = "INSERT INTO CASTTABLE" + "(VOTE)" + "VALUES" + 
		"(" + Integer.parseInt(vote) + ")"; 
		try {
			stm = conn.createStatement();
			stm.executeUpdate(insertSql);
			
		} catch(SQLException e) {
			e.printStackTrace();
		} jdbcConn.closeConn(conn);
		
		return 1;
	}

}
