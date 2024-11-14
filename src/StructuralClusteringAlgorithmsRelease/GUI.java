package StructuralClusteringAlgorithmsRelease;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GUI extends JFrame {

    private final JTextField inputFileField;
    private final JTextField outputFolderField;
    private final JCheckBox directedCheckBox;
    private final JCheckBox weightedCheckBox;

    private final Main main;

    public GUI(Main main) {
        this.main = main;

        setTitle("SCAN Program");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Input file selection
        add(new JLabel("Input .pairs file:"));
        inputFileField = new JTextField();
        add(inputFileField);
        JButton inputBrowseButton = new JButton("Browse");
        inputBrowseButton.addActionListener(e -> browseFile());
        add(inputBrowseButton);

        // Output folder selection
        add(new JLabel("Output folder:"));
        outputFolderField = new JTextField();
        add(outputFolderField);
        JButton outputBrowseButton = new JButton("Browse");
        outputBrowseButton.addActionListener(e -> browseFolder());
        add(outputBrowseButton);

        // Directed checkbox
        directedCheckBox = new JCheckBox("Directed");
        add(directedCheckBox);

        // Weighted checkbox
        weightedCheckBox = new JCheckBox("Weighted");
        add(weightedCheckBox);

        // Process button
        JButton processButton = new JButton("Process");
        processButton.addActionListener(e -> processFiles());
        add(processButton);

        setVisible(true);
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "PAIRS files", "pairs"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            inputFileField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void browseFolder() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = folderChooser.showDialog(this, "Select Folder");

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
            outputFolderField.setText(selectedFolder.getAbsolutePath());
        }
    }

    private void processFiles() {
        String inputFile = inputFileField.getText();
        String outputFolder = outputFolderField.getText();
        boolean isDirected = directedCheckBox.isSelected();
        boolean isWeighted = weightedCheckBox.isSelected();

        if (inputFile.isEmpty() || outputFolder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select both input file and output folder.");
            return;
        }

        // Here you would add the logic to process the files
        // For now, we'll just show a message with the selected options
        String message = String.format("Processing:\nInput: %s\nOutput Folder: %s\nDirected: %b\nWeighted: %b",
                inputFile, outputFolder, isDirected, isWeighted);
        JOptionPane.showMessageDialog(this, message);

        String[] parameters = new String[]{
                isDirected ? "d=true" : "d=false",
                isWeighted ? "w=true" : "w=false",
                inputFile,
                outputFolder
        };

        main.run(parameters);
    }
}
