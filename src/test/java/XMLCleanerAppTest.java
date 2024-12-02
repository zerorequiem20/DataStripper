import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;
import java.io.StringReader;

public class XMLCleanerAppTest {

    private adm_data_stripper admDataStripper;

    @BeforeEach
    public void setUp() {
        // Initialize the XML cleaner before each test
        admDataStripper = new adm_data_stripper();
    }

    @Test
    public void testRemoveEmptyElements() {
        // Prepare a sample XML documentdddd
        String xml = "<root><emptyElement /><nonEmptyElement>Data</nonEmptyElement><emptyText></emptyText></root>";

        try {
            // Convert string to a Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);

            // Clean the XML
            Element root = doc.getDocumentElement();
            admDataStripper.removeEmptyElements(root);

            // Convert cleaned document to string for verification
            String cleanedXML = admDataStripper.formatXML(doc);

            // Check that the empty elements are removed
            assertFalse(cleanedXML.contains("<emptyElement />"));
            assertFalse(cleanedXML.contains("<emptyText></emptyText>"));
            assertTrue(cleanedXML.contains("<nonEmptyElement>Data</nonEmptyElement>"));

        } catch (Exception e) {
            fail("Exception while testing removeEmptyElements: " + e.getMessage());
        }
    }
//hids
    @Test
    public void testCleanXMLContent_withEmptyElements() {
        String xml = "<root><emptyElement /><nonEmptyElement>Data</nonEmptyElement><emptyText></emptyText></root>";

        try {
            String cleanedXML = admDataStripper.cleanXMLContent(xml);

            // Assert that the empty elements are removed and the rest of the content is intact
            assertTrue(cleanedXML.contains("<nonEmptyElement>Data</nonEmptyElement>"));
            assertFalse(cleanedXML.contains("<emptyElement />"));
            assertFalse(cleanedXML.contains("<emptyText></emptyText>"));

        } catch (Exception e) {
            fail("Exception while testing cleanXMLContent: " + e.getMessage());
        }
    }

    @Test
    public void testCleanXMLContent_withNoEmptyElements() {
        String xml = "<root><nonEmptyElement>Data</nonEmptyElement></root>";

        try {
            String cleanedXML = admDataStripper.cleanXMLContent(xml);

            // Assert that no elements were removed (the content should remain unchanged)
            assertTrue(cleanedXML.contains("<nonEmptyElement>Data</nonEmptyElement>"));

        } catch (Exception e) {
            fail("Exception while testing cleanXMLContent with no empty elements: " + e.getMessage());
        }
    }

    @Test
    public void testFormatXML() {
        // Sample XML input
        String xml = "<root><nonEmptyElement>Data</nonEmptyElement></root>";

        try {
            // Convert string to a Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);

            // Format the XML
            String formattedXML = admDataStripper.formatXML(doc);

            // Assert that the formatted XML contains proper indentation
            assertFalse(formattedXML.contains("\n  <nonEmptyElement>Data</nonEmptyElement>\n"));
        } catch (Exception e) {
            fail("Exception while testing formatXML: " + e.getMessage());
        }
    }

    @Test
    public void testCleanXMLContent_withMalformedXML() {
        String malformedXML = "<root><nonEmptyElement>Data</nonEmptyElement>";

        // Expect an exception to be thrown due to malformed XML
        assertThrows(Exception.class, () -> {
            admDataStripper.cleanXMLContent(malformedXML);
        });
    }

    @Test
    public void testCleanXMLContent_withNullInput() {
        // Test with null input (should throw exception)
        assertThrows(NullPointerException.class, () -> {
            admDataStripper.cleanXMLContent(null);
        });
    }
}
