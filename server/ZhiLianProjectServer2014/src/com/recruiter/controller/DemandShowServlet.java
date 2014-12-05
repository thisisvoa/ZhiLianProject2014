package com.recruiter.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.jobhunter.bean.Job;
import com.jobhunter.model.JobService;
import com.recruiter.bean.Demand;
import com.recruiter.model.DemandService;

/**
 * Servlet implementation class DemandshowServlet
 */
public class DemandShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DemandShowServlet() {
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
		String username = (String) request.getSession().getAttribute("username");
		String show = request.getParameter("show");
		String id_cloud = request.getParameter("id_cloud");
		
		JobService jobService = new JobService();
		ArrayList<String> list_field = jobService.getField();
		ArrayList<Job> list_job = jobService.getJob();
		JSONArray json_job = JSONArray.fromObject(list_job);
		DemandService demandservice = new DemandService();

		RequestDispatcher dispatcher = null;
		switch(show) {
		case "demand":
			ArrayList<Demand> list = demandservice.getDemand(username);
			for (Demand demand : list) {
				String demand_detail = demand.getDemand_detail();
				demand_detail = demand_detail.replaceAll("\\n","</p><p>").replaceAll(" ","&nbsp;");
				demand.setDemand_detail(demand_detail);
			}
			request.setAttribute("list", list);
			dispatcher = request.getRequestDispatcher("demand.jsp");
			dispatcher.forward(request, response);
			break;
		case "demandedit":
			Demand demand = demandservice.getDemand(username, id_cloud);
			request.setAttribute("list_field", list_field);
			request.setAttribute("list_job", list_job);
			request.setAttribute("json_job", json_job);
			request.setAttribute("demand", demand);
			dispatcher = request.getRequestDispatcher("demandedit.jsp");
			dispatcher.forward(request, response);
			break;
		case "demandadd":
			request.setAttribute("list_field", list_field);
			request.setAttribute("json_job", json_job);
			dispatcher = request.getRequestDispatcher("demandadd.jsp");
			dispatcher.forward(request, response);
			break;
		}
	}

}
