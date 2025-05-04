import java.io.FileReader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;


public class JsonBuilder {
    public static void convertCSVToJSON() throws CsvValidationException {
    	BuildDataset MainClass= new BuildDataset();
    	
        List<Map<String, String>> data = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(MainClass.csvPath))) {
            String[] headers = reader.readNext();  // Read the header row

            // Read the CSV lines
            String[] line;
            while ((line = reader.readNext()) != null) {
                Map<String, String> map = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    map.put(headers[i], line[i]);  // Create a map with header as key and row data as value
                }
                data.add(map);  // Add the map to the data list
            }

            // Convert the data list to JSON and write to a file
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(MainClass.jsonPath), data);

            System.out.println(" JSON file created: " + MainClass.jsonPath);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while reading the CSV file or writing the JSON file.");
        }
    }
}
