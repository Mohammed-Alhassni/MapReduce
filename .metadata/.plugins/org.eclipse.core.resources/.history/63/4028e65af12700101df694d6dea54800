import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BuildDataset {
	public static void main(String[] args) throws InterruptedException, IOException {
	    String samplePath = "/Users/mohmmedmsoud/Desktop/sem2 temp/pds project/SAMPLE.txt";

	    long startNs = System.nanoTime();

	    BlocksExtractor.extractBlocks(samplePath);

	    long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
	    System.out.println("Done, took " + durationMs + "ms");
	}

}
