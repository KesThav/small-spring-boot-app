package com.spring.tag;

import com.spring.todo.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tag {
    private String name;

    private final List<Todo> todos;

    public Tag(String name) {
        this.name = name;
        this.todos = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Todo> getTodos() {
        return this.todos;
    }

    public void addTodo(Todo todo) {
        this.todos.add(todo);
    }

    public void removeTodo(Todo todo) {
        this.todos.removeIf(todo1 -> todo1.getId().equals(todo.getId()));
    }

}
