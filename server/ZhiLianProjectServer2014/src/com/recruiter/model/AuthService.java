package com.recruiter.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhilian.db.DBConnection;

public class AuthService {
	
	private Connection connection;
	private PreparedStatement prst;
	private ResultSet resultset;

	public boolean login(String username, String userpassword) {
		boolean flag = false;
		
		connection = DBConnection.getConnection();
		String sql = "select username,userpassword from recruiter_auth where username=? and userpassword=?";
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, username);
			prst.setString(2, userpassword);
			
			resultset = prst.executeQuery();
			if(resultset.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return flag;
	}
	
	public boolean register(String username, String userpassword) {
		boolean flag = false;
		
		connection = DBConnection.getConnection();
		String sql = "select username from recruiter_auth where username=?";
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, username);
			resultset = prst.executeQuery();
			if(resultset.next()) {
				flag = false;
			} else {
				sql = "insert into recruiter_auth(username,userpassword) values (?,?)";
				prst = connection.prepareStatement(sql);
				prst.setString(1, username);
				prst.setString(2, userpassword);
				
				int row = prst.executeUpdate();
				if (row == 1) {
					sql = "insert into recruiter_userinfo"
							+ "(username,name,address,location,email,detail) "
							+ "values (?,'','',?,?,'')";
					prst = connection.prepareStatement(sql);
					prst.setString(1, username);
					prst.setString(2, "120.703961,31.271771");
					prst.setString(3, "dian6210310@126.com");
					row = prst.executeUpdate();
					if (row == 1) {
						flag = true;
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return flag;
	}
	
	private void close() {
		try{
			if(resultset != null) {
				resultset.close();
			}
			if(prst != null) {
				prst.close();
			}
			if(connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
