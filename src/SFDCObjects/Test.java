package SFDCObjects;

import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filter = "(a0XA0000000qnGYMAY,a0XA0000000qnGgMAI,a0XA0000000qnGfMAI,a0XA0000000qnGbMAI,a0XA0000000qnGZMAY)";
		String[] lstFilters = filter.split(",");
		String ret = "";
		for(String s : lstFilters){
			ret += "'" + s + "',";
		}
		ret = ret.substring(0, ret.length() -1);
		System.out.println(ret);
		System.out.println(ret);
	}
	



}
