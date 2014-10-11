package service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dirSetting.Constant;
import bean.ImageBean;

public class HistService {
	
	private long timeCost=0;
	/**
	 * 读取图片的灰度序列
	 * @param filePath
	 * @param image
	 * @return
	 */
	public String read_Hist(String filePath,String image){
		//获取图片的行号
		Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(image);
        String id=null;
        while(matcher.find()) {
          id = matcher.group();
        }
		//System.out.println(id);
		//
		int imageId=Integer.valueOf(id);
		//System.out.println("图像的灰度行号:"+imageId);
		BufferedReader br=null;
		String histStr=null;
		int i=1;
		try {
			br=new BufferedReader(new FileReader(filePath));
			while(i<imageId){
				String line=br.readLine();
				i++;
			}
			//读取图片的灰度值
			histStr=br.readLine();
			//System.out.println("histStr:"+histStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{			
			try {
				if(br!=null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return histStr;
	}
	
	/**
	 * 计算欧氏距离的倒数评价相似度
	 * @param source
	 * @param target
	 * @return
	 */
	public float compute(String source,String target){
		float dist=0;
		float[] sorce, tar;
		String[] temp1=(source.trim()).split("[\\s]+");
		String[] temp2=(target.trim()).split("[\\s]+");
		int len=temp1.length;
		sorce=new float[len];
		tar=new float[len];
		//转变为数字向量
		for(int i=0;i<len;i++){
			//System.out.print("......."+temp1[i+1]+"\t");
			sorce[i]=Float.parseFloat(temp1[i].trim());
			tar[i]=Float.parseFloat(temp2[i].trim());
			//System.out.print(sorce[i]+"\t");
		}
		for(int i=0;i<len;i++){
			dist +=(sorce[i]-tar[i])*(sorce[i]-tar[i]);
		}
		dist=(float) Math.sqrt(dist);
/*		if(dist==0)
			dist=0.005f;			//该图片与自己的相似设置(经验给出)
*/		float score = 1 / (dist+1) ;			//<使用   1 / (x+1) 函数对其进行打分作为重要性>
		return score;
	}
	/**
	 * 计算与sorceHist(1---20 0000 )的重要性
	 * @param filePath
	 * @param sourceHist
	 * @return
	 */
	public float[] comp_HistScore(String filePath,String sourceHist){
		int sum=200000;
		float histScore[]=new float[sum];
		BufferedReader br=null;
		String target=null;
		try {
			br=new BufferedReader(new FileReader(filePath));
			//读取图片的灰度值
			int i=0;
		while(i<sum){
				target=br.readLine();
				histScore[i]=this.compute(sourceHist, target);		
				//System.out.println("与第 "+(i+1)+" 张图片相似度为： "+histScore[i]);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{			
			try {
				if(br!=null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return histScore;		
	}
	public long getTimeCost() {
		return timeCost;
	}
	 /**
	  * 文本的分数及灰度图的分数 
	  * @param histScore   1---20 0000
	  * @param searchText
	  * @return
	  */
	public List crossScore(float[] histScore,String searchText){
		List croImList=new ArrayList();				//保存图片列表
		
		SearchService ss=new SearchService();		
		List imagelist=ss.getImages(searchText);
		
		
		for(int i=0;i<imagelist.size();i++){
			ImageBean image=(ImageBean)imagelist.get(i);
			String imagename=image.getImagename();
			//获取图片的文件名中的数字,即为编号
			Pattern pattern = Pattern.compile("\\d+");
	        Matcher matcher = pattern.matcher(imagename);
	        String id=null;
	        while(matcher.find()) {
	          id = matcher.group();
	        }
	        int index=Integer.parseInt(id);
	       // System.out.println("图像名字:"+imagename+"\t index:"+index);
			float a=0.6f;
			float score = a * image.getScore() + (1-a)*histScore[index - 1];				//融合文本的打分和灰度图的打分
			histScore[index-1]=score;
			image.setScore(score);
			
			croImList.add(image);
		}
		return croImList;
	}
	/**
	 * 对重要性的图片进行排序
	 * @param croImList
	 * @return
	 */
	public List soreCro(List croImList){
		/******排序****/
		// 排序,通过泛型和匿名类来实现  
        Collections.sort(croImList, new Comparator<ImageBean>() {  
			//@Override
			public int compare(ImageBean ib1, ImageBean ib2) {
				 return  (int)((ib2.getScore() - ib1.getScore())*10000);    		//为了具有可比性,进行放大10000倍                
			}  
        });  
        
     // 打印排序结果  
        Iterator<ImageBean> iterator = croImList.iterator();  
        int i=0;
        while (iterator.hasNext()) {
        	if(i>100)
        		break;
            ImageBean ib = iterator.next();  
           //System.out.println("image=" + ib.getImagename() + ";score=" + ib.getScore());
            i++;
        }  
  
        System.out.println("*******************************************");  
		return croImList;
	}
	public String getSearchText(String filePath,String imagename){
		//获取图片的文件名中的数字,即为编号
		Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(imagename);
        String id=null;
        while(matcher.find()) {
          id = matcher.group();
        }
		//System.out.println(id);
		//
		int imageId=Integer.valueOf(id);
		//System.out.println("图像的灰度行号:"+imageId);
        BufferedReader br=null;
		String label=null;
		int i=1;
		try {
			br=new BufferedReader(new FileReader(filePath));
			while(i<imageId){
				String line=br.readLine();
				i++;
			}
			//读取图片的标签
			label=br.readLine();
			//System.out.println("histStr:"+histStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{			
			try {
				if(br!=null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
		return label;
	}
	/**
	 * 步骤流程调用函数
	 * @param searchText
	 * @param imagename
	 * @return
	 */
	public List getCroList(String imagename){
		long startTime=System.currentTimeMillis();
		HistService hs=new HistService();
		String str=hs.read_Hist("D:/luke/hist_4.txt", imagename);
		//System.out.println("str-->"+str);
		float[] histScore=hs.comp_HistScore("D:/luke/hist_4.txt", str);
		String searchText=hs.getSearchText(Constant.TTAG_PATH+"Tags", imagename);
		List score=hs.crossScore(histScore,searchText);
		List croList=hs.soreCro(score);
		long endTime=System.currentTimeMillis();
		timeCost=endTime - startTime;			//得到时间花销
		return croList;
	}
}
