package com.spring.todo;

import com.spring.tag.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Todo {

    private String id;

    private String title;
    private String description;
    private String dueDate;
    private statusEnum status;

    private List<String> tags;


    public enum statusEnum {
        TODO, IN_PROGRESS, DONE
    }
    public Todo() {}


    public Todo(String title, String description, String dueDate, statusEnum status, List<String> tags) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.tags = new ArrayList<>(tags);
        this.tags = new ArrayList<>(tags);
    }

    public Todo(String title, String description, String dueDate, statusEnum status) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.tags = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public statusEnum getStatus() {
        return status;
    }

    public void setStatus(statusEnum status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

}
