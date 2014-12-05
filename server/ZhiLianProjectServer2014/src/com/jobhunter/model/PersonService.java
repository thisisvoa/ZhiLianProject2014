package com.jobhunter.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jobhunter.bean.Person;
import com.zhilian.db.DBConnection;

public class PersonService {

	private Connection connection;
	private PreparedStatement prst;
	private ResultSet resultset;
	
	public Person getPerson(String userphone) {
		Person person = new Person();
		
		connection = DBConnection.getConnection();
		String sql = "select * from jobhunter_userinfo where userphone=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, userphone);
			
			resultset = prst.executeQuery();
			
			if(resultset.next()) {
				person.setUsername(resultset.getNString("username"));
				person.setUserdemand_field(resultset.getNString("userdemand_field"));
				person.setUserdemand_job(resultset.getNString("userdemand_job"));
				person.setUserdemand_salary(resultset.getNString("userdemand_salary"));
				person.setUserdemand_experience(resultset.getNString("userdemand_experience"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return person;
	}
	
	public boolean editPerson(Person person) {
		boolean flag = false;
		connection = DBConnection.getConnection();
		String sql = "update jobhunter_userinfo "
				+ "set username=?,userdemand_field=?,userdemand_job=?,userdemand_salary=?,userdemand_experience=? "
				+ "where userphone=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, person.getUsername());
			prst.setString(2, person.getUserdemand_field());
			prst.setString(3, person.getUserdemand_job());
			prst.setString(4, person.getUserdemand_salary());
			prst.setString(5, person.getUserdemand_experience());
			prst.setString(6, person.getUserphone());
			
			int i = prst.executeUpdate();
			if (i == 1) {
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
