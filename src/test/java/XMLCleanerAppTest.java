import org.junit.jupiter.api.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class XMLCleanerAppTest {

    private adm_data_stripper admDataStripper;

    @BeforeEach
    public void setUp() {
        admDataStripper = new adm_data_stripper(); // Only test the business logic methods
    }

    @Test
    public void testRemoveEmptyElements() throws Exception {
        // Sample XML string with empty elements
        String xml = "<root>" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\" MANAGER=\"MAN1\" />" +
                "<RWDATA_HEADERS />" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\" MANAGER=\"MAN1\" />" +
                "<RWDATA_TEMPLATE />" +
                "<WS_FUNC_ACCESS_GROUP />" +
                "</root>";

        // Parse the XML string into a Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);

        // Get the root element and remove empty elements
        Element root = doc.getDocumentElement();
        adm_data_stripper.removeEmptyElements(root);

        // Convert the cleaned document to string
        StringWriter writer = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String cleanedXML = writer.toString();

        // Assert that empty elements are removed
        assertTrue(cleanedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\""));
        assertTrue(cleanedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\""));
        assertFalse(cleanedXML.contains("<RWDATA_HEADERS"));
        assertFalse(cleanedXML.contains("<RWDATA_TEMPLATE"));
        assertFalse(cleanedXML.contains("<WS_FUNC_ACCESS_GROUP"));
    }

    @Test
    public void testCleanXMLContent() throws Exception {
        // Sample XML string with empty elements
        String xml = "<root>" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\" MANAGER=\"MAN1\" />" +
                "<RWDATA_HEADERS />" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\" MANAGER=\"MAN1\" />" +
                "<RWDATA_TEMPLATE />" +
                "<WS_FUNC_ACCESS_GROUP />" +
                "</root>";

        // Clean the XML content
        String cleanedXML = admDataStripper.cleanXMLContent(xml);

        // Assert that empty elements are removed
        assertTrue(cleanedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\""));
        assertTrue(cleanedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\""));
        assertFalse(cleanedXML.contains("<RWDATA_HEADERS"));
        assertFalse(cleanedXML.contains("<RWDATA_TEMPLATE"));
        assertFalse(cleanedXML.contains("<WS_FUNC_ACCESS_GROUP"));
    }

    @Test
    public void testFormatXML() throws Exception {
        // Sample unformatted XML string
        String xml = "<root><MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\" MANAGER=\"MAN1\" /><MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\" MANAGER=\"MAN1\" /></root>";

        // Parse the XML string into a Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);

        // Format the XML
        String formattedXML = admDataStripper.formatXML(doc);

        // Assert that the formatted XML has indentation
        assertTrue(formattedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\""));
        assertTrue(formattedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\""));
        assertTrue(formattedXML.contains("  <MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\""));
        assertTrue(formattedXML.contains("  <MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\""));
    }

    @Test
    public void testEmptyXML() throws Exception {
        // Sample XML with only empty elements
        String xml = "<root>" +
                "<RWDATA_HEADERS />" +
                "<RWDATA_TEMPLATE />" +
                "<WS_FUNC_ACCESS_GROUP />" +
                "</root>";

        // Clean the XML content
        String cleanedXML = admDataStripper.cleanXMLContent(xml);

        // Expected cleaned XML
        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<root/>\n";

        // Assert that all empty elements are removed
        assertEquals(expectedXML.replaceAll("\r\n", "\n"), cleanedXML.replaceAll("\r\n", "\n"));
    }
}
