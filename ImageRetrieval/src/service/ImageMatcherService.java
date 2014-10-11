package service;

import java.util.List;

import ImageUtils.ImageCoder;
import ImageUtils.Searcher;

public class ImageMatcherService {
	
	//private ImageCoder imcoder=new ImageCoder();
	private Searcher s=new Searcher();
	
	public List getMatch(String filename){
		/*String sourceCode=imcoder.fingerPrinter(filename);
		List mList=imcoder.match(sourceCode);*/
		List mList=s.getSearch(filename);
		return mList;
	}

}
