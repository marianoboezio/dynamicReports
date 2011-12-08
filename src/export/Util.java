package export;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Util {

	/**
	 * Recive un string con formato XML y devuelve un 
	 * documento XML valido
	 * @param stringXml
	 * @return Document
	 */
	public static Document xmlFormat(String stringXml) {
	    try { 
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	Document document = builder.parse(new InputSource(new StringReader(stringXml)));
	    		        
	        return document;
	    } catch (Exception e) {
	        throw new RuntimeException(e); 
	    }
	}
	
	
	/**
	 * Recive un string separado por coma y devuelve un 
	 * string sin espacios y parseado para correr una sentencia SQL
	 * @param filter
	 * @return String  
	 */
	public static String getFormatedInClause(String filter){		
		String[] lstFilters = filter.split(",");
		String ret = "";
		for(String s : lstFilters){
			ret += "'" + s.trim() + "',";
		}
		return ret.substring(0, ret.length() -1);	
	}
	
	
	/**
	 * Decodes Hex String y lo convierte en un Byte array 
	 * @param s
	 * @return byte[]
	 */
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}
