package com.jobhunter.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jobhunter.bean.Job;
import com.zhilian.db.DBConnection;

public class JobService {
	private Connection connection;
	private PreparedStatement prst;
	private ResultSet resultset;
	
	public ArrayList<Job> getJob() {
		ArrayList<Job> list = new ArrayList<Job>();
		Job job = null;
		
		connection = DBConnection.getConnection();
		String sql = "select * from job";
		
		try {
			prst = connection.prepareStatement(sql);
			resultset = prst.executeQuery();
			
			while(resultset.next()) {
				job = new Job();
				job.setField(resultset.getNString("field"));
				job.setJob(resultset.getNString("job"));
				list.add(job);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
		return list;
	}
	
	public ArrayList<String> getField() {
		ArrayList<String> list = new ArrayList<String>();
		
		connection = DBConnection.getConnection();
		String sql = "select distinct field from job";
		
		try {
			prst = connection.prepareStatement(sql);
			resultset = prst.executeQuery();
			
			while(resultset.next()) {
				list.add(resultset.getNString("field"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
		return list;
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
