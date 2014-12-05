package com.jobhunter.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jobhunter.bean.Applicationhistory;
import com.zhilian.db.DBConnection;

public class ApplicationhistoryService {

	private Connection connection;
	private PreparedStatement prst;
	private ResultSet resultset;
	
	public ArrayList<Applicationhistory> getApplicationhistory(String userphone) {
		ArrayList<Applicationhistory> list = new ArrayList<Applicationhistory>();
		Applicationhistory applicationhistory = null;
		
		connection = DBConnection.getConnection();
		String sql = "select * from jobhunter_applicationhistory where userphone=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, userphone);
			
			resultset = prst.executeQuery();
			
			while(resultset.next()) {
				applicationhistory = new Applicationhistory();
				applicationhistory.setApplication_recruitername(resultset.getNString("application_recruitername"));
				applicationhistory.setId_cloud(resultset.getNString("id_cloud"));
				applicationhistory.setApplication_jobname(resultset.getNString("application_jobname"));
		        applicationhistory.setApplication_salary(resultset.getNString("application_salary"));
		        applicationhistory.setApplication_time_hour(resultset.getNString("application_time_hour"));
		        applicationhistory.setApplication_time_day(resultset.getNString("application_time_day"));
				list.add(applicationhistory);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}
	
	public boolean addApplicationhistory(Applicationhistory applicationhistory) {
		boolean flag = false;
		
		connection = DBConnection.getConnection();
		String sql = "insert into jobhunter_applicationhistory"
				+ "(userphone,application_recruitername,id_cloud,application_jobname,application_salary,application_time_hour,application_time_day) "
				+ "values (?,?,?,?,?,?,?)";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, applicationhistory.getUserphone());
			prst.setString(2, applicationhistory.getApplication_recruitername());
			prst.setString(3, applicationhistory.getId_cloud());
			prst.setString(4, applicationhistory.getApplication_jobname());
			prst.setString(5, applicationhistory.getApplication_salary());
			prst.setString(6, applicationhistory.getApplication_time_hour());
			prst.setString(7, applicationhistory.getApplication_time_day());
			
			int row = prst.executeUpdate();
			
			if (row == 1) {
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
