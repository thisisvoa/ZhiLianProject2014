package com.recruiter.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.recruiter.bean.Info;
import com.recruiter.model.InfoService;

/**
 * Servlet implementation class InfoEditServlet
 */
public class InfoEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoEditServlet() {
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
		String name = (String) request.getParameter("name");
		String address = (String) request.getParameter("address");
		String location = (String) request.getParameter("location");
		String email = (String) request.getParameter("email");
		String detail = (String) request.getParameter("detail");
		
		Info info = new Info();
		info.setUsername(username);
		info.setName(name);
		info.setAddress(address);
		info.setLocation(location);
		info.setEmail(email);
		info.setDetail(detail);
		InfoService infoservice = new InfoService();
		boolean flag = infoservice.editInfo(info);
		
		if (flag == true) {
			response.sendRedirect("InfoShowServlet?show=info");
		}
	}

}
