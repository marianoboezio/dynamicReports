package export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.w3c.dom.Document;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectorConfig;

import export.Base64;

public class WebSPublish extends HttpServlet {

    List<String> filterProducts = new ArrayList<String>();
    
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { 		
	
	/*System.out.println(req.getParameter("user") + "\n\n " + req.getParameter("pass"));
	String USERNAME = req.getParameter("user");
	String PASSWORD = req.getParameter("pass");
	String type = req.getParameter("type");
	String reportID = req.getParameter("reportID");*/
	
	String EncrypetedUSERNAME = req.getParameter("user");
	String EncryptedPASSWORDKEY = req.getParameter("pass");
	String EncryptedTOKEN = req.getParameter("token");
	String type = req.getParameter("type");
	String reportID = req.getParameter("reportID");
	
	/*String[] keypass = EncryptedPASSWORDKEY.split("\\+++---");
	String EncryptedPASSWORD = keypass[0];
	String key = keypass[1];*/
	
	Integer length = Integer.valueOf(EncryptedPASSWORDKEY.substring(EncryptedPASSWORDKEY.length() - 2));
	String key = EncryptedPASSWORDKEY.substring(length , EncryptedPASSWORDKEY.length() - 3);
	String EncryptedPASSWORD = EncryptedPASSWORDKEY.substring(0 , length - 1);
	
	System.out.println("######################## Encrypted CREDENTIALS ########################");
	//System.out.println("LENGTH ---->" + length);
	System.out.println("KEY ---->" + Base64.decode(key).toString());
	System.out.println("EncryptedUSERNAME ---->" + Base64.decode(EncrypetedUSERNAME).toString());
	System.out.println("EcryptedPASSWORD ---->" + Base64.decode(EncryptedPASSWORD).toString());
	System.out.println("EncryptedTOKEN ---->" + Base64.decode(EncryptedTOKEN).toString());
	
	PartnerConnection connection;
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	
	
	try { 		
			
		SecretKeySpec secretkey = new SecretKeySpec(Base64.decode(key), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
		cipher.init(Cipher.DECRYPT_MODE, secretkey);
		
		System.out.println("######################## Decrypting CREDENTIALS ########################");			
		
		String TOKEN = new String(cipher.doFinal(Base64.decode(EncryptedTOKEN)));
		System.out.println("TOKEN ---->" + TOKEN);
		
		byte[] decodedUsername = Base64.decode(EncrypetedUSERNAME);
		String USERNAME = new String(cipher.doFinal(decodedUsername));
		System.out.println("USERNAME ---->" + USERNAME);
		
		String PASSWORD = new String(cipher.doFinal(Base64.decode(EncryptedPASSWORD)));
		System.out.println("PASSWORD ---->" + PASSWORD);
				
		ConnectorConfig config = new ConnectorConfig();
	    config.setUsername(USERNAME);
	    // config.setPassword(PASSWORD);
	    config.setPassword(PASSWORD + TOKEN);
	    
	    connection = Connector.newConnection(config);   
	    QueryResult queryResults =  connection.query("SELECT o.Row_HTML__c, o.Object_Export_Excel__c FROM Object_Row__c o WHERE o.Object_Export_Excel__c = '" + reportID + "' ORDER BY o.name");	
	    
	    System.out.println("######################## START ########################");
	    
	    String xml = "<root>";	
	    for (SObject s : queryResults.getRecords()) { 
	    	
	    	System.out.println("######################## Looping ########################");
	    	if(s.getField("Row_HTML__c") != null){
	    		xml += s.getField("Row_HTML__c").toString();	
	    	}	    	
		}
	    
	    xml += "</root>";	 	   
	    System.out.println("XML --------------------------------------->" + xml); 
	    
	    System.out.println("######################## XML ########################");
	    Document xmlOutput = Util.xmlFormat(xml.replaceAll("&", " "));
	    
	    /* JasperPrint is the object contains
		report after result filling process */
		JasperPrint jasperPrint = null;
	    
	    if (type.equals("NonPerformer")) {
		    // Create Data source
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput, "root/NonPerformingAccounts/Row");	
			
			// Complie Template to .jasper
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			JasperReport jasperReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("NonPerformingAccounts.jrxml"));
			
			// Compilamos el sub reporte
			JasperReport jasperSubReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("NonPerformingAccounts_subreport1.jrxml"));				 	
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("SubReportParam", jasperSubReport);
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,param,xmlDataSource);
	    } else if (type.equals("productionBySalesperson")){
	    	JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput, "root/content");	
			
			// Complie Template to .jasper
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			JasperReport jasperReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("productionBySalesperson.jrxml"));
			
			// Compilamos el sub reporte
			JasperReport jasperSubReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("productionBySalesperson_subreport1.jrxml"));
			JasperReport jasperSubReport2 = JasperCompileManager.compileReport(classLoader.getResourceAsStream("productionBySalesperson_subreport2.jrxml"));
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("SubReportParam", jasperSubReport);
			param.put("SubReportParam2", jasperSubReport2);
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,param,xmlDataSource);
	    	
	    } else if (type.equals("productionByBook")){
	    	JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput, "root/ProductionReportBook/Row");	
	    	System.out.println("###################Book########################");
			// Complie Template to .jasper
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			JasperReport jasperReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("productionByBook.jrxml"));
			
			// Compilamos el sub reporte
			JasperReport jasperSubReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("productionByBook_subreport1.jrxml"));
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("SubReportParam", jasperSubReport);
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,param,xmlDataSource);
	    	
	    } else if (type.equals("productionByProduct")){
	    	JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput, "root/content");	
			
			// Complie Template to .jasper
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			JasperReport jasperReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("productionByProduct.jrxml"));
			
			// Compilamos el sub reporte
			JasperReport jasperSubReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("productionByProduct_subreport1.jrxml"));
			JasperReport jasperSubReport2 = JasperCompileManager.compileReport(classLoader.getResourceAsStream("productionByProduct_subreport2.jrxml"));
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("SubReportParam", jasperSubReport);
			param.put("SubReportParam2", jasperSubReport2);
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,param,xmlDataSource);
	    	
	    } else if (type.equals("coverage")){
	    	JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput, "root/conflicts/conflict");	
			
			// Complie Template to .jasper
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			JasperReport jasperReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("coverage.jrxml"));
						
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,null,xmlDataSource);
	    	
	    }			
		
		//resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("content-type","application/vnd.ms-excel#report.xls");
		resp.setContentType("application/x-msdownload");
		resp.setHeader("Content-Disposition",
				 "attachment; filename=report.xls"); 
		resp.setDateHeader ("Expires", 0);
		
		// exports to xls file
		JRXlsExporter exporterXls = new JRXlsExporter ();
		exporterXls.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporterXls.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream); 
		exporterXls.exportReport();		
		
		System.out.println(Base64.encodeBytes(byteArrayOutputStream.toByteArray()).getBytes());		
		resp.getOutputStream().write(Base64.encodeBytes(byteArrayOutputStream.toByteArray()).getBytes());
		System.out.println("######################## Finish ########################");
		
		} catch (Exception e) {			
			e.printStackTrace();			
		}		
    }
	
	@Override	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*String xml = "<root><User>Travis Brooks 85.19 % Coverage Conflict</User><conflicts><conflict><acount> Ag Workers Ins Grp </acount><ccoverage> Y </ccoverage>" +
					"<eemployee> O Hennesey, Edward </eemployee><productt> US-HG </productt><productionytd> $197025.91 </productionytd><prospectiveEmpR></prospectiveEmpR><exitingEmpR></exitingEmpR>"+			
					"</conflict></conflicts></root>";*/		
		
		try {
			String xml = req.getParameter("xml");
			
			System.out.println("\n\n\n" + req.getRequestURL() + "\n" + req.toString() + "\n\n\n");
			System.out.println("\n\n\n" + req.getPathInfo() + "\n" + req.toString() + "\n\n\n");
			System.out.println("\n\n\n" + req.getHeader("xml") + "\n" + req.toString() + "\n\n\n");
			
			System.out.println("PARAMETER HERE ---------------> " + xml);
			Document xmlOutput = Util.xmlFormat(xml);			
			
			// Create Data source
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput, "root/content");	 
			
			// Complie Template to .jasper
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			System.out.println(classLoader.getResourceAsStream("production.jrxml"));
			JasperReport jasperReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("production.jrxml"));

			/* JasperPrint is the object contains
			report after result filling process */
			JasperPrint jasperPrint;	 	
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,null,xmlDataSource);
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			
			resp.setContentType("application/vnd.ms-excel");
			resp.setHeader("Content-Disposition",
					 "attachment; filename=report.xls");
			
			// exports to xls file
			JRXlsExporter exporterXls = new JRXlsExporter ();
			exporterXls.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporterXls.setParameter(JRExporterParameter.OUTPUT_STREAM,  byteArrayOutputStream);
			exporterXls.exportReport();			
			
			resp.getOutputStream().write(Base64.encodeBytes(byteArrayOutputStream.toByteArray()).getBytes());			

		} catch (Exception e) {
			
			e.printStackTrace();
		}	
		
	}	
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));		
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        context.addServlet(new ServletHolder(new WebSPublish()),"/toxls");

        server.start();
        server.join(); 
		
	}
}