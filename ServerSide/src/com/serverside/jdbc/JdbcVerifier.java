package com.serverside.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.serverside.Crypto;

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
	
	
	public int verifyAnswers(String id, String ans1, String ans2){
		jdbcConn = new JdbcConn();
		jdbcConn.verifyDriver();
		Connection conn = jdbcConn.getConn();
		String sql = "select Answer1, Answer2 from people where id = ?";
		String ans1Db = "";
		String ans2Db = "";
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, id);
			ResultSet result = prepStmt.executeQuery(); 
			if(result.next()) {
				ans1Db = result.getString("Answer1");		
				ans2Db = result.getString("Answer2");
			}
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		jdbcConn.closeConn(conn);
		if(ans1.equals(ans1Db) && ans2.equals(ans2Db)) {
			return 1;
		}
		
		return 0;
	}
	public int castVote(String vote, String key) {
	
		Crypto crypto = new Crypto();
		//String value = crypto.decrypt(vote, key);
		String value = crypto.fakeDecrypt(vote, key);
		jdbcConn = new JdbcConn();
		jdbcConn.verifyDriver();
		Connection conn = jdbcConn.getConn();
		Statement stm = null;
		String insertSql = "INSERT INTO CASTTABLE" + "(VOTE)" + "VALUES" + 
		"(" + Integer.parseInt(value) + ")"; 
		try {
			stm = conn.createStatement();
			stm.executeUpdate(insertSql);
			
		} catch(SQLException e) {
			e.printStackTrace();
		} jdbcConn.closeConn(conn);
		
		System.out.println("Vote was succesfully casted");
		return 1;
	}
	
	public String results() {
		jdbcConn = new JdbcConn();
		jdbcConn.verifyDriver();
		Connection conn = jdbcConn.getConn();
		Statement stm = null;
		String sql ="select COUNT(Vote) from casttable where Vote = 1";
		String sql2 ="select COUNT(Vote) from casttable where Vote = 2";
		try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int vote1 = 0;
		int vote2 = 0;
		try {
			ResultSet result = stm.executeQuery(sql);
			if(result.next()) {
				vote1  = result.getInt(1);				
			}
			result = stm.executeQuery(sql2);
			if(result.next()) {
				vote2  = result.getInt(1);				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Candidate 1 have " + vote1 + "votes and Candidate 2 have " + vote2 + "votes";
	}
	
	public void updateImei(String id, String cnp) {
		jdbcConn = new JdbcConn();
		jdbcConn.verifyDriver();
		Connection conn = jdbcConn.getConn();
		String sql = "UPDATE people SET id = ? where cnp = ?";
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, id);
			prepStmt.setString(2, cnp);
			prepStmt.executeUpdate(); 
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		jdbcConn.closeConn(conn);
	System.out.println("Imei was succesfully changed ");
	}

}
