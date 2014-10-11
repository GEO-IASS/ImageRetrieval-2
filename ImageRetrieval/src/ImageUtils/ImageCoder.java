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
	 * 对图像进行编码
	 * @param filename
	 * @return
	 * @throws IOException 
	 */
	public String fingerPrinter(String filename){
		//读取图片
		BufferedImage source=ImageUtils.readPNGImage(filename);
		//得到缩略图
		BufferedImage image=ImageUtils.abbreviativeGraphic(source, width, height, false);
		//得到灰度图表示
		int[] pixels = new int[width * height];
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				pixels[i*height+j]=ImageUtils.rgb2gray(image.getRGB(i, j));
			}
		}
		//计算灰度平均值
		int average=ImageUtils.average(pixels);
		//像素灰度比较
		int[] comp=ImageUtils.comp(pixels, average);
		//计算哈希值
		StringBuffer code=ImageUtils.hashCoder(comp);
		
		return code.toString();
	}
	/**
	 * 计算汉明距离
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
	 * 对图像编码进行匹配
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
				int difference=this.getDistance(sourceCode.trim(), line.trim());			//计算汉明距离
				String imagename="im"+i+".jpg";
				ibean=new ImageBean(imagename,difference);
				list.add(ibean);
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println("与图片im1000.jpg图像相似计算:");
		/******排序****/
		// 排序,通过泛型和匿名类来实现  
        Collections.sort(list, new Comparator<ImageBean>() {  
			//@Override
			public int compare(ImageBean ib1, ImageBean ib2) {
				 return  ib1.getDifference() - ib2.getDifference();                    
			}  
        });  
        
        
      // 打印排序结果  
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
