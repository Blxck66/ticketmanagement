package data.model;

import java.io.Serializable;

public class Keyword implements Serializable {
    private Long keywordId;
    private String keyword;

    public Long getKeywordId() {
        return keywordId;
    }

    public String getKeyword() {
        return keyword;
    }

    public Keyword(Long keywordId, String keyword) {
        this.keywordId = keywordId;
        this.keyword = keyword;
    }

    //This is important because keywords are displayed in the gui
    @Override
    public String toString() {
        return this.keyword;
    }
}
