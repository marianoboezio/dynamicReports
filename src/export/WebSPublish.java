package export;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;


public class WebSPublish extends HttpServlet {

	/*public static void main(String[] args) throws Exception{
		Endpoint.publish("http://sharp-light-3461.herokuapp.com:"++"/WS/WebS",new XmlExportWS());
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	 Endpoint.publish("http://sharp-light-3461.herokuapp.com:"+req.getLocalPort()+"/WS/WebS",new XmlExportWS());
    }*/
	   
	public static void main(String[] args) throws Exception {
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));
		/* ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new WebSPublish()),"/*");*/
		Endpoint.publish("http://sharp-light-3461.herokuapp.com:"+Integer.valueOf(System.getenv("PORT"))+"/WS/WebS",new XmlExportWS());
        server.start();
        server.join(); 
		
	}
}