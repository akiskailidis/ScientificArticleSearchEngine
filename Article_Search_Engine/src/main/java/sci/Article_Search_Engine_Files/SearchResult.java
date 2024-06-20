package sci.Article_Search_Engine_Files;

import java.util.List;

public class SearchResult {
    private List<ArticleData> articles;
    private int totalHits;

    public SearchResult(List<ArticleData> articles, int totalHits) {
        this.articles = articles;
        this.totalHits = totalHits;
    }

    public List<ArticleData> getArticles() {
        return articles;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public ArticleData getArticleById(int sourceId) {
        for (ArticleData article : articles) {
            if (article.getSourceId() == sourceId) {
            	System.out.println("Found it");
                return article;
            }
        }
        System.out.println("Nowhere to be found");
        return null;
    }
}
