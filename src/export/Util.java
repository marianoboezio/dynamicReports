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
}
