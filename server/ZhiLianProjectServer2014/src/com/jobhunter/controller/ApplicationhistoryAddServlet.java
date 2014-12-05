package com.jobhunter.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jobhunter.bean.Applicationhistory;
import com.jobhunter.model.ApplicationhistoryService;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ApplicationhistoryAddServlet
 */
public class ApplicationhistoryAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationhistoryAddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String json = readJSONString(request);
        JSONObject jsonObject = JSONObject.fromObject(json);
        
        Applicationhistory applicationhistory = new Applicationhistory();
        applicationhistory.setUserphone(jsonObject.optString("userphone"));
        applicationhistory.setApplication_recruitername(jsonObject.optString("application_recruitername"));
        applicationhistory.setApplication_jobname(jsonObject.optString("application_jobname"));
        applicationhistory.setApplication_salary(jsonObject.optString("application_salary"));
        applicationhistory.setApplication_time_hour(jsonObject.optString("application_time_hour"));
        applicationhistory.setApplication_time_day(jsonObject.optString("application_time_day"));
        applicationhistory.setId_cloud(jsonObject.optString("id_cloud"));
        
        ApplicationhistoryService applicationhistoryservice = new ApplicationhistoryService();
        boolean flag = applicationhistoryservice.addApplicationhistory(applicationhistory);
        
        PrintWriter out = response.getWriter();
		out.print(flag);
	}
	
	private String readJSONString(HttpServletRequest request){
        StringBuffer json = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        return json.toString();
    }

}
