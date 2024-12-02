package StructuralClusteringAlgorithmsRelease;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GUI extends JFrame {

    private final JTextField inputFileField, outputFolderField;
    private final JCheckBox directedCheckBox, weightedCheckBox;

    private final Main main;

    public GUI(Main main) {
        this.main = main;

        setTitle("SCAN Program");
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3, 10, 20));

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
        processButton.addActionListener(e -> {
            dispose();
            processFiles();
        });
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

        // If the user didn't put in all input, give them the original window again.
        if (inputFile.isEmpty() || outputFolder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select both input file and output folder.");
            setVisible(true);
            return;
        }

        String message = String.format("Processing data!\nOutput will be located at: %s\n\nSettings:\nInput File: %s\nDirected: %b\nWeighted: %b",
                outputFolder, inputFile, isDirected, isWeighted);
        JOptionPane.showMessageDialog(this, message);

        String[] parameters = new String[]{
                isDirected ? "d=true" : "d=false",
                isWeighted ? "w=true" : "w=false",
                inputFile,
                outputFolder
        };

        // Had some threading issues preventing the program from closing. Disposing again may help?
        dispose();
        main.run(parameters);
    }
}
