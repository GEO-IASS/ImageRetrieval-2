package index;

import dirSetting.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class ImageIndex {
	private String directory_Path=null;
	private String image_Path=null;
	private String tag_Path=null;
	
	private Directory directory=null;
	private IndexWriter writer=null;
	
	public ImageIndex(){
		try {
			directory_Path=Constant.DIRECTORU_PATH;
			image_Path=Constant.IMAGE_PATH;
			tag_Path=Constant.TTAG_PATH;
			
			directory = FSDirectory.open(new File(directory_Path));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	/**
	 * 建立索引
	 */
	public void index(){
		try {
			Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_45); 
			writer = new IndexWriter(directory,new IndexWriterConfig(Version.LUCENE_45, analyzer));
			BufferedReader br=new BufferedReader(new FileReader(tag_Path+"Tags"));
			String tags=null;		//表示tags
			Document doc=null;
			int i=0;
			String fileheader="im";
			int imageNum=200000;
			for(int f=1;f<=imageNum;f++){
				String imageName=fileheader+((Integer)f).toString()+".jpg";
				doc=new Document();
				tags=br.readLine();
				if(i<50)
					System.out.println(tags+":"+imageName);
				i++;
				doc.add(new TextField("tag",tags,Field.Store.YES));
				doc.add(new Field("imageName",imageName,Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));		//Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				writer.addDocument(doc);
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(writer!=null)
					writer.close();
			} catch (CorruptIndexException e) {		
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 测试索引构建如何
	 * @param args
	 */
	public static void main(String args[]){
		ImageIndex idex=new ImageIndex();
		idex.index();
		System.out.println("索引构建完成...");
	}
}
