package com.jobhunter.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jobhunter.bean.Person;
import com.jobhunter.model.PersonService;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class PersonEditServlet
 */
public class PersonEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonEditServlet() {
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
		String json = readJSONString(request);
        JSONObject jsonObject = JSONObject.fromObject(json);
        
        Person person = new Person();
        person.setUsername(jsonObject.optString("username"));
        person.setUserphone(jsonObject.optString("userphone"));
        person.setUserdemand_field(jsonObject.optString("userdemand_field"));
        person.setUserdemand_job(jsonObject.optString("userdemand_job"));
        person.setUserdemand_salary(jsonObject.optString("userdemand_salary"));
        person.setUserdemand_experience(jsonObject.optString("userdemand_experience"));
        
        PersonService personService = new PersonService();
        boolean flag = personService.editPerson(person);
        
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
