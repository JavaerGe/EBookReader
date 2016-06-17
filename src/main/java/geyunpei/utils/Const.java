package geyunpei.utils;

import java.util.ArrayList;
import java.util.List;

public class Const {
	
	public final static Integer SUCCESS = 0;
	
	public final static Integer FAIL = -1;
	
	public final static Integer EXPIRED = -2;
	
	public final static Integer NO_DATA = 1;
	
	public static List<String> getLogoType() {
		List<String> typeList = new ArrayList<>();
		typeList.add(".jpg");
		typeList.add(".jpeg");
		typeList.add(".png");
		typeList.add(".bmp");
		return typeList;
	}
	
	public static List<String> getBookType() {
		List<String> typeList = new ArrayList<>();
		typeList.add(".txt");
		return typeList;
	}
	
}
