package cn.wizzer.bugwk.commons.service;

/**
 * Created by wizzer on 2018.08
 */
public class LuceneSearchResult {
    private String id;
    private String title;

    public LuceneSearchResult(String id, String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
