package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CrossService;
import service.HistService;


public class CrossSearchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String searchText =request.getParameter("searchText");
		String imagename=request.getParameter("imagename");
	/*	CrossService sc=new CrossService();
		List images=sc.getCroList(imagename);		
		System.out.println("共有"+images.size()+"张图片");
		long timeCost=hs.getTimeCost();*/
		HistService hs=new HistService();
		List images=hs.getCroList(imagename);
		System.out.println("共有"+images.size()+"张图片");
		long timeCost=hs.getTimeCost();
		request.setAttribute("images", images);
		request.setAttribute("timeCost", timeCost);
		//request.setAttribute("searchText", searchText);
		request.getRequestDispatcher("../searcher.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
