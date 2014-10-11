package ImageUtils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import bean.ImageBean;
import dirSetting.Constant;

public class Searcher {
	private String Context_Index=Constant.CONTENT_INDEX;
	private List imageList=new ArrayList();
	public List getSearch(String imagePath){
		int sum=20;			//一次性检索数目
		ImageBean image=null;
		
		if(imagePath ==null){
			System.out.println("请核查匹配的图像...");
			System.exit(1);
		}
		File f=new File(imagePath);
		BufferedImage img=null;
		if(f.exists()){
			try {
				img=ImageIO.read(f);
			} catch (IOException e) {
				ImageIcon ii=new ImageIcon(imagePath);
				Image i=ii.getImage();
				img = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g = img.createGraphics();
				g.drawImage(i, null, null);
				//e.printStackTrace();
			}
		}
		try {
			IndexReader ir=DirectoryReader.open(FSDirectory.open(new File(Context_Index)));
			ImageSearcher searcher=ImageSearcherFactory.createCEDDImageSearcher(sum);
			
			ImageSearchHits hits=searcher.search(img, ir);
			for(int i=0;i<hits.length();i++){
				Document document=hits.doc(i);
				String fileName=document.getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
				//imageList.add(fileName);
				
				image=new ImageBean();
				image.setImagename(fileName);
				float score=hits.score(i);
				image.setScore(score);
				imageList.add(image);
				
				System.out.println(hits.score(i)+",\t"+fileName);
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return imageList;
	}
}
