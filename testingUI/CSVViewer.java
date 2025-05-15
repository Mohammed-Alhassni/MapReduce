import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class CSVViewer {
    public static void showCSVViewer(String csvPath) {
        JFrame frame = new JFrame("CSV Viewer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // So it doesn't exit the whole app

        String[][] data = readCSV(csvPath);

        // You can enhance this to use real headers from the CSV
        String[] columns = {"Column 1", "Column 2", "Column 3", "Column 4", "Column 5", 
                            "Column 6", "Column 7", "Column 8", "Column 9", "Column 10", 
                            "Column 11"};

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        frame.setSize(1080, 720);
        frame.setVisible(true);
    }

    private static String[][] readCSV(String filePath) {
        List<String[]> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                dataList.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList.toArray(new String[0][0]);
    }
}
