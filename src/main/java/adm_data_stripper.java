import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;

public class adm_data_stripper {

    // This is the XML processing logic that doesn't need the GUI
    public static void removeEmptyElements(Element element) {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeEmptyElements((Element) child);  // Recursively clean children

                if ((child.getTextContent().isEmpty() || child.getTextContent().trim().isEmpty())
                        && child.getAttributes().getLength() == 0
                        && child.getChildNodes().getLength() == 0) {
                    element.removeChild(child);  // Remove empty element
                    i--;  // Decrease index since a child was removed
                }
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                // Handle text nodes, remove them if they are empty or only whitespace
                if (child.getTextContent().isEmpty() || child.getTextContent().trim().isEmpty()) {
                    element.removeChild(child);
                    i--;  // Decrease index since a child was removed
                }
            }
        }
    }
    //heiu3heweaaaefdewwefedrfrfedefeffewwef

    public String formatXML(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.toString();
    }

    public String cleanXMLContent(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlContent));
        Document doc = builder.parse(is);

        Element root = doc.getDocumentElement();
        removeEmptyElements(root);

        return formatXML(doc);
    }

    // Main method for GUI usage
    public static void main(String[] args) {
        // Run the application
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new adm_data_stripper_GUI().setVisible(true); // New GUI class
            }
        });
    }
}
