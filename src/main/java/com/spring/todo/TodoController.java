package com.spring.todo;

import com.spring.tag.TagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Todo")
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
        return this.todoService.addNewTodo(todo);
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
        return this.todoService.addTagToTodoById(todoId,tag);
    }

}
