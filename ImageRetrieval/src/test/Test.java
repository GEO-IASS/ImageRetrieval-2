package test;

import java.io.File;



public class Test {
	
	public static void main (String args[]){
		File f=new File("D:/luke/images/");
		int i=0;
		for(File  file: f.listRoots()){
			if(i<50){
				System.out.println(file.getName());
			}
			else{
				break;
			}
			i++;
		}
	}
}
