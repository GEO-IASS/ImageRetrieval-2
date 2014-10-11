package servlet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

public class ImageServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
String fileName=request.getParameter("fileName");
		
		//fileName=fileName;
		File img=new File(fileName);
		/*BufferedImage image=ImageIO.read(new FileInputStream(img));  
		response.setContentType("image/jpeg"); // 设置返回的文件类型   
		ImageIO.write(image, "jpg", response.getOutputStream());*/
		BufferedImage image=null;
		try{
			image=ImageIO.read(new FileInputStream(img));  
		}catch (Exception e) {
			ImageIcon ii=new ImageIcon(fileName);
			Image i=ii.getImage();
			image = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(i, null, null);
		}
		response.setContentType("image/jpeg"); // 设置返回的文件类型   
		ImageIO.write(image, "jpg", response.getOutputStream());
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
