package com.jobhunter.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhilian.db.DBConnection;


public class AuthService {
	private Connection connection;
	private PreparedStatement prst;
	private ResultSet resultset;

	public boolean login(String userphone, String userpassword) {
		boolean flag = false;

		connection = DBConnection.getConnection();
		String sql = "select userphone,userpassword from jobhunter_auth where userphone=? and userpassword=?";
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, userphone);
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

	public boolean register(String userphone, String userpassword) {
		boolean flag = false;

		connection = DBConnection.getConnection();
		String sql = "select userphone from jobhunter_auth where userphone=?";
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, userphone);

			resultset = prst.executeQuery();
			if(resultset.next()) {
				flag = false;
			} else {
				sql = "insert into jobhunter_auth(userphone,userpassword) values (?,?)";
				prst = connection.prepareStatement(sql);
				prst.setString(1, userphone);
				prst.setString(2, userpassword);
				int row = prst.executeUpdate();

				if (row == 1) {
					sql = "insert into jobhunter_userinfo"
							+ "(username,userdemand_field,userdemand_job,userdemand_salary,userdemand_experience,userphone) "
							+ "values ('','','',?,?,?)";
					prst = connection.prepareStatement(sql);
					prst.setString(1, "0");
					prst.setString(2, "0");
					prst.setString(3, userphone);
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
