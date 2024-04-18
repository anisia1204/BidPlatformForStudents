package com.licenta.domain;

import javax.persistence.*;

@Table(name = "profile_picture")
@Entity
public class ProfilePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SERIAL")
    private Long id;
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_PROFILE_PICTURE__USER"))
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Long size;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "file_content", columnDefinition = "BYTEA")
    private byte[] fileContent;

    public ProfilePicture() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
