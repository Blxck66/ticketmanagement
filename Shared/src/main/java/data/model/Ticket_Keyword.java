package data.model;

import java.io.Serializable;

public class Ticket_Keyword implements Serializable {
    private Long TickedId;
    private Long KeywordId;

    public Ticket_Keyword(Long tickedId, Long keywordId) {
        TickedId = tickedId;
        KeywordId = keywordId;
    }

    public Long getTickedId() {
        return TickedId;
    }

    public void setTickedId(Long tickedId) {
        TickedId = tickedId;
    }

    public Long getKeywordId() {
        return KeywordId;
    }

    public void setKeywordId(Long keywordId) {
        KeywordId = keywordId;
    }
}
