package org.burgas.backendserver.dto;

import java.util.List;

@SuppressWarnings("unused")
public final class StreamerRequest {

    private Long id;
    private Long identityId;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String about;
    private List<Long> categoryIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public String toString() {
        return "StreamerRequest{" +
               "id=" + id +
               ", identityId=" + identityId +
               ", firstname='" + firstname + '\'' +
               ", lastname='" + lastname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", about='" + about + '\'' +
               ", categoryIds=" + categoryIds +
               '}';
    }
}
