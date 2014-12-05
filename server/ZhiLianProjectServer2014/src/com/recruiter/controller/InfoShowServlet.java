package com.recruiter.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.recruiter.bean.Info;
import com.recruiter.model.InfoService;

/**
 * Servlet implementation class InfoServlet
 */
public class InfoShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoShowServlet() {
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
		InfoService infoservice = new InfoService();
		Info info = infoservice.getInfo(username);
		String detail = info.getDetail();
		if(show.equals("info")) {
			detail = detail.replaceAll("\\n","</p><p>").replaceAll(" ","&nbsp;");
		}
		info.setDetail(detail);
		request.setAttribute("info", info);
		
		RequestDispatcher dispatcher = null;
		switch(show) {
		case "info":
			dispatcher = request.getRequestDispatcher("info.jsp");
			dispatcher.forward(request, response);
			break;
		case "infoedit":
			dispatcher = request.getRequestDispatcher("infoedit.jsp");
			dispatcher.forward(request, response);
			break;
		}
	}

}
