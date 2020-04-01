package back_end;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDocument {
	
	private Element _currentTag;
	
	private XmlDocument(Element element) {
		_currentTag = element;
	}
	
	public XmlDocument(String xmlStr) {
		
		int inxStart = xmlStr.indexOf('<', 0);
		xmlStr = xmlStr.substring(inxStart);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		doc.getDocumentElement().normalize();
		
		_currentTag = doc.getDocumentElement();
	}
	
	public XmlDocument getTagDoc(String tag) {
		Element newElement = (Element) _currentTag.getElementsByTagName(tag).item(0);
		
		if (newElement != null) {
			return new XmlDocument(newElement);
		}
		else {
			return null;
		}
	}
	
	public List<XmlDocument> getTagDocs(String tag) {
		List<XmlDocument> docList = new ArrayList<XmlDocument>();
		NodeList nodes = _currentTag.getElementsByTagName(tag);
		for (int inx = 0; inx < nodes.getLength(); inx++) {
			Node node = nodes.item(inx);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				docList.add(new XmlDocument(element));
			}
		}
		return docList;
	}
	
	public String getDocText() {
		return _currentTag.getTextContent();
	}
	
	public String getTagText(String tag) {
		XmlDocument tagDoc = getTagDoc(tag);
		if (tagDoc != null) {
			return tagDoc.getDocText();
		}
		else {
			return null;
		}
	}
	
	public String getDocString() {
		StringWriter strWriter = new StringWriter();
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transf = tf.newTransformer();
			DOMSource sourceElem = new DOMSource(_currentTag);
			StreamResult streamRes = new StreamResult(strWriter);
		
			transf.transform(sourceElem, streamRes);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		
		return strWriter.toString();
	}
}
