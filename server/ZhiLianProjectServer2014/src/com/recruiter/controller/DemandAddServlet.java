package com.recruiter.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.recruiter.bean.Demand;
import com.recruiter.model.DemandService;

/**
 * Servlet implementation class DemandAddServlet
 */
public class DemandAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DemandAddServlet() {
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
		String username = (String) request.getSession().getAttribute("username");
		String demand_field = (String) request.getParameter("demand_field");
		String demand_job = (String) request.getParameter("demand_job");
		String demand_jobname = (String) request.getParameter("demand_jobname");
		String demand_salary_min = (String) request.getParameter("demand_salary_min");
		String demand_salary_max = (String) request.getParameter("demand_salary_max");
		String demand_experience = (String) request.getParameter("demand_experience");
		String demand_detail = (String) request.getParameter("demand_detail");
		
		Demand demand = new Demand();
		demand.setUsername(username);
		demand.setDemand_field(demand_field);
		demand.setDemand_job(demand_job);
		demand.setDemand_jobname(demand_jobname);
		demand.setDemand_salary_min(demand_salary_min);
		demand.setDemand_salary_max(demand_salary_max);
		demand.setDemand_experience(demand_experience);
		demand.setDemand_detail(demand_detail);
		
		DemandService demandservice = new DemandService();
		boolean flag = demandservice.addDemand(demand);
		
		if (flag == true) {
			response.sendRedirect("DemandShowServlet?show=demand");
		}
	}

}
