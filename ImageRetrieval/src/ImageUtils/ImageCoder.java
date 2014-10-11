package ImageUtils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import bean.ImageBean;


public class ImageCoder {
	private int width=16;
	private int height=16;
	/**
	 * ��ͼ����б���
	 * @param filename
	 * @return
	 * @throws IOException 
	 */
	public String fingerPrinter(String filename){
		//��ȡͼƬ
		BufferedImage source=ImageUtils.readPNGImage(filename);
		//�õ�����ͼ
		BufferedImage image=ImageUtils.abbreviativeGraphic(source, width, height, false);
		//�õ��Ҷ�ͼ��ʾ
		int[] pixels = new int[width * height];
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				pixels[i*height+j]=ImageUtils.rgb2gray(image.getRGB(i, j));
			}
		}
		//����Ҷ�ƽ��ֵ
		int average=ImageUtils.average(pixels);
		//���ػҶȱȽ�
		int[] comp=ImageUtils.comp(pixels, average);
		//�����ϣֵ
		StringBuffer code=ImageUtils.hashCoder(comp);
		
		return code.toString();
	}
	/**
	 * ���㺺������
	 * @param source
	 * @param target
	 * @return
	 */
	public int getDistance(String source,String target){
		int difference=0;
		int len=source.length();
		for(int i=0;i<len;i++){
			if(source.charAt(i)!=target.charAt(i)){
				difference ++;
			}
		}		
		return difference;
	}
	
	
	/**
	 * ��ͼ��������ƥ��
	 * @param sourceCode
	 * @return
	 */
	public List match(String sourceCode) {
		
		List list=new ArrayList();
		ImageBean ibean=null;
		String codeFile="D:/WHMao/java_WorkSpace/ImageCoder/coder/coder.txt";			
		try {
			BufferedReader br=new BufferedReader(new FileReader(codeFile));
			String line=null;
			int i=1;
			while((line=br.readLine())!=null){
				int difference=this.getDistance(sourceCode.trim(), line.trim());			//���㺺������
				String imagename="im"+i+".jpg";
				ibean=new ImageBean(imagename,difference);
				list.add(ibean);
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println("��ͼƬim1000.jpgͼ�����Ƽ���:");
		/******����****/
		// ����,ͨ�����ͺ���������ʵ��  
        Collections.sort(list, new Comparator<ImageBean>() {  
			//@Override
			public int compare(ImageBean ib1, ImageBean ib2) {
				 return  ib1.getDifference() - ib2.getDifference();                    
			}  
        });  
        
        
      // ��ӡ������  
  /*      Iterator<ImageBean> iterator = list.iterator();  
        int i=0;
        while (iterator.hasNext()) {
        	if(i>100)
        		break;
            ImageBean ib = iterator.next();  
            System.out.println("image=" + ib.getImagename() + ";difference=" + ib.getDifference());
            i++;
        }  
        System.out.println("*******************************************");  */
		return list;
	}
}
