package export;

import java.io.File;
import java.io.FileNotFoundException;

//import java.io.OutputStream;
import java.io.StringReader;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
//import net.sf.jasperreports.view.JasperViewer;
import javax.xml.ws.soap.MTOM;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;

//@MTOM
//@WebService(endpointInterface = "export.WebS")
public class XmlExportWS {
		
	//@WebMethod
	public DataHandler XmlToPdf(/*@WebParam(name = "xml")*/ String xml) throws FileNotFoundException, JRException {	
			
		try {		
			
			Document xmlOutput = xmlFormat(xml);
			
			// Create Data source 
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput,"root/conflicts/conflict");	 
			
			// Complie Template to .jasper
			JasperReport jasperReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/resources/coverage.jrxml"));
			
			/* JasperPrint is the object contains
			report after result filling process */
			JasperPrint jasperPrint;	 	
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,null,xmlDataSource);
								
			File temp = File.createTempFile("report", "");
			DataHandler dataHandler = new DataHandler(new FileDataSource(temp));
						
			// exports to Pdf file
			JasperExportManager.exportReportToPdfStream(jasperPrint, dataHandler.getOutputStream());//"sample_report.pdf");			
			
			return dataHandler;
			
		} catch(Exception e) {					
			String connectMsg = "Could not create the report " + e.getMessage() + " " + e.getLocalizedMessage();
            System.out.println(connectMsg); 
            e.printStackTrace();
            return null;
		}		
		
		
	};
	
	//@WebMethod
	public DataHandler XmlToXls (String xml) throws FileNotFoundException, JRException {  
		
		try {			
			Document xmlOutput = xmlFormat(xml);
			
			// Create Data source
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput,"root/conflicts/conflict");	 
			
			// Complie Template to .jasper
			System.out.println(this.getClass().getResourceAsStream("coverage.jrxml"));
			JasperReport jasperReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("coverage.jrxml"));
			
			/* JasperPrint is the object contains
			report after result filling process */
			JasperPrint jasperPrint;	 	
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,null,xmlDataSource);
			
			File temp = File.createTempFile("report", "");
			DataHandler dataHandler = new DataHandler(new FileDataSource(temp));
			
			// exports to xls file
			JRXlsExporter exporterXls = new JRXlsExporter ();
			exporterXls.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporterXls.setParameter(JRExporterParameter.OUTPUT_STREAM,dataHandler.getOutputStream());
			exporterXls.exportReport();	
			
			return dataHandler;
			
		} catch(Exception e) {			
			String connectMsg = "Could not create the report " + e.getMessage() + " " + e.getLocalizedMessage();
            System.out.println(connectMsg);
            e.printStackTrace();
            return null;
		}
	};	

	public Document xmlFormat(String input) {
	    try { 
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	Document document = builder.parse(new InputSource(new StringReader(input)));
	    		        
	        return document;
	    } catch (Exception e) {
	        throw new RuntimeException(e); 
	    }
	};	
	
}
