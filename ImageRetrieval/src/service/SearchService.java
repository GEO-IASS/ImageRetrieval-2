package service;

import java.util.List;

import dirSetting.Constant;
import search.ImageSearcher;

public class SearchService {
	private ImageSearcher is=null;
	public SearchService(){
		is=new ImageSearcher();	
	}

	public long getTimeCost(){
		long timeCost=is.getTimeCost();
		return timeCost;
	}
	
	public List getImages(String text){
		is.search("tag", text, 200);
		List images=is.getImageList();
		return images;
	}

}
