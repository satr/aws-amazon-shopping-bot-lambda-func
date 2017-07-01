package io.github.satr.aws.lambda.shoppingbot.response;
// Copyright © 2017, github.com/satr, MIT License

public class Message {
    private String contentType;
    private String content;

    public class ContentType{
        public static final String PlainText = "PlainText";
    }

    public Message(String contentType, String content) {
        this.contentType = contentType;
        this.content = content;
    }

    public Message() {
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
