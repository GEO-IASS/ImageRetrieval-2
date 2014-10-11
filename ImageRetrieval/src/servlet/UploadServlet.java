package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import service.ImageMatcherService;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
			String imagename=this.upload(request, response);
			long startTime=System.currentTimeMillis();
			ImageMatcherService ims=new ImageMatcherService();
			String filename="D:/WHMao/WORK/ImageRetrieval/uploadHistory/"+imagename;
			List images=ims.getMatch(filename);
			long endTime=System.currentTimeMillis();
			long timeCost= endTime - startTime;
			request.setAttribute("images", images);
			request.setAttribute("timeCost", timeCost);
			request.getRequestDispatcher("../searcher.jsp").forward(request, response);
			
	}
	protected String  upload(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String name = null;
		String addr = null;
		String describe = null;
		DiskFileItemFactory factory = new DiskFileItemFactory(); // 创建磁盘工厂
		ServletFileUpload upload = new ServletFileUpload(factory); // 创建处理工具
		upload.setFileSizeMax(3 * 1024 * 1024); // 设置最大上传大小为3M
		upload.setHeaderEncoding("UTF-8");
		List<FileItem> items = null;
		try {
			items = upload.parseRequest(request); // 接收全部内容
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (!item.isFormField()) { // 不是普通文本数据，是上传文件
				InputStream input = null; // 定义输入流，用以读取源文件
				OutputStream output = null; // 定义输出流，用以保存文件
				input = item.getInputStream(); // 取得上传文件的输入流
				/* 下面为产生文件名 */
				/*
				 * SimpleDateFormat date = new SimpleDateFormat(
				 * "yyyyMMddHHmmssSSS"); String d = date.format(new Date());
				 */
				/** ************************** */
				String d = "target";
				name = d + "." + item.getName().split("\\.")[1]; // 设置文件名
				addr = "../" + "uploadPhoto" + File.separator; // 设置路径位置
				output = new FileOutputStream(new File(
						"D:/WHMao/WORK/ImageRetrieval/uploadHistory/"
								+ name));// 定义文件输出路径
				byte data[] = new byte[512]; // 分块保存
				while ((input.read(data, 0, 512)) != -1) // 依次读取内容
				{
					output.write(data); // 保存内容
				}
				input.close();
				output.close();
			} else {
				describe = item.getString("UTF-8");
				// System.out.println(describe);
			}
			// message="上传成功";

		}
		return "target.jpg";
	}
}
