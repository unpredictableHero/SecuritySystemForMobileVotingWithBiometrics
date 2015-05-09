package com.serverside;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
@WebServlet(name="Servlet", urlPatterns={"/Servlet"})
public class Servlet extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
        String un,pw;
        un=request.getParameter("username");
        pw=request.getParameter("password");
        if(un.equalsIgnoreCase("hello") && pw.equals("world"))
            out.print(1);
        else
            out.print(0);
        } finally {            
            out.close();
        }
    }
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
	
	@Override
    public String getServletInfo() {
        return "Short description";
    }


}
