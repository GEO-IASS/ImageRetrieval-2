package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.SearchService;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchText =request.getParameter("searchText");
		System.out.println(searchText);
		if(searchText==null || searchText.equals("")){
			response.sendRedirect(request.getContextPath()+"/index.jsp");
		}else{
			SearchService ss=new SearchService();
			List images=ss.getImages(searchText);
			System.out.println("π≤”–"+images.size()+"’≈Õº∆¨");
			long timeCost=ss.getTimeCost();
			request.setAttribute("images", images);
			request.setAttribute("timeCost", timeCost);
			request.setAttribute("searchText", searchText);
			request.getRequestDispatcher("../searcher.jsp").forward(request, response);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
