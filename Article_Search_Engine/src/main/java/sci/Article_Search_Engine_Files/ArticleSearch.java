package sci.Article_Search_Engine_Files;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleSearch {
    private static final Logger logger = LoggerFactory.getLogger(ArticleSearch.class);
    private int maxResults = 50000;
    private final IndexSearcher searcher;
    private Directory indexDir;
    private IndexReader reader;
    private Analyzer analyzer;

    public ArticleSearch(String indexPath) throws IOException {
        this.indexDir = FSDirectory.open(Paths.get(indexPath));
        this.reader = DirectoryReader.open(indexDir);
        this.searcher = new IndexSearcher(reader);
        this.analyzer = new StandardAnalyzer();
    }

    public SearchResult search(String query, String field, int pageNum, int pageSize, boolean sortByYear) throws IOException {
        try {
            Query q;
            if ("all".equals(field)) {
                MultiFieldQueryParser parser = new MultiFieldQueryParser(
                    new String[] {"title", "fullText", "abstractText"}, analyzer
                );
                parser.setDefaultOperator(Operator.AND);  // Ensure all terms must match
                q = parser.parse(query);
            } else if ("fullText".equals(field)) {
                QueryParser parser = new QueryParser("fullText", analyzer);
                parser.setDefaultOperator(Operator.AND);  // Ensure all terms must match
                q = parser.parse(query);
            } else if ("title".equals(field)) {
                QueryParser parser = new QueryParser("title", analyzer);
                parser.setDefaultOperator(Operator.AND);  // Ensure all terms must match
                q = parser.parse(query);
            } else if ("abstractText".equals(field)) {
                QueryParser parser = new QueryParser("abstractText", analyzer);
                parser.setDefaultOperator(Operator.AND);  // Ensure all terms must match
                q = parser.parse(query);
            } else {
                throw new IllegalArgumentException("Unknown field: " + field);
            }

            // Print the parsed query for debugging
            System.out.println("Parsed Query: " + q.toString());

            Sort sort = null;
            if (sortByYear) {
                sort = new Sort(new SortField("year", SortField.Type.INT, true)); // true for descending order
            }

            TopDocs topDocs = sort != null ? searcher.search(q, maxResults, sort) : searcher.search(q, maxResults);
            int totalHits = (int) topDocs.totalHits.value;

            int start = (pageNum - 1) * pageSize;
            int end = Math.min(start + pageSize, totalHits);

            List<ArticleData> results = new ArrayList<>();
            for (int i = start; i < end; i++) {
                Document document = searcher.doc(topDocs.scoreDocs[i].doc);
                int sourceId = Integer.parseInt(document.get("sourceId"));
                int year = Integer.parseInt(document.get("year"));
                String title = document.get("title");
                String abstractText = document.get("abstractText");
                String fullText = document.get("fullText");

                // Add the document to the results list
                ArticleData article = new ArticleData(sourceId, year, title, abstractText, fullText);
                results.add(article);
            }

            return new SearchResult(results, totalHits);
        } catch (Exception e) {
            logger.error("Search error", e);
            throw new IOException("Failed to search due to internal error", e);
        }
    }
    
    public ArticleData getArticleById(int sourceId) throws IOException, ParseException {
        QueryParser parser = new QueryParser("sourceId", analyzer);
        Query query = parser.parse(String.valueOf(sourceId));
        TopDocs topDocs = searcher.search(query, 1);
        if (topDocs.totalHits.value > 0) {
            Document document = searcher.doc(topDocs.scoreDocs[0].doc);
            int year = Integer.parseInt(document.get("year"));
            String title = document.get("title");
            String abstractText = document.get("abstractText");
            String fullText = document.get("fullText");

            return new ArticleData(sourceId, year, title, abstractText, fullText);
        } else {
            return null;
        }
    }


    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (indexDir != null) {
            indexDir.close();
        }
    }
}
