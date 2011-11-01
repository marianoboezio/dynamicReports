package export;

import javax.xml.ws.Endpoint;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import javax.activation.DataHandler;


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
			DataHandler dh = tmp.XmlToXls (xml);
			//resp.getWriter().println(dh);
			//dh.writeTo(resp.getOutputStream());			
			//resp.getWriter().println(dh.getInputStream());
			
			InputStream in = null;
			byte out[] = null;
			in = dh.getInputStream();
			if(in != null) {
				out = new byte[in.available()];
				/*for (int i=0; i<in.available(); i++){
					resp.getWriter().print(out[i]);
				}*/
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
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}	
	}



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