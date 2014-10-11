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
		//��ȡͼƬ���ļ����е�����,��Ϊ���
		Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(imagename);
        String id=null;
        while(matcher.find()) {
          id = matcher.group();
        }
		//System.out.println(id);
		//
		int imageId=Integer.valueOf(id);
		//System.out.println("ͼ��ĻҶ��к�:"+imageId);
        BufferedReader br=null;
		String label=null;
		int i=1;
		try {
			br=new BufferedReader(new FileReader(filePath));
			while(i<imageId){
				String line=br.readLine();
				i++;
			}
			//��ȡͼƬ�ı�ǩ
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
	
	//��ע��ͼ��
	public List getTagScore(String searchText){		
		SearchService ss=new SearchService();		
		List imagelist=ss.getImages(searchText);
		return imagelist;
	}
	//�Ӿ���ͼ��
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
			//��ȡͼƬ���ļ����е�����,��Ϊ���
			Pattern pattern = Pattern.compile("\\d+");
	        Matcher matcher = pattern.matcher(image.getImagename());
	        String id=null;
	        while(matcher.find()) {
	          id = matcher.group();
	        }
	        int index=Integer.parseInt(id);
	        tagScore[index-1]=image.getScore();
	        System.out.println("------��ǩ:"+i+"\t index:"+(index-1)+"\timagename:"+image.getImagename()+"\tscore:"+image.getScore());
		}
		return tagScore;		
	}
	public float[] comp_ContentScore(List contentList){
		int sum=200000;
		float contentScore[]=new float[sum];
		for(int i=0;i<contentList.size();i++){
			ImageBean image=(ImageBean)contentList.get(i);
			//��ȡͼƬ���ļ����е�����,��Ϊ���
			Pattern pattern = Pattern.compile("\\d+");
	        Matcher matcher = pattern.matcher(image.getImagename());
	        String id=null;
	        while(matcher.find()) {
	          id = matcher.group();
	        }
	        int index=Integer.parseInt(id);
	        contentScore[index-1]=image.getScore();
	        System.out.println("*****��ǩ:"+i+"\t index:"+(index-1)+"\timagename:"+image.getImagename()+"\tscore:"+image.getScore());
		}
		return contentScore;		
	}
	
	public List crossScore(float[] tagScore,float[] contentScore){
		List croImList=new ArrayList();				//����ͼƬ�б�
		
		
		ImageBean image=null;
		for(int i=0;i<tagScore.length;i++){
			image=new ImageBean();
			float a=0.6f;
			float score = a * tagScore[i] + (1-a)* (1/(contentScore[i]+1));				//�ںϴ��	
			
			image.setImagename(Constant.IMAGE_PATH+"im"+((Integer)(i+1)).toString()+".jpg");
			image.setScore(score);
			croImList.add(image);
		}
		return croImList;
	}
	
	public List soreCro(List croImList){
		/******����****/
		// ����,ͨ�����ͺ���������ʵ��  
        Collections.sort(croImList, new Comparator<ImageBean>() {  
			//@Override
			public int compare(ImageBean ib1, ImageBean ib2) {
				 return  (int)((ib2.getScore() - ib1.getScore())*10000);    		//Ϊ�˾��пɱ���,���зŴ�10000��                
			}  
        });  
        
     // ��ӡ������  
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
		timeCost=endTime - startTime;			//�õ�ʱ�仨��
		return croList;
	}
}
