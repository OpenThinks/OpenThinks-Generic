package org.openthinks.generic.util.charset;

import java.io.UnsupportedEncodingException;

public class EncodingParser {
	
	private byte[] temp;
	
	public EncodingParser(String string,String encoding){
		try {
			temp=string.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static String trans(String string){
		
		byte temp[];
		
		try{
			temp=string.getBytes("iso-8859-1");
			string=new String(temp);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return string;
		
	}
	
	public String transform(){
		return new String(temp);
	}
	
	public static String transform(String string,String encoding) {
		
		byte temp[];
		
		try {
			temp=string.getBytes(encoding);
			string=new String(temp);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return string;
	}
}
