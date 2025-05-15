import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.opencsv.exceptions.CsvValidationException;

public class BuildDataset {
    private String samplePath;
    private String csvPath;
    private String jsonPath;

    public BuildDataset(String samplePath, String csvPath, String jsonPath) {
        this.samplePath = samplePath;
        this.csvPath = csvPath;
        this.jsonPath = jsonPath;
    }

    public void process() throws InterruptedException, IOException, CsvValidationException {
        long startNs = System.nanoTime();

        BlocksExtractor.extractBlocks(samplePath);        // Use instance variable
        JsonBuilder.convertCSVToJSON();                   // Modify this if it needs paths too

        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        System.out.println("Done, took " + durationMs + "ms");
    }
}

