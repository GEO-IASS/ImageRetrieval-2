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
	 * ��ȡͼƬ�ĻҶ�����
	 * @param filePath
	 * @param image
	 * @return
	 */
	public String read_Hist(String filePath,String image){
		//��ȡͼƬ���к�
		Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(image);
        String id=null;
        while(matcher.find()) {
          id = matcher.group();
        }
		//System.out.println(id);
		//
		int imageId=Integer.valueOf(id);
		//System.out.println("ͼ��ĻҶ��к�:"+imageId);
		BufferedReader br=null;
		String histStr=null;
		int i=1;
		try {
			br=new BufferedReader(new FileReader(filePath));
			while(i<imageId){
				String line=br.readLine();
				i++;
			}
			//��ȡͼƬ�ĻҶ�ֵ
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
	 * ����ŷ�Ͼ���ĵ����������ƶ�
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
		//ת��Ϊ��������
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
			dist=0.005f;			//��ͼƬ���Լ�����������(�������)
*/		float score = 1 / (dist+1) ;			//<ʹ��   1 / (x+1) ����������д����Ϊ��Ҫ��>
		return score;
	}
	/**
	 * ������sorceHist(1---20 0000 )����Ҫ��
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
			//��ȡͼƬ�ĻҶ�ֵ
			int i=0;
		while(i<sum){
				target=br.readLine();
				histScore[i]=this.compute(sourceHist, target);		
				//System.out.println("��� "+(i+1)+" ��ͼƬ���ƶ�Ϊ�� "+histScore[i]);
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
	  * �ı��ķ������Ҷ�ͼ�ķ��� 
	  * @param histScore   1---20 0000
	  * @param searchText
	  * @return
	  */
	public List crossScore(float[] histScore,String searchText){
		List croImList=new ArrayList();				//����ͼƬ�б�
		
		SearchService ss=new SearchService();		
		List imagelist=ss.getImages(searchText);
		
		
		for(int i=0;i<imagelist.size();i++){
			ImageBean image=(ImageBean)imagelist.get(i);
			String imagename=image.getImagename();
			//��ȡͼƬ���ļ����е�����,��Ϊ���
			Pattern pattern = Pattern.compile("\\d+");
	        Matcher matcher = pattern.matcher(imagename);
	        String id=null;
	        while(matcher.find()) {
	          id = matcher.group();
	        }
	        int index=Integer.parseInt(id);
	       // System.out.println("ͼ������:"+imagename+"\t index:"+index);
			float a=0.6f;
			float score = a * image.getScore() + (1-a)*histScore[index - 1];				//�ں��ı��Ĵ�ֺͻҶ�ͼ�Ĵ��
			histScore[index-1]=score;
			image.setScore(score);
			
			croImList.add(image);
		}
		return croImList;
	}
	/**
	 * ����Ҫ�Ե�ͼƬ��������
	 * @param croImList
	 * @return
	 */
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
	/**
	 * �������̵��ú���
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
		timeCost=endTime - startTime;			//�õ�ʱ�仨��
		return croList;
	}
}
