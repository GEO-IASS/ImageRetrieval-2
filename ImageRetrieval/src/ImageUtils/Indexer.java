package ImageUtils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;









import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;
import dirSetting.Constant;

public class Indexer {
	public static void main(String args[]){
		String imagePath=Constant.IMAGE_PATH;
		String contentIndex=Constant.CONTENT_INDEX;
		
		File f=new File(imagePath);
		if(!(f.exists() && f.isDirectory())){
			System.out.println("不是图片的目录,请检查清楚!!!");
			System.exit(1);
		}
		
			ArrayList<String> images = null;
			DocumentBuilder builder=null;
			IndexWriter iw=null;
			try {
				images = FileUtils.getAllImages(new File(imagePath), true);
				builder=DocumentBuilderFactory.getCEDDDocumentBuilder();
				IndexWriterConfig conf=new IndexWriterConfig(LuceneUtils.LUCENE_VERSION,new WhitespaceAnalyzer(LuceneUtils.LUCENE_VERSION));
				iw=new IndexWriter(FSDirectory.open(new File(contentIndex)),conf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			for(Iterator<String> it=images.iterator();it.hasNext();){
				String imageFilePath=it.next();
				System.out.println("Indexing "+imageFilePath);
				
				BufferedImage img = null;
				try {
					img = ImageIO.read(new FileInputStream(imageFilePath));
				} catch (Exception e) {
					ImageIcon ii=new ImageIcon(imageFilePath);
					Image i=ii.getImage();
					img = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
					Graphics2D g = img.createGraphics();
					g.drawImage(i, null, null);
					//e.printStackTrace();
				} 
				
				Document document;
				try {
					document = builder.createDocument(img, imageFilePath);
					iw.addDocument(document);
				} catch (Exception e) {
					System.err.println("Error reading image or indexing it.");
					e.printStackTrace();
				}
			}	
			
			try {
				if(iw!=null)
					iw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.println("Content-Based image index completed");
	}
}
