package bean;

public class ImageBean{

	    private String imagename;  
	    private String label;
	    private float score;
		private int difference;  
		
		public ImageBean(){
			super();
		}
	    public ImageBean(String imagename, int difference) {
			this.imagename = imagename;
			this.difference = difference;
		}
	    
		public String getImagename() {
			return imagename;
		}

		public void setImagename(String imagename) {
			this.imagename = imagename;
		}
		 public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public float getScore() {
			return score;
		}

		public void setScore(float score) {
			this.score = score;
		}

		public int getDifference() {
			return difference;
		}
		public void setDifference(int difference) {
			this.difference = difference;
		} 
		
}
