package com.timeAssignment.TimeAssignment;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class News {

    @Id
    private String title;

    private String link;

    public News()
    {

    }

    public News(String title, String link)
    {
        super();
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
