package com.spring.tag;

import com.spring.todo.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getTags() {
        return this.tagService.getTags();
    }

    @PostMapping
    public Tag addNewTag(@RequestBody String tag) {
        return this.tagService.addNewTag(new Tag(tag));
    }

    @DeleteMapping("/{tag}")
    public void deleteTag(@PathVariable("tag") String tag) {
        this.tagService.deleteTag(tag);
    }

    @GetMapping("/{tag}/todos")
    public List<Todo> getTodosByTag(@PathVariable("tag") String tag) {
        return this.tagService.getTodosOfTag(tag);
    }

}
