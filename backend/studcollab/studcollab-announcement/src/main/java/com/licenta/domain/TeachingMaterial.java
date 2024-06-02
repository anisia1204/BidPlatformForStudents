package com.licenta.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "teaching_material")
public class TeachingMaterial extends Announcement{
    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "edition")
    private Integer edition;

    public TeachingMaterial() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }
}
