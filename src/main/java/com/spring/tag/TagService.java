package com.spring.tag;

import com.spring.todo.Todo;
import com.spring.todo.TodoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TodoService todoService;

    private List<Tag> tags = new ArrayList<>();;



    public TagService(TodoService todoService) {
        this.todoService = todoService;
    }


    public List<Tag> getTags() {
        return this.tags;
    }

    public Tag getTag(String tag){
        return this.tags.stream()
                .filter(tag1 -> tag1.getName().equals(tag))
                .findFirst()
                .orElse(null);
    }

    public Tag addNewTag(Tag tag) {
        this.tags.add(tag);
        return tag;
    }

    public void deleteTag(String tag) {
        this.getTodosOfTag(tag).forEach(todo -> this.todoService.removeTagFromTodoById(todo.getId(), tag));
        this.tags = this.tags.stream()
                .filter(tag1 -> !tag1.getName().equals(tag)).collect(Collectors.toList());
    }

    public List<Todo> getTodosOfTag(String tag) {
        return this.tags.stream().filter(tag1 -> tag1.getName().equals(tag) || tag1.getName().equals(tag))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tag" + tag + " does not exist"))
                .getTodos();
    }

    public Tag addTodoToTagById(String tag, Todo todo) {
        this.tags.stream().filter(tag1 -> tag1.getName().equals(tag))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tag with id " + tag + " does not exist"))
                .addTodo(todo);

        return this.getTag(tag);
    }

    public Tag removeTodoFromTagById(String tagId, Todo todo) {
        this.tags.stream().filter(tag1 -> tag1.getName().equals(tagId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tag with id " + tagId + " does not exist"))
                .removeTodo(todo);

        return this.getTag(tagId);
    }

    public void removeTodoFromTag(String tag, Todo todo) {
        this.getTag(tag).removeTodo(todo);
    }


}
