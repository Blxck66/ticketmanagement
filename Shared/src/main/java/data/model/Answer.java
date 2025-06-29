package data.model;

import java.io.Serializable;

public class Answer implements Serializable {
    private Long answerId;
    private String answerString;
    private Boolean isFinal;
    private Long ticketId;

    public Answer(String answerString, Boolean isFinal, Long ticketId) {
        this.answerString = answerString;
        this.isFinal = isFinal;
        this.ticketId = ticketId;
    }

    public Answer(Long answerId, String answerString, Boolean isFinal, Long ticketId) {
        this.answerId = answerId;
        this.answerString = answerString;
        this.isFinal = isFinal;
        this.ticketId = ticketId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    public Boolean getFinal() {
        return isFinal;
    }

    public void setFinal(Boolean aFinal) {
        isFinal = aFinal;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
}
