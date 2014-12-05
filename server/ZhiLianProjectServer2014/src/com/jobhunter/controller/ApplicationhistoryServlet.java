package com.jobhunter.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jobhunter.bean.Applicationhistory;
import com.jobhunter.bean.Person;
import com.jobhunter.model.ApplicationhistoryService;
import com.jobhunter.model.PersonService;

/**
 * Servlet implementation class ApplicationhistoryServlet
 */
public class ApplicationhistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationhistoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userphone = request.getParameter("userphone");
		ApplicationhistoryService applicationhistoryService = new ApplicationhistoryService();
		ArrayList<Applicationhistory> applicationhistory = applicationhistoryService.getApplicationhistory(userphone);
		
		JSONArray jsonArray = JSONArray.fromObject(applicationhistory);
		PrintWriter out = response.getWriter();
		out.print(jsonArray);
	}

}
