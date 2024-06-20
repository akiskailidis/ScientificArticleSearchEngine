package sci.Article_Search_Engine_Files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CsvFileIndexer {
	
	// Adjust path in /src/main/resources/application.properties
	
    @Value("${csv.file.path}")
    private String csvFilePath;

    @Value("${index.path}")
    private String indexPath;

    @PostConstruct
    public void indexCsv() throws IOException {
    	Path indexPathDir = Paths.get(indexPath);
    	
    	// Check if the index directory exists and if it's not empty
    	if(Files.exists(indexPathDir) && Files.list(indexPathDir).findAny().isPresent()) {
    		System.out.println("The indexFiles already exist. Skipping indexing.");
    		return;
    	}
    	
    	// Perform indexing
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
             LuceneIndexer indexer = new LuceneIndexer(indexPath)) { 
        	
            for (CSVRecord record : csvParser) {
                ArticleData article = new ArticleData(
                    Integer.parseInt(record.get("source_id")),
                    Integer.parseInt(record.get("year")),
                    record.get("title"),
                    record.get("abstract"),
                    record.get("full_text")
                );
                indexer.indexArticle(article);
            }
            
            System.out.println("Indexing completed successfully.");
        } catch (Exception e) {
            System.err.println("Failed to index CSV: " + e.getMessage());
        }
    }
}
