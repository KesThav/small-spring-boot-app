package com.spring;

import com.spring.tag.TagController;
import com.spring.todo.Todo;
import com.spring.todo.TodoController;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@SpringBootTest(classes = TodoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TodoApplicationTests{

	private final TodoController todoController;
	private final TagController tagController;

	@Autowired
	public TodoApplicationTests(TodoController todoController, TagController tagController) {
		this.todoController = todoController;
		this.tagController = tagController;
	}

	@Test
	@Order(1)
	void getTodos() {
		todoController.getTodos();
		assertEquals(0,todoController.getTodos().size() );
	}

	@Test
	@Order(2)
	void getTags() {
		tagController.getTags();
		assertEquals(0,tagController.getTags().size() );
	}

	@Test
	@Order(3)
	void addNewTodo() {
		todoController.addNewTodo(new Todo("todo1", "description of todo 1", "2020-12-12", Todo.statusEnum.TODO, List.of("tag1", "tag2")));
		todoController.addNewTodo(new Todo("todo2", "description of todo 2", "2020-12-12", Todo.statusEnum.IN_PROGRESS));
		assertEquals(2,todoController.getTodos().size());
		assertEquals(2, todoController.getTodos().stream().mapToLong(todo1 -> todo1.getTags().size()).sum());
		assertEquals(2,tagController.getTags().size());
		assertEquals(1,tagController.getTodosByTag("tag1").size());
		assertEquals(1,tagController.getTodosByTag("tag2").size());
	}

	@Test
	@Order(4)
	void getTodoById(){
		Todo todo = todoController.getTodos().stream().filter(t -> t.getTitle().equals("todo1")).findFirst().orElse(null);

		assertEquals("todo1", todoController.getTodoById(todo.getId()).getTitle());
	}

	@Test
	@Order(5)
	void updateTodo(){
		List<Todo> todos = new ArrayList<>(todoController.getTodos()); // create a new list object
		Optional<Todo> optionalTodo1 = todos.stream()
				.filter(t -> t.getTitle().equals("todo1"))
				.findFirst();
		String todo1Id = optionalTodo1.map(Todo::getId).orElse(null);
		assertNotNull(todo1Id);

		Todo newTodo = new Todo("todo1 updated", "description of todo 1 updated", "2022-12-12", Todo.statusEnum.DONE, List.of("tag3"));

		todoController.updateTodo(todo1Id, newTodo);

		Todo updatedTodo = todoController.getTodoById(todo1Id);

		assertEquals("todo1 updated", updatedTodo.getTitle());
		assertEquals("description of todo 1 updated", updatedTodo.getDescription());
		assertEquals("2022-12-12", updatedTodo.getDueDate());
		assertEquals(Todo.statusEnum.DONE, updatedTodo.getStatus());
		assertEquals(1, updatedTodo.getTags().size());
		assertEquals(1,tagController.getTodosByTag("tag3").size() );
		assertEquals(0,tagController.getTodosByTag("tag1").size() );
		assertEquals(0,tagController.getTodosByTag("tag2").size() );
	}

	@Test
	@Order(6)
	void deleteTodo(){
		String todo1Id = todoController.getTodos().stream()
				.filter(t -> t.getTitle().equals("todo1 updated"))
				.findFirst()
				.orElse(null).getId();

		todoController.deleteTodo(todo1Id);

		assertEquals(1,todoController.getTodos().size());
		assertEquals(3,tagController.getTags().size());
		assertEquals(0,tagController.getTodosByTag("tag3").size() );
	}

	@Test
	@Order(7)
	void addTag() {
		tagController.addNewTag("tag4");
		assertEquals(4,tagController.getTags().size());
	}


	@Test
	@Order(8)
	void addTagOfTodobyId(){
		Todo todo = todoController.getTodos().stream().filter(todo1 -> todo1.getTitle().equals("todo2")).findFirst().orElse(null);

		todoController.addTagToTodoById(todo.getId(), "tag4");
		todoController.addTagToTodoById(todo.getId(), "tag5");

		assertEquals(2, todoController.getTagsOfTodoById(todo.getId()).size());
	}

	@Test
	@Order(9)
	void getTagOfTodobyId(){
		Todo todo = todoController.getTodos().stream().filter(todo1 -> todo1.getTitle().equals("todo2")).findFirst().orElse(null);

		assertEquals(2, todoController.getTagsOfTodoById(todo.getId()).size());
	}

	@Test
	@Order(10)
	void deleteTag(){
		tagController.deleteTag("tag4");
		assertEquals(4,tagController.getTags().size());
		assertFalse(todoController.getTodos().stream()
				.flatMap(todo -> todo.getTags().stream())
				.anyMatch(tag -> tag.equals("tag4")));
	}

	@Test
	@Order(11)
	void TodoByTag(){
		assertEquals(1,tagController.getTodosByTag("tag5").size());
	}



}
