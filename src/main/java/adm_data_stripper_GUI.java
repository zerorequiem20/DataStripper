import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class adm_data_stripper_GUI extends JFrame {

    private JButton uploadButton;
    private adm_data_stripper xmlProcessor;  // Reference to XML processor

    public adm_data_stripper_GUI() {
        xmlProcessor = new adm_data_stripper();

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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select XML File");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String xmlContent = readFile(file);
                String cleanedXML = xmlProcessor.cleanXMLContent(xmlContent);

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
}
