package export;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Endpoint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

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
		
		String xml = "<root><User>Travis Brooks 85.19 % Coverage Conflict</User><conflicts><conflict><acount> Ag Workers Ins Grp </acount><ccoverage> Y </ccoverage>" +
				"<eemployee> O Hennesey, Edward </eemployee><productt> US-HG </productt><productionytd> $197025.91 </productionytd><prospectiveEmpR></prospectiveEmpR><exitingEmpR></exitingEmpR>"+			
				"</conflict></conflicts></root>";
		XmlExportWS tmp = new XmlExportWS();
		
		try {		
			
			DataHandler dh = tmp.XmlToXls (xml);
			 FileInputStream fileInputStream=null;
			 
		    
		 
		       byte[] bFile = new byte[((FileInputStream)dh.getContent()).available()];
		 
		       try {
		           //convert file into array of bytes
		           fileInputStream = ((FileInputStream)dh.getContent());
		           fileInputStream.read(bFile);
		           fileInputStream.close();
		 
		           for (int i = 0; i < bFile.length; i++) {
		                      resp.getWriter().print((char)bFile[i]);
		           }
		 
		           System.out.println("Done");
		       }catch(Exception e){
		               e.printStackTrace();
		       }
			//resp.getWriter().println(dh);
			//dh.writeTo(resp.getOutputStream());			
			//resp.getWriter().println(dh.getInputStream());
			
			/*InputStream in = null;
			byte out[] = null;
			in = dh.getInputStream();
			if(in != null) {
				out = new byte[in.available()];
				/*for (int i=0; i<in.available(); i++){
					resp.getWriter().print(out[i]);
				}
				in.read(out);				
			} else {
				out = new byte[0];
			}			*/
			//resp.getWriter().println(dh.getContent().toString());
			//resp.getWriter().println(((FileInputStream)dh.getContent()));
			//resp.getWriter().println(((FileInputStream)dh.getContent()).toString());
			
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
			
			File temp = File.createTempFile("report", "");
			DataHandler dataHandler = new DataHandler(new FileDataSource(temp));
			
			OutputStream ouputStream= new FileOutputStream(File.createTempFile("report", ""));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			
			// exports to xls file
			JRXlsExporter exporterXls = new JRXlsExporter ();
			exporterXls.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporterXls.setParameter(JRExporterParameter.OUTPUT_STREAM,  byteArrayOutputStream);
			exporterXls.exportReport();
			
			resp.setContentType("application/octet-stream");
			//resp.setContentLength(byteArrayOutputStream.toByteArray().length);
			resp.setHeader("Content-disposition",
					"attachment;filename=report.xls");
						
			ouputStream.write(byteArrayOutputStream.toByteArray());
			ouputStream.flush();
			ouputStream.close();
			
			resp.getOutputStream().write(byteArrayOutputStream.toByteArray());
			/*for (int i = fis.read(); i != -1; i = fis.read()) {  
				sos.write((byte) i);  
			}
		
			FileInputStream fileInputStream = null;
			 
			byte[] bFile = new byte[((FileInputStream)dataHandler.getContent()).available()];
	 
			//convert file into array of bytes
			((FileInputStream) dataHandler.getContent()).read(bFile);
			((FileInputStream) dataHandler.getContent()).close();

			for (int i = 0; i < bFile.length; i++) {
				resp.getWriter().print((char)bFile[i]);
			}*/
 
			//System.out.println("Done");

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
	
	
	 /*  
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String xml = "<root><User>Travis Brooks 85.19 % Coverage Conflict</User><conflicts><conflict><acount> Ag Workers Ins Grp </acount><ccoverage> Y </ccoverage>" +
					"<eemployee> O Hennesey, Edward </eemployee><productt> US-HG </productt><productionytd> $197025.91 </productionytd><prospectiveEmpR></prospectiveEmpR><exitingEmpR></exitingEmpR>"+			
					"</conflict></conflicts></root>";
		XmlExportWS tmp = new XmlExportWS();		
		
		try { 
			
			DataHandler dh = tmp.XmlToXls (xml);
			 FileInputStream fileInputStream=null;
			 
			    
			 
		       byte[] bFile = new byte[((FileInputStream)dh.getContent()).available()];
		 
		       try {
		           //convert file into array of bytes
		           fileInputStream = ((FileInputStream)dh.getContent());
		           fileInputStream.read(bFile);
		           fileInputStream.close();
		 
		           for (int i = 0; i < bFile.length; i++) {
		                      resp.getWriter().print((char)bFile[i]);
		           }
		 
		           System.out.println("Done");
		       }catch(Exception e){
		               e.printStackTrace();
		       }
			//resp.getWriter().println(dh);
			//dh.writeTo(resp.getOutputStream());			
			//resp.getWriter().println(dh.getInputStream());
			
			/*InputStream in = null;
			byte out[] = null;
			in = dh.getInputStream();
			if(in != null) {
				out = new byte[in.available()];
				/*for (int i=0; i<in.available(); i++){
					resp.getWriter().print(out[i]);
				}*
				in.read(out);				
			} else {
				out = new byte[0];
			}			
			
			dh.writeTo(resp.getOutputStream());
			System.out.println(dh.toString());
			
			resp.getWriter().println(dh.toString());
			resp.getWriter().println(dh.getInputStream());
			resp.getWriter().println(dh.getDataSource());
			
			//resp.getWriter().println(new String(out, "UTF-8"));			
			
			*
		} catch (Exception e) {
			
			e.printStackTrace();
		}	
		
	}*/



	public static void main(String[] args) throws Exception {
		
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));		
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        context.addServlet(new ServletHolder(new WebSPublish()),"/*");

        server.start();
        server.join(); 
		
	}
}