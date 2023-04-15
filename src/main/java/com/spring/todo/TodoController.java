package com.spring.todo;

import com.spring.tag.Tag;
import com.spring.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "todos")
public class TodoController {

    private final TodoService todoService;
    private final TagService tagService;

    @Autowired
    public TodoController(TodoService todoService, TagService tagService) {
        this.todoService = todoService;
        this.tagService = tagService;
    }

    @GetMapping
    public List<Todo> getTodos() {
        return this.todoService.getTodos();
    }

    @GetMapping("/{todoId}")
    public Todo getTodoById(@PathVariable("todoId") String todoId) {
        return this.todoService.getTodoById(todoId);
    }

    @PostMapping
    public Todo addNewTodo(@RequestBody Todo todo) {
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
        return this.todoService.addNewTodo(todo1);
    }

    @DeleteMapping("/{todoId}")
    public void deleteTodo(@PathVariable("todoId") String todoId) {
        this.todoService.deleteTodo(todoId);
    }

    @PutMapping("/{todoId}")
    public Todo updateTodo(@PathVariable("todoId") String todoId, @RequestBody Todo todo) {
        return this.todoService.updateTodo(todoId, todo);
    }

    @GetMapping("/{todoId}/tag")
    public List<String> getTagsOfTodoById(@PathVariable("todoId") String todoId) {
        return this.todoService.getTagOfTodoById(todoId);
    }
    @PostMapping("/{todoId}/tag")
    public Todo addTagToTodoById(@PathVariable("todoId") String todoId, @RequestBody String tag) {
        Tag tagExist = tagService.getTag(tag);
        Todo todoToUpdate = this.todoService.getTodoById(todoId);
        if (tagExist == null) {
            Tag newTag = this.tagService.addNewTag(new Tag(tag));
            this.todoService.addTagToTodoById(todoId, newTag.getName());
            this.tagService.addTodoToTagById(newTag.getName(), todoToUpdate);
        }else{
            this.todoService.addTagToTodoById(todoId, tagExist.getName());
            this.tagService.addTodoToTagById(tag, todoToUpdate);
        }

        return this.todoService.getTodoById(todoId);
    }

}
