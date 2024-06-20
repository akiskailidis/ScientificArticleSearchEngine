package sci.Article_Search_Engine_Files;

public class ArticleData {
    private int sourceId;
    private int year;
    private String title;
    private String abstractText; 
    private String fullText;

    // Constructor, getters, and setters
    public ArticleData(int sourceId, int year, String title, String abstractText, String fullText) {
        this.sourceId = sourceId;
        this.year = year;
        this.title = title;
        this.abstractText = abstractText;
        this.fullText = fullText;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }
    
}
