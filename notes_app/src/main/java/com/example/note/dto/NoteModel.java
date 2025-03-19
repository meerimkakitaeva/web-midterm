package com.example.note.dto;

import com.example.note.enums.NoteStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NoteModel {

    private Long id;

    @NotBlank(message = "title cannot be blank")
    private String title;

    private NoteStatus status;

    @NotNull(message = "userId cannot be null")
    private Long userId;

    public NoteModel() {}

    public NoteModel(Long id, String title, NoteStatus status, Long userId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NoteStatus getStatus() {
        return status;
    }

    public void setStatus(NoteStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "NoteModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                '}';
    }
}
