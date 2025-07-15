import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainUI {
    private static JTable previewTable;
    private static JComboBox<String> columnSelector;
    private static JTextField filterField;
    private static JButton filterButton;
    private static JButton resetButton;
    

    public static void main(String[] args) {
    	
        JFrame frame = new JFrame("Hassani MapReducer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // Top Panel with Buttons
        JPanel topPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        JButton sampleBtn = new JButton("Choose Sample File");
        JButton runBtn = new JButton("Run Builder");
        topPanel.add(sampleBtn);
        topPanel.add(runBtn);


        // Center Panel for Preview and Filter
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        previewTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(previewTable);

        // Filter Panel on top of table
        JPanel filterPanel = new JPanel();
        columnSelector = new JComboBox<>();
        columnSelector.setEnabled(false);
        filterField = new JTextField(20);
        filterButton = new JButton("Apply Filter");
        resetButton = new JButton("Reset Filter");

        filterPanel.add(new JLabel("Column:"));
        filterPanel.add(columnSelector);
        filterPanel.add(new JLabel("Value:"));
        filterPanel.add(filterField);
        filterPanel.add(filterButton);
        filterPanel.add(resetButton);

        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        // File paths
        String[] samplePath = new String[1];
        String[] csvPath = new String[1];

        sampleBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                samplePath[0] = chooser.getSelectedFile().getAbsolutePath();
            }
        });

        runBtn.addActionListener(e -> {
            if (samplePath[0] != null) {
                try {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = chooser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File folder = chooser.getSelectedFile();
                        File file = new File(folder.getAbsolutePath() + "/Dataset.csv");
                        if (file.exists()) {
                        	JOptionPane.showMessageDialog(frame, "CSV is already exist.");
                        	csvPath[0] = folder.getAbsolutePath() + "/Dataset.csv";
                        	
                        } else {
                        	csvPath[0] = folder.getAbsolutePath() + "/Dataset.csv";
                        	System.out.println("Selected CSV Path: " + csvPath[0]);
                        	BuildDataset builder = new BuildDataset(samplePath[0], csvPath[0]);
                            builder.process();
                            JOptionPane.showMessageDialog(frame, "Processing complete.");
                            
                        }                     
                    }
                    loadPreview(csvPath[0]);                 
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select the sample file first.");
            }
        });


        filterButton.addActionListener(e -> {
            String column = (String) columnSelector.getSelectedItem();
            String keyword = filterField.getText();
            if (csvPath[0] != null && column != null && !keyword.isEmpty()) {
                applyAdvancedFilter(csvPath[0], column, keyword);
            }
        });

        resetButton.addActionListener(e -> {
            if (csvPath[0] != null) {
                loadPreview(csvPath[0]);
            }
        });

        frame.setVisible(true);
    }

    private static void loadPreview(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String headerLine = br.readLine();
            if (headerLine == null) return;

            String[] headers = headerLine.split(",");
            columnSelector.removeAllItems();
            for (String h : headers) columnSelector.addItem(h);
            columnSelector.setEnabled(true);

            List<String[]> rows = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(",", -1));
            }

            DefaultTableModel model = new DefaultTableModel(to2DArray(rows), headers);
            previewTable.setModel(model);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void applyAdvancedFilter(String path, String columnName, String keyword) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] headers = br.readLine().split(",");
            int colIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase(columnName)) {
                    colIndex = i;
                    break;
                }
            }
            if (colIndex == -1) return;

            List<String[]> filteredRows = new ArrayList<>();
            String line;
            boolean isNumericComparison = keyword.startsWith(">") || keyword.startsWith("<") || keyword.startsWith("=");
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                if (colIndex >= values.length) continue;

                String cell = values[colIndex].trim();
                String lowerCell = cell.toLowerCase();
                String lowerKeyword = keyword.toLowerCase();

                boolean match = false;

                if (isNumericComparison) {
                    try {
                        double cellVal = Double.parseDouble(cell.replace("%", ""));
                        String expr = keyword.replace("%", "").trim();
                        if (expr.startsWith(">=")) {
                            match = cellVal >= Double.parseDouble(expr.substring(2));
                        } else if (expr.startsWith("<=")) {
                            match = cellVal <= Double.parseDouble(expr.substring(2));
                        } else if (expr.startsWith(">")) {
                            match = cellVal > Double.parseDouble(expr.substring(1));
                        } else if (expr.startsWith("<")) {
                            match = cellVal < Double.parseDouble(expr.substring(1));
                        } else if (expr.startsWith("=")) {
                            match = cellVal == Double.parseDouble(expr.substring(1));
                        }
                    } catch (NumberFormatException ignored) {}
                } else {
                    match = lowerCell.contains(lowerKeyword);
                }

                if (match) filteredRows.add(values);
            }

            DefaultTableModel model = new DefaultTableModel(to2DArray(filteredRows), headers);
            previewTable.setModel(model);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String[][] to2DArray(List<String[]> list) {
        return list.toArray(new String[0][0]);
    }
}