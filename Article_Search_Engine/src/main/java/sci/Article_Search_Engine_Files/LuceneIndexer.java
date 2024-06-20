package sci.Article_Search_Engine_Files;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Paths;

public class LuceneIndexer implements Closeable {
    private Directory indexDir;
    private StandardAnalyzer analyzer;
    private IndexWriterConfig config;
    private IndexWriter writer;

    public LuceneIndexer(String indexPath) throws IOException {
        this.indexDir = FSDirectory.open(Paths.get(indexPath));
        this.analyzer = new StandardAnalyzer();
        this.config = new IndexWriterConfig(analyzer);
        this.writer = new IndexWriter(indexDir, config);
    }

    public void indexArticle(ArticleData article) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("sourceId", String.valueOf(article.getSourceId()), Field.Store.YES));
        doc.add(new IntPoint("year", article.getYear())); // Indexed for range queries
        doc.add(new StoredField("year", article.getYear())); // Stored for retrieval
        doc.add(new NumericDocValuesField("year", article.getYear())); // Indexed for sorting
        doc.add(new TextField("title", article.getTitle(), Field.Store.YES)); // Indexed and stored
        doc.add(new TextField("abstractText", article.getAbstractText(), Field.Store.YES)); // Indexed and stored
        doc.add(new TextField("fullText", article.getFullText(), Field.Store.YES)); // Indexed and stored
        writer.addDocument(doc);
    }

    @Override
    public void close() throws IOException {
        writer.close();
        indexDir.close();
    }
}
