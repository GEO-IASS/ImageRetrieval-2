package search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import bean.ImageBean;
import dirSetting.Constant;

public class ImageSearcher {
	private String directory_Path = null;
	private String image_Path = null;
	private float score;
	private long timeCost=0;
	private List imagelist=null;

	private Directory directory = null;
	private IndexReader reader = null;
	

	public ImageSearcher() {
		directory_Path = Constant.DIRECTORU_PATH;
		image_Path = Constant.IMAGE_PATH;
		try {
			directory = FSDirectory.open(new File(directory_Path));
			reader = IndexReader.open(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IndexSearcher getSearcher() {
		return new IndexSearcher(reader);
	}

	public void search(String field,String name,int num) {
		long startTime=System.currentTimeMillis();
		IndexSearcher searcher=null;
		String[] searchTexts=name.split(" ");
		
		try {
			searcher =getSearcher();
			//searcher.setSimilarity(new DefaultSimilarity());
			searcher.setSimilarity(new LMDirichletSimilarity());
			//searcher.setSimilarity(new BM25Similarity());
			//searcher.setSimilarity(new TFIDFSimilarity());
			
			BooleanQuery query=new BooleanQuery();
			for(int i=0;i<searchTexts.length;i++){
				query.add(new TermQuery(new Term(field,searchTexts[i])),Occur.SHOULD);
			}
			
			TopDocs docs=searcher.search(query, num);
			
			int sum=docs.totalHits;
			//System.out.println("一共查询到了:"+docs.totalHits);
			imagelist=new ArrayList();
			ImageBean image=null;
			int i=0;
			for(ScoreDoc sd : docs.scoreDocs){
				Document doc=searcher.doc(sd.doc);
			
				image=new ImageBean();
				//image.setImagename(Constant.IMAGE_PATH+doc.get("imageName"));
				image.setImagename(Constant.IMAGE_PATH+doc.get("imageName"));				/////////////////////
				image.setScore(sd.score);
				imagelist.add(image);
									
			}
			long endTime=System.currentTimeMillis();
			//System.out.println("\n查询共花费: "+(endTime - startTime)+" 毫秒");
			timeCost=endTime - startTime;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public long getTimeCost() {
		return timeCost;
	}

	public void setTimeCost(long timeCost) {
		this.timeCost = timeCost;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public List getImageList() {
		return imagelist;
	}

	public void setImageList(List imagelist) {
		this.imagelist = imagelist;
	}
	

	
	/**
	 * 测试搜索功能
	 * */
/*	public static void main(String args[]){
		ImageSearcher is=new ImageSearcher();
		
		System.out.println("图片目录地址:"+Constant.IMAGE_PATH+"\n");
		is.search("tag", "apple", 10);
	}*/


}

