package export;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class WebSPublish extends HttpServlet {

	/*public static void main(String[] args) throws Exception{
		Endpoint.publish("http://sharp-light-3461.herokuapp.com:"++"/WS/WebS",new XmlExportWS());
	}*/
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {   
		Endpoint.publish("/WebS:" + Integer.valueOf(System.getenv("PORT")),new XmlExportWS());
    }
	   
	public static void main(String[] args) throws Exception {
		//System.setProperty("com.sun.net.httpserver.HttpServerProvider",
		//        "org.eclipse.jetty.jaxws2spi.JettyHttpServerProvider");
		
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));
		
		/*JettyHttpServerProvider().setServer(server); 
		
		HandlerCollection serverHandlers = new ContextHandlerCollection();
		server.setHandler(serverHandlers);
		
		server.
		
		HandlerCollection handlerCollection = new HandlerCollection();
	    server.setHandler(handlerCollection);
	    
	    String context = "/web/ws";
	    
	    JettyHttpServerProvider httpServerProv = new JettyHttpServerProvider().setServer(server);
	    Endpoint endpoint = Endpoint.create(new XmlExportWS());*/
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new WebSPublish()),"/*");
		/*server.s
		XmlExportWS implementor = new XmlExportWS();
		JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
		svrFactory.setServiceClass(XmlExportWS.class);
		svrFactory.setAddress("http://blooming-sunset-3320.herokuapp.com:"+Integer.valueOf(System.getenv("PORT"))+"/WS/WebS");
		svrFactory.setServiceBean(implementor);
		svrFactory.create();
		System.out.println("http://blooming-sunset-3320.herokuapp.com:"+Integer.valueOf(System.getenv("PORT"))+"/WS/WebS");
		Endpoint.publish("http://blooming-sunset-3320.herokuapp.com:"+Integer.valueOf(System.getenv("PORT"))+"/", implementor);*/
        server.start();
        server.join(); 
		
	}
}