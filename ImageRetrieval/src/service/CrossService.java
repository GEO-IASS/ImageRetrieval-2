package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ImageUtils.Searcher;
import bean.ImageBean;
import dirSetting.Constant;

public class CrossService {
	private long timeCost=0;
	public long getTimeCost() {
		return timeCost;
	}

	public void setTimeCost(long timeCost) {
		this.timeCost = timeCost;
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
	
	//标注的图像
	public List getTagScore(String searchText){		
		SearchService ss=new SearchService();		
		List imagelist=ss.getImages(searchText);
		return imagelist;
	}
	//视觉的图像
	public List getContentScore(String searchText){		
		Searcher s=new Searcher();		
		List imagelist=s.getSearch(searchText);
		return imagelist;
	}
	
	
	public float[] comp_TagScore(List tagList){
		int sum=200000;
		float tagScore[]=new float[sum];
		for(int i=0;i<tagList.size();i++){
			ImageBean image=(ImageBean)tagList.get(i);
			//获取图片的文件名中的数字,即为编号
			Pattern pattern = Pattern.compile("\\d+");
	        Matcher matcher = pattern.matcher(image.getImagename());
	        String id=null;
	        while(matcher.find()) {
	          id = matcher.group();
	        }
	        int index=Integer.parseInt(id);
	        tagScore[index-1]=image.getScore();
	        System.out.println("------标签:"+i+"\t index:"+(index-1)+"\timagename:"+image.getImagename()+"\tscore:"+image.getScore());
		}
		return tagScore;		
	}
	public float[] comp_ContentScore(List contentList){
		int sum=200000;
		float contentScore[]=new float[sum];
		for(int i=0;i<contentList.size();i++){
			ImageBean image=(ImageBean)contentList.get(i);
			//获取图片的文件名中的数字,即为编号
			Pattern pattern = Pattern.compile("\\d+");
	        Matcher matcher = pattern.matcher(image.getImagename());
	        String id=null;
	        while(matcher.find()) {
	          id = matcher.group();
	        }
	        int index=Integer.parseInt(id);
	        contentScore[index-1]=image.getScore();
	        System.out.println("*****标签:"+i+"\t index:"+(index-1)+"\timagename:"+image.getImagename()+"\tscore:"+image.getScore());
		}
		return contentScore;		
	}
	
	public List crossScore(float[] tagScore,float[] contentScore){
		List croImList=new ArrayList();				//保存图片列表
		
		
		ImageBean image=null;
		for(int i=0;i<tagScore.length;i++){
			image=new ImageBean();
			float a=0.6f;
			float score = a * tagScore[i] + (1-a)* (1/(contentScore[i]+1));				//融合打分	
			
			image.setImagename(Constant.IMAGE_PATH+"im"+((Integer)(i+1)).toString()+".jpg");
			image.setScore(score);
			croImList.add(image);
		}
		return croImList;
	}
	
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
	
	public List getCroList(String imagename){
		long startTime=System.currentTimeMillis();
		HistService hs=new HistService();
		System.out.println("imageName"+imagename);
		String str=hs.read_Hist("D:/luke/hist_4.txt", imagename);
		
		String searchText=this.getSearchText(Constant.TTAG_PATH+"Tags", imagename);
		List tagList=this.getTagScore(searchText);
		List contentList=this.getContentScore(imagename);
		float tagScore[]=this.comp_TagScore(tagList);
		float contentScore[]=this.comp_ContentScore(contentList);
		List croList=this.crossScore(tagScore,contentScore);
		croList=this.soreCro(croList);
		long endTime=System.currentTimeMillis();
		timeCost=endTime - startTime;			//得到时间花销
		return croList;
	}
}
