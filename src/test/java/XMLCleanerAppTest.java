import org.junit.jupiter.api.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class XMLCleanerAppTest {

    private adm_data_stripper admDataStripper;

    @BeforeEach
    public void setUp() {
        admDataStripper = new adm_data_stripper();
    }

    @Test
    public void testRemoveEmptyElements() throws Exception {
        // Prepare a sample XML string
        String xml = "<root>" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\" MANAGER=\"MAN1\" MPFN02X=\"US\" MPFN13X=\"UA08\" RGROUP=\"0001\" />" +
                "<RWDATA_HEADERS />" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\" MANAGER=\"MAN1\" MPFN02X=\"US\" MPFN13X=\"UA09\" RGROUP=\"0001\" />" +
                "<RWDATA_TEMPLATE />" +
                "<WS_FUNC_ACCESS_GROUP />" +
                "</root>";

        // Parse the XML string into a Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);

        // Get root element
        Element root = doc.getDocumentElement();

        // Remove empty elements
        admDataStripper.removeEmptyElements(root);

        // Convert the cleaned document to string
        String cleanedXML = admDataStripper.formatXML(doc);

        // Verify that the empty tags were removed
        assertTrue(cleanedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\""));
        assertTrue(cleanedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\""));
        assertFalse(cleanedXML.contains("<RWDATA_HEADERS />"));
        assertFalse(cleanedXML.contains("<RWDATA_TEMPLATE />"));
        assertFalse(cleanedXML.contains("<WS_FUNC_ACCESS_GROUP />"));
    }

    @Test
    public void testCleanXMLContent() throws Exception {
        // Prepare a sample XML with some empty elements
        String xml = "<root>" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\" MANAGER=\"MAN1\" MPFN02X=\"US\" MPFN13X=\"UA08\" RGROUP=\"0001\" />" +
                "<RWDATA_HEADERS />" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\" MANAGER=\"MAN1\" MPFN02X=\"US\" MPFN13X=\"UA09\" RGROUP=\"0001\" />" +
                "<RWDATA_TEMPLATE />" +
                "<WS_FUNC_ACCESS_GROUP />" +
                "</root>";

        // Clean the XML content
        String cleanedXML = admDataStripper.cleanXMLContent(xml);

        // Verify that the empty elements were removed
        assertTrue(cleanedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\""));
        assertTrue(cleanedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\""));
        assertFalse(cleanedXML.contains("<RWDATA_HEADERS />"));
        assertFalse(cleanedXML.contains("<RWDATA_TEMPLATE />"));
        assertFalse(cleanedXML.contains("<WS_FUNC_ACCESS_GROUP />"));
    }

    @Test
    public void testFormatXML() throws Exception {
        // Prepare a simple XML document
        String xml = "<root>" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\" MANAGER=\"MAN1\" MPFN02X=\"US\" MPFN13X=\"UA08\" RGROUP=\"0001\" />" +
                "<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\" MANAGER=\"MAN1\" MPFN02X=\"US\" MPFN13X=\"UA09\" RGROUP=\"0001\" />" +
                "</root>";

        // Parse the XML string into a Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);

        // Format the XML using the formatXML method
        String formattedXML = admDataStripper.formatXML(doc);

        // Check if the formatted XML has indentation
        assertTrue(formattedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\""));
        assertTrue(formattedXML.contains("<MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\""));
        assertTrue(formattedXML.contains("  <MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US08\""));
        assertTrue(formattedXML.contains("  <MP_FUND_O BGROUP=\"003\" FUNDCODE=\"US09\""));
    }

    @Test
    public void testEmptyXML() throws Exception {
        // Prepare an XML string with only empty elements
        String xml = "<root>" +
                "<RWDATA_HEADERS />" +
                "<RWDATA_TEMPLATE />" +
                "<WS_FUNC_ACCESS_GROUP />" +
                "</root>";

        // Clean the XML content
        String cleanedXML = admDataStripper.cleanXMLContent(xml);

        // Expected cleaned XML (normalized line breaks)
        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<root/>\n";

        // Normalize line breaks in both actual and expected output
        cleanedXML = cleanedXML.replaceAll("\r\n", "\n"); // Normalize CRLF to LF (Windows-style to Unix-style)
        expectedXML = expectedXML.replaceAll("\r\n", "\n");

        // Verify that all empty elements were removed
        assertEquals(expectedXML, cleanedXML);
    }

}
