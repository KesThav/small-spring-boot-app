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
import java.util.stream.Stream;

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
        Todo todo1;
        if(todo.getTags() == null){
            todo1 = new Todo(todo.getTitle(), todo.getDescription(), todo.getDueDate(), todo.getStatus());
        }else{
            for(String tag : todo.getTags()){
                if(this.tagService.getTag(tag) == null){
                    this.tagService.addNewTag(new Tag(tag));
                }
            }
            todo1 = new Todo(todo.getTitle(), todo.getDescription(), todo.getDueDate(), todo.getStatus(), todo.getTags());
        }

        todo.getTags().forEach(tag -> {
            this.tagService.getTag(tag).addTodo(todo1);
        });
        this.todos.add(todo1);
        return todo1;

    }

    public Todo updateTodo(String todoId, Todo todo) {

        Todo todo1 = this.getTodoById(todoId);
        todo1.setTitle(todo.getTitle());
        todo1.setDescription(todo.getDescription());
        todo1.setDueDate(todo.getDueDate());
        todo1.setStatus(todo.getStatus());
        Set<String> tags1 = new HashSet<>(todo1.getTags());
        Set<String> tags2 = new HashSet<>(todo.getTags());

        if (tags1.isEmpty() && tags2.isEmpty()) {
            return todo1;
        }

        tags1.forEach(tag1 -> {
            if (!tags2.contains(tag1)) {
                this.tagService.getTag(tag1).removeTodo(todo1);
                this.removeTagFromTodoById(todoId, tag1);
            }
        });


        tags2.forEach(tag2 -> {
            if (!tags1.contains(tag2)) {
                if (this.tagService.getTag(tag2) == null) {
                    this.tagService.addNewTag(new Tag(tag2));
                }
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
        Tag tagExist = tagService.getTag(tag);
        if (tagExist == null) {
            this.tagService.addNewTag(new Tag(tag));
        }

        this.todos = this.todos.stream().flatMap(todo1 -> {
            if (todo1.getId().equals(todoId)) {
                this.tagService.addTodoToTagById(tag, todo1);
                todo1.addTag(tag);
                return Stream.of(todo1);
            }else{
                return Stream.of(todo1);
            }
        }).collect(Collectors.toList());


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
