package ImageUtils;

import java.io.File;

import dirSetting.Constant;
import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.indexing.parallel.ParallelIndexer;

public class ParallelIndexing {
	
	public static void main(String args[]){
		String imagePath=Constant.IMAGE_PATH;
		String contentIndex=Constant.CONTENT_INDEX;
		
		File f=new File(imagePath);
		if(!(f.exists() && f.isDirectory())){
			System.out.println("����ͼƬ��Ŀ¼,�������!!!");
			System.exit(1);
		}
		
		ParallelIndexer indexer=new ParallelIndexer(6,contentIndex,imagePath){
			public void addBuilders(ChainedDocumentBuilder builder){
				builder.addBuilder(DocumentBuilderFactory.getCEDDDocumentBuilder());
				builder.addBuilder(DocumentBuilderFactory.getFCTHDocumentBuilder());
				builder.addBuilder(DocumentBuilderFactory.getAutoColorCorrelogramDocumentBuilder());
			}
		};
		indexer.run();
		System.out.println("Content-Based image index completed");
	}

}
