package export;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;


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
		System.setProperty("com.sun.net.httpserver.HttpServerProvider",
		        "org.eclipse.jetty.jaxws2spi.JettyHttpServerProvider");
		
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));
		
		/*JettyHttpServerProvider().setServer(server); 
		
		HandlerCollection serverHandlers = new ContextHandlerCollection();
		server.setHandler(serverHandlers);
		
		server.
		
		HandlerCollection handlerCollection = new HandlerCollection();
	    server.setHandler(handlerCollection);
	    
	    String context = "/web/ws";
	    
	    JettyHttpServerProvider httpServerProv = new JettyHttpServerProvider().setServer(server);
	    Endpoint endpoint = Endpoint.create(new XmlExportWS());
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new WebSPublish()),"/*");*/
		System.out.println("http://blooming-sunset-3320.herokuapp.com:"+Integer.valueOf(System.getenv("PORT"))+"/WS/WebS");
		Endpoint.publish("http://blooming-sunset-3320.herokuapp.com:"+Integer.valueOf(System.getenv("PORT"))+"/",new XmlExportWS());
        //server.start();
        //server.join(); 
		
	}
}