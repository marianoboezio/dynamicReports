package export;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface WebS {
	 @WebMethod DataHandler XmlToPdf(@WebParam(name = "xml") String xml);
	 
	 @WebMethod DataHandler XmlToXls(@WebParam(name = "xml") String xml);	 
}