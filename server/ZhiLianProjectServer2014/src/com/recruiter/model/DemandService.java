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

import com.recruiter.bean.Demand;
import com.recruiter.bean.Info;
import com.recruiter.bean.LbsDataAdd;
import com.recruiter.bean.LbsDataEdit;
import com.zhilian.db.DBConnection;

public class DemandService {
	private Connection connection;
	private PreparedStatement prst;
	private ResultSet resultset;
	
	public ArrayList<String> getDemandIdCloud(String username) {
		ArrayList<String> list = new ArrayList<String>();
		
		connection = DBConnection.getConnection();
		String sql = "select id_cloud from recruiter_demand where username=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, username);
			
			resultset = prst.executeQuery();
			
			while(resultset.next()) {
				list.add(resultset.getNString("id_cloud"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
		return list;
	}
	
	public ArrayList<Demand> getDemand(String username) {
		ArrayList<Demand> list = new ArrayList<Demand>();
		Demand demand = null;
		
		connection = DBConnection.getConnection();
		String sql = "select * from recruiter_demand where username=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, username);
			
			resultset = prst.executeQuery();
			
			while(resultset.next()) {
				demand = new Demand();
				demand.setDemand_field(resultset.getNString("demand_field"));
				demand.setDemand_job(resultset.getNString("demand_job"));
				demand.setDemand_jobname(resultset.getNString("demand_jobname"));
				demand.setDemand_salary_min(resultset.getNString("demand_salary_min"));
				demand.setDemand_salary_max(resultset.getNString("demand_salary_max"));
				demand.setDemand_experience(resultset.getNString("demand_experience"));
				demand.setDemand_detail(resultset.getNString("demand_detail"));
				demand.setId_cloud(resultset.getNString("id_cloud"));
				list.add(demand);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}
	
	public Demand getDemand(String username, String id_cloud) {
		Demand demand = new Demand();
		
		connection = DBConnection.getConnection();
		String sql = "select * from recruiter_demand where username=? and id_cloud=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, username);
			prst.setString(2, id_cloud);
			
			resultset = prst.executeQuery();
			
			if(resultset.next()) {
				demand.setDemand_field(resultset.getNString("demand_field"));
				demand.setDemand_job(resultset.getNString("demand_job"));
				demand.setDemand_jobname(resultset.getNString("demand_jobname"));
				demand.setDemand_salary_min(resultset.getNString("demand_salary_min"));
				demand.setDemand_salary_max(resultset.getNString("demand_salary_max"));
				demand.setDemand_experience(resultset.getNString("demand_experience"));
				demand.setDemand_detail(resultset.getNString("demand_detail"));
				demand.setId_cloud(id_cloud);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return demand;
	}
	
	public String getDemandDetail(String id_cloud) {
		String detail = "";
		connection = DBConnection.getConnection();
		String sql = "select demand_detail from recruiter_demand where id_cloud=?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setString(1, id_cloud);
			
			resultset = prst.executeQuery();
			
			if(resultset.next()) {
				detail = resultset.getNString("demand_detail");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return detail;
	}
	
	public boolean addDemand(Demand demand) {
		boolean flag = false;
		// 传至lbs 如果成功 获取lbs表的id 再插入数据库
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(Config.URL_LBS_AMAP_DATA_CREATE);
		postMethod.addParameter("key", Config.LBS_AMAP_KEY);
		postMethod.addParameter("tableid", Config.LBS_AMAP_TABLEID);
		
		LbsDataAdd data = new LbsDataAdd();
		Info info = new InfoService().getInfo(demand.getUsername());
		data.set_name(info.getName());
		data.set_location(info.getLocation());
		data.set_address(info.getAddress());
		data.setField(demand.getDemand_field());
		data.setJob(demand.getDemand_job());
		data.setJobname(demand.getDemand_jobname());
		data.setSalary_min(demand.getDemand_salary_min());
		data.setSalary_max(demand.getDemand_salary_max());
		data.setExperience(demand.getDemand_experience());
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
					
					demand.setId_cloud(jsonObject.optString("_id"));
					connection = DBConnection.getConnection();
					String sql = "insert into recruiter_demand"
							+ "(username,demand_field,demand_job,demand_jobname,demand_salary_min,demand_salary_max,demand_experience,demand_detail,id_cloud) "
							+ "values (?,?,?,?,?,?,?,?,?)";
					
					try {
						prst = connection.prepareStatement(sql);
						prst.setString(1, demand.getUsername());
						prst.setString(2, demand.getDemand_field());
						prst.setString(3, demand.getDemand_job());
						prst.setString(4, demand.getDemand_jobname());
						prst.setString(5, demand.getDemand_salary_min());
						prst.setString(6, demand.getDemand_salary_max());
						prst.setString(7, demand.getDemand_experience());
						prst.setString(8, demand.getDemand_detail());
						prst.setString(9, demand.getId_cloud());
						
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
				} else if (jsonObject.optString("status").equals("0")) {
					System.out.println("LBS添加请求失败 错误码 "+jsonObject.optString("info"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		
		return flag;
	}
	
	public boolean editDemand(Demand demand) {
		boolean flag = false;
		// 修改lbs数据 如果成功 再修改数据库数据
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(Config.URL_LBS_AMAP_DATA_UPDATE);
		postMethod.addParameter("key", Config.LBS_AMAP_KEY);
		postMethod.addParameter("tableid", Config.LBS_AMAP_TABLEID);
		
		LbsDataEdit data = new LbsDataEdit();
		Info info = new InfoService().getInfo(demand.getUsername());
		data.set_name(info.getName());
		data.set_location(info.getLocation());
		data.set_address(info.getAddress());
		data.setField(demand.getDemand_field());
		data.setJob(demand.getDemand_job());
		data.setJobname(demand.getDemand_jobname());
		data.setSalary_min(demand.getDemand_salary_min());
		data.setSalary_max(demand.getDemand_salary_max());
		data.setExperience(demand.getDemand_experience());
		data.set_id(demand.getId_cloud());
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
					
					connection = DBConnection.getConnection();
					String sql = "update recruiter_demand "
							+ "set demand_field=?,demand_job=?,demand_jobname=?,demand_salary_min=?,demand_salary_max=?,demand_experience=?,demand_detail=? "
							+ "where username=? and id_cloud=?";
					
					try {
						prst = connection.prepareStatement(sql);
						prst.setString(1, demand.getDemand_field());
						prst.setString(2, demand.getDemand_job());
						prst.setString(3, demand.getDemand_jobname());
						prst.setString(4, demand.getDemand_salary_min());
						prst.setString(5, demand.getDemand_salary_max());
						prst.setString(6, demand.getDemand_experience());
						prst.setString(7, demand.getDemand_detail());
						prst.setString(8, demand.getUsername());
						prst.setString(9, demand.getId_cloud());
						
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
				} else if (jsonObject.optString("status").equals("0")) {
					System.out.println("LBS修改请求失败 错误码 "+jsonObject.optString("info"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		
		return flag;
	}
	
	public boolean delDemand(String username, String id_cloud) {
		boolean flag = false;
		// 删除lbs数据 如果成功 再删除数据库数据
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(Config.URL_LBS_AMAP_DATA_DELETE);
		postMethod.addParameter("key", Config.LBS_AMAP_KEY);
		postMethod.addParameter("tableid", Config.LBS_AMAP_TABLEID);
		postMethod.addParameter("ids", id_cloud);
		
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String responseBody = postMethod.getResponseBodyAsString();  
				
				JSONObject jsonObject = JSONObject.fromObject(responseBody);
				if (jsonObject.optString("status").equals("1")) {
					if (jsonObject.optString("success").equals("1")) {
						
						connection = DBConnection.getConnection();
						String sql = "delete from recruiter_demand where username=? and id_cloud=?";
						
						try {
							prst = connection.prepareStatement(sql);
							prst.setString(1, username);
							prst.setString(2, id_cloud);
							int row = prst.executeUpdate();
							if(row == 1) {
								flag = true;
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							close();
						}
					}
				} else if (jsonObject.optString("status").equals("0")) {
					System.out.println("LBS删除请求失败 错误码 "+jsonObject.optString("info"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
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
