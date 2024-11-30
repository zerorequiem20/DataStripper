import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;

public class adm_data_stripper extends JFrame {

    private JButton uploadButton;

    public adm_data_stripper() {
        // Setup the frame
        setTitle("XML Cleaner");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the layout and add components
        setLayout(new FlowLayout());

        // Upload button
        uploadButton = new JButton("Upload and Clean XML");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadAndProcessFile();
            }
        });

        add(uploadButton);
    }

    public void uploadAndProcessFile() {
        // Open file chooser to select the XML file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select XML File");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String xmlContent = readFile(file);
                String cleanedXML = cleanXMLContent(xmlContent);

                // Show the save file dialog
                JFileChooser saveChooser = new JFileChooser();
                saveChooser.setDialogTitle("Save Cleaned XML");
                int saveSelection = saveChooser.showSaveDialog(this);

                if (saveSelection == JFileChooser.APPROVE_OPTION) {
                    File saveFile = saveChooser.getSelectedFile();
                    saveFile(saveFile, cleanedXML);
                    JOptionPane.showMessageDialog(this, "Cleaned XML saved successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error processing the file: " + ex.getMessage());
            }
        }
    }

    public String readFile(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public void saveFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }
//cc
    public static void removeEmptyElements(Element element) {
        NodeList children = element.getChildNodes();
        int xyz=5;
        for (int i = 0; i < children.getLength(); i++) {
            // Get the current node
            Node child = children.item(i);

            // Only process element nodes
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeEmptyElements((Element) child);  // Recursively clean children

                // Check if the element is empty: no text, no attributes, no child nodes
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

    public String formatXML(Document doc) throws TransformerException {
        // Create a transformer to pretty-print the XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Set indentation properties
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Create a StringWriter to capture the formatted XML
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.toString();
    }

    public String cleanXMLContent(String xmlContent) throws Exception {
        // Create a DocumentBuilder to parse the XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlContent));
        Document doc = builder.parse(is);

        // Get the root element and clean empty elements
        Element root = doc.getDocumentElement();
        removeEmptyElements(root);

        // Format the cleaned XML
        return formatXML(doc);
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new adm_data_stripper().setVisible(true);
            }
        });
    }
}
