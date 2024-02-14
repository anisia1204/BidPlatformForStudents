package com.licenta.domain;

import javax.persistence.*;

@Entity
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SERIAL")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teaching_material_id", foreignKey = @ForeignKey(name = "FK_ATTACHMENT__TEACHING_MATERIAL"))
    private TeachingMaterial teachingMaterial;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Long size;

    @Column(name = "object_url")
    private String objectUrl;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "file_content", columnDefinition = "BYTEA")
    private byte[] fileContent;


    public Attachment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TeachingMaterial getTeachingMaterial() {
        return teachingMaterial;
    }

    public void setTeachingMaterial(TeachingMaterial teachingMaterial) {
        this.teachingMaterial = teachingMaterial;
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

    public String getObjectUrl() {
        return objectUrl;
    }

    public void setObjectUrl(String objectUrl) {
        this.objectUrl = objectUrl;
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
}
