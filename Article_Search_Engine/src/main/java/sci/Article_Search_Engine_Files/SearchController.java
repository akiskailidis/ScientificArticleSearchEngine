package sci.Article_Search_Engine_Files;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;
import java.util.List;

@Controller
@SessionScope
public class SearchController {

    private final ArticleSearch articleSearch;
    private SearchResult searchResult;

    // Adjust path accordingly
    public SearchController() throws IOException {
        this.articleSearch = new ArticleSearch("C:\\Users\\Akis\\Downloads\\Article_Search_Engine\\Article_Search_Engine\\indexFiles");
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/search";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false, defaultValue = "") String query,
                         @RequestParam(name = "field", required = false, defaultValue = "all") String field,
                         @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(name = "sort", required = false, defaultValue = "false") boolean sort,
                         Model model) {
        if (query.isEmpty()) {
            model.addAttribute("error", "Please enter a search term.");
            return "search";
        }

        try {
            int pageSize = 10;
            searchResult = articleSearch.search(query, field, page, pageSize, sort);

            List<ArticleData> results = highlightSearchTerm(searchResult.getArticles(), query);
            int totalPages = (int) Math.ceil((double) searchResult.getTotalHits() / pageSize);

            model.addAttribute("articles", results);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("query", query);
            model.addAttribute("field", field);
            model.addAttribute("sort", sort);

            int startPage = Math.max(1, page - 2);
            int endPage = Math.min(totalPages, page + 2);
            if (endPage - startPage < 4) {
                if (startPage == 1) {
                    endPage = Math.min(startPage + 4, totalPages);
                } else if (endPage == totalPages) {
                    startPage = Math.max(endPage - 4, 1);
                }
            }
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        } catch (Exception e) {
            model.addAttribute("error", "Error performing search: " + e.getMessage());
        }
        return "search";
    }

    @GetMapping("/article")
    public String viewArticle(@RequestParam(name = "sourceId") int sourceId, Model model) throws IOException {
        System.out.println("Attempting to retrieve article with sourceId: " + sourceId);
		ArticleData article = searchResult.getArticleById(sourceId);
		if (article == null) {
		    System.out.println("Article not found.");
		    model.addAttribute("error", "Article not found.");
		    return "article";
		}
		System.out.println("Article found: " + article.getTitle());
		model.addAttribute("article", article);
        return "article";
    }


    private List<ArticleData> highlightSearchTerm(List<ArticleData> articles, String term) {
        String boldTerm = "<strong>" + term + "</strong>";
        for (ArticleData article : articles) {
            //article.setTitle(article.getTitle().replaceAll("(?i)" + term, boldTerm));
            article.setAbstractText(article.getAbstractText().replaceAll("(?i)" + term, boldTerm));
            article.setFullText(article.getFullText().replaceAll("(?i)" + term, boldTerm));
        }
        return articles;
    }

}
