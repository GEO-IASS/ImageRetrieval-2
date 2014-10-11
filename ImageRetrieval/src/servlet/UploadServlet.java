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
		DiskFileItemFactory factory = new DiskFileItemFactory(); // �������̹���
		ServletFileUpload upload = new ServletFileUpload(factory); // ����������
		upload.setFileSizeMax(3 * 1024 * 1024); // ��������ϴ���СΪ3M
		upload.setHeaderEncoding("UTF-8");
		List<FileItem> items = null;
		try {
			items = upload.parseRequest(request); // ����ȫ������
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (!item.isFormField()) { // ������ͨ�ı����ݣ����ϴ��ļ�
				InputStream input = null; // ���������������Զ�ȡԴ�ļ�
				OutputStream output = null; // ��������������Ա����ļ�
				input = item.getInputStream(); // ȡ���ϴ��ļ���������
				/* ����Ϊ�����ļ��� */
				/*
				 * SimpleDateFormat date = new SimpleDateFormat(
				 * "yyyyMMddHHmmssSSS"); String d = date.format(new Date());
				 */
				/** ************************** */
				String d = "target";
				name = d + "." + item.getName().split("\\.")[1]; // �����ļ���
				addr = "../" + "uploadPhoto" + File.separator; // ����·��λ��
				output = new FileOutputStream(new File(
						"D:/WHMao/WORK/ImageRetrieval/uploadHistory/"
								+ name));// �����ļ����·��
				byte data[] = new byte[512]; // �ֿ鱣��
				while ((input.read(data, 0, 512)) != -1) // ���ζ�ȡ����
				{
					output.write(data); // ��������
				}
				input.close();
				output.close();
			} else {
				describe = item.getString("UTF-8");
				// System.out.println(describe);
			}
			// message="�ϴ��ɹ�";

		}
		return "target.jpg";
	}
}
