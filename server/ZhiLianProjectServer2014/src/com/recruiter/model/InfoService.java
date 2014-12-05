package com.recruiter.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import com.recruiter.bean.Detail;
import com.recruiter.bean.Info;
import com.recruiter.bean.LbsDataEditInfo;
import com.zhilian.db.DBConnection;

public class InfoService {
	private Connection connection;
	private PreparedStatement prst;
	private ResultSet resultset;
	
	public Info getInfo(String username) {
		Info info = new Info();
		
		connection = DBConnection.getConnection();
		String sql = "select * from recruiter_userinfo where username=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, username);
			
			resultset = prst.executeQuery();
			
			if(resultset.next()) {
				info.setName(resultset.getNString("name"));
				info.setAddress(resultset.getNString("address"));
				info.setLocation(resultset.getNString("location"));
				info.setEmail(resultset.getNString("email"));
				info.setDetail(resultset.getNString("detail"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return info;
	}
	
	public String getEmail(String recruitername) {
		String email = "";
		connection = DBConnection.getConnection();
		String sql = "select email from recruiter_userinfo where name=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, recruitername);
			
			resultset = prst.executeQuery();
			
			if(resultset.next()) {
				email = resultset.getNString("email");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
		return email;
	}
	
	public Detail getDetail(String recruitername) {
		Detail detail = new Detail();
		
		connection = DBConnection.getConnection();
		String sql = "select email,detail from recruiter_userinfo where name=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, recruitername);
			
			resultset = prst.executeQuery();
			
			if(resultset.next()) {
				detail.setEmail(resultset.getNString("email"));
				detail.setDetail(resultset.getNString("detail"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return detail;
	}
	
	public boolean editInfo(Info info) {
		boolean flag = true;
		// 修改lbs数据 如果成功 再修改数据库数据
		ArrayList<String> list_id_cloud = new DemandService().getDemandIdCloud(info.getUsername());
		for (String id_cloud : list_id_cloud) {
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod(Config.URL_LBS_AMAP_DATA_UPDATE);
			postMethod.addParameter("key", Config.LBS_AMAP_KEY);
			postMethod.addParameter("tableid", Config.LBS_AMAP_TABLEID);
			
			LbsDataEditInfo data = new LbsDataEditInfo();
			data.set_name(info.getName());
			data.set_location(info.getLocation());
			data.set_address(info.getAddress());
			data.set_id(id_cloud);
			JSONObject jsonData = JSONObject.fromObject(data);
			postMethod.addParameter("data", jsonData.toString());
			
			postMethod.setRequestHeader("Content-Type", "text/html;charset=utf-8");
			
			int statusCode = 0;
			try {
				statusCode = httpClient.executeMethod(postMethod);
				if (statusCode == HttpStatus.SC_OK) {
					String responseBody = postMethod.getResponseBodyAsString();  
					
					JSONObject jsonObject = JSONObject.fromObject(responseBody);
					if (jsonObject.optString("status").equals("1")) {
						flag = flag && true;
					} else if (jsonObject.optString("status").equals("0")) {
						flag = flag && false;
						System.out.println("LBS修改请求失败 错误码 "+jsonObject.optString("info"));
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				postMethod.releaseConnection();
			}
		}
		
		if (flag == true) {
			connection = DBConnection.getConnection();
			String sql = "update recruiter_userinfo "
					+ "set name=?,address=?,location=?,email=?,detail=? "
					+ "where username=?";
			
			try {
				prst = connection.prepareStatement(sql);
				prst.setString(1, info.getName());
				prst.setString(2, info.getAddress());
				prst.setString(3, info.getLocation());
				prst.setString(4, info.getEmail());
				prst.setString(5, info.getDetail());
				prst.setString(6, info.getUsername());
				
				int i = prst.executeUpdate();
				if (i == 1) {
					flag = flag && true;
				} else {
					flag = flag && false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				close();
			}
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
