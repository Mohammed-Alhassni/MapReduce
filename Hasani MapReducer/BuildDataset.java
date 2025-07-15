import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.opencsv.exceptions.CsvValidationException;

public class BuildDataset {
    public static String samplePath;
    public static String csvPath;
    public static String jsonPath;
    
    public BuildDataset() {

    }
    
    public BuildDataset(String samplePath, String csvPath) {
        BuildDataset.samplePath = samplePath;
        BuildDataset.csvPath = csvPath;
        BuildDataset.jsonPath =  BuildDataset.csvPath.replace(".csv", ".json");
    }

    public void process() throws InterruptedException, IOException, CsvValidationException {
        long startNs = System.nanoTime();

        BlocksExtractor.extractBlocks(samplePath);        // Use instance variable
        JsonBuilder.convertCSVToJSON();                   // Modify this if it needs paths too

        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        System.out.println("Done, took " + durationMs + "ms");
    }
}

