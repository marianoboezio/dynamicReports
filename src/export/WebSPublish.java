package export;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
import org.xml.sax.InputSource;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

public class WebSPublish extends HttpServlet {
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { 		
		
	/*String xml = "<root><User>Travis Brooks 85.19 % Coverage Conflict</User><conflicts><conflict><acount> Ag Workers Ins Grp </acount><ccoverage> Y </ccoverage>" +
				 "<eemployee> O Hennesey, Edward </eemployee><productt> US-HG </productt><productionytd> $197025.91 </productionytd><prospectiveEmpR></prospectiveEmpR><exitingEmpR></exitingEmpR>"+			
				 "</conflict></conflicts></root>";*/	
	try { 		
		String xml = req.getParameter("xml");
		
		System.out.println();System.out.println();
		
		for (Enumeration e = req.getParameterNames() ; e.hasMoreElements();) {
		       System.out.println(e.nextElement());	
		}
		
		System.out.println(req.toString());
		System.out.println();System.out.println();
		
		System.out.println("PARAMETER HERE ---------------> " + xml);
		
		Document xmlOutput = xmlFormat(xml);
		
		// Create Data source
		JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput, "root/conflicts/conflict");	 
		
		// Complie Template to .jasper
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		JasperReport jasperReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("coverage.jrxml"));

		/* JasperPrint is the object contains
		report after result filling process */
		JasperPrint jasperPrint;	 	
		
		// filling report with data from data source
		jasperPrint = JasperFillManager.fillReport(jasperReport,null,xmlDataSource);
		
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition",
				 "attachment; filename=report.xls"); 
		resp.setDateHeader ("Expires", 0);
		
		ServletOutputStream out = resp.getOutputStream();
		
		// exports to xls file
		JRXlsExporter exporterXls = new JRXlsExporter ();
		exporterXls.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporterXls.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		exporterXls.exportReport();
			
		} catch (Exception e) {			
			e.printStackTrace();
			
		}		
    }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String xml = "<root><User>Travis Brooks 85.19 % Coverage Conflict</User><conflicts><conflict><acount> Ag Workers Ins Grp </acount><ccoverage> Y </ccoverage>" +
					"<eemployee> O Hennesey, Edward </eemployee><productt> US-HG </productt><productionytd> $197025.91 </productionytd><prospectiveEmpR></prospectiveEmpR><exitingEmpR></exitingEmpR>"+			
					"</conflict></conflicts></root>";
		XmlExportWS tmp = new XmlExportWS();		
		
		try { 			
			Document xmlOutput = xmlFormat(xml);
			
			// Create Data source
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlOutput, "root/conflicts/conflict");	 
			
			// Complie Template to .jasper
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			System.out.println(classLoader.getResourceAsStream("coverage.jrxml"));
			JasperReport jasperReport = JasperCompileManager.compileReport(classLoader.getResourceAsStream("coverage.jrxml"));

			/* JasperPrint is the object contains
			report after result filling process */
			JasperPrint jasperPrint;	 	
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,null,xmlDataSource);
			
			File temp = File.createTempFile("report.xls", "");
			DataHandler dataHandler = new DataHandler(new FileDataSource(temp));
			
			OutputStream ouputStream= new FileOutputStream(File.createTempFile("report.xls", ""));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			
			resp.setContentType("application/vnd.ms-excel");
			resp.setHeader("Content-Disposition",
					 "attachment; filename=report.xls");
			
			// exports to xls file
			JRXlsExporter exporterXls = new JRXlsExporter ();
			exporterXls.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporterXls.setParameter(JRExporterParameter.OUTPUT_STREAM,  byteArrayOutputStream);
			exporterXls.exportReport();			
			
			resp.getOutputStream().write(byteArrayOutputStream.toByteArray());
			resp.getOutputStream().flush();
			resp.getOutputStream().close();				

		} catch (Exception e) {
			
			e.printStackTrace();
		}	
		
	}

	public Document xmlFormat(String input) {
	    try { 
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	Document document = builder.parse(new InputSource(new StringReader(input)));
	    		        
	        return document;
	    } catch (Exception e) {
	        throw new RuntimeException(e); 
	    }
	}

	public static void main(String[] args) throws Exception {
		
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));		
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/toxls");
        server.setHandler(context);
        
        context.addServlet(new ServletHolder(new WebSPublish()),"/toxls");

        server.start();
        server.join(); 
		
	}
}