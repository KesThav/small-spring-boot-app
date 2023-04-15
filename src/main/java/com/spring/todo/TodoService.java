package com.spring.todo;

import com.spring.tag.Tag;
import com.spring.tag.TagService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class TodoService {


    private List<Todo> todos = new ArrayList<>();

    private List<Tag> tags;

    private final TagService tagService;

    public TodoService(@Lazy TagService tagService) {
        this.tagService = tagService;
    }

    public List<Todo> getTodos() {
        return this.todos;
    }

    public Todo getTodoById(String todoId) {
        return this.todos.stream()
                .filter(todo1 -> todo1.getId().equals(todoId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Todo with id " + todoId + " does not exist"));

    }

    public Todo addNewTodo(Todo todo) {
        //check if tags exist
        todo.getTags().forEach(tag -> {
            if (this.tagService.getTag(tag) == null) {
                this.tagService.addNewTag(new Tag(tag));
            }
        });
        this.todos.add(todo);
        return todo;

    }

    public Todo updateTodo(String todoId, Todo todo) {
        Todo todo1 = this.getTodoById(todoId);
        Set<String> tags1 = new HashSet<>(todo1.getTags());
        Set<String> tags2 = new HashSet<>(todo.getTags());

        if (tags1.isEmpty() && tags2.isEmpty()) {
            return todo1;
        }

        tags1.forEach(tag1 -> {
            if (tags2.isEmpty() || !tags2.contains(tag1)) {
                this.tagService.getTag(tag1).removeTodo(todo1);
                this.removeTagFromTodoById(todoId, tag1);
            }
        });

        tags2.forEach(tag2 -> {
            if (tags1.isEmpty() || !tags1.contains(tag2)) {
                if (this.tagService.getTag(tag2) == null) {
                    this.tagService.addNewTag(new Tag(tag2));
                }
                this.tagService.getTag(tag2).addTodo(todo1);
                this.addTagToTodoById(todoId, tag2);
            }
        });

        return this.getTodoById(todoId);
    }


    public void deleteTodo(String todoId) {
        Todo todo0 = this.getTodoById(todoId);
        this.todos = this.todos.stream()
                .filter(todo1 -> !todo1.getId().equals(todoId))
                .collect(Collectors.toList());
        todo0.getTags().forEach(tag1 -> {
            this.tagService.getTag(tag1).removeTodo(todo0);
        });
    }

    public List<String> getTagOfTodoById(String todoId) {
       return this.todos.stream().filter(todo1 -> todo1.getId().equals(todoId))
               .findFirst()
               .orElseThrow(() -> new IllegalStateException("Todo with id " + todoId + " does not exist"))
               .getTags().stream().map(tag1 -> this.tagService.getTag(tag1).getName()).collect(Collectors.toList());
    }

    public Todo addTagToTodoById(String todoId, String tag) {
        this.todos.stream().filter(todo1 -> todo1.getId().equals(todoId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Todo with id " + todoId + " does not exist"))
                .addTag(tag);

        return this.getTodoById(todoId);
    }

    public Todo removeTagFromTodoById(String todoId, String tag) {
        this.todos.stream().filter(todo1 -> todo1.getId().equals(todoId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Todo with id " + todoId + " does not exist"))
                .removeTag(tag);

        return this.getTodoById(todoId);
    }
}
