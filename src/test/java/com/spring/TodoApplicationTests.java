package com.spring;

import com.spring.tag.Tag;
import com.spring.tag.TagService;
import com.spring.todo.Todo;
import com.spring.todo.TodoController;
import com.spring.todo.TodoService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@SpringJUnitConfig
@ContextConfiguration(classes=TodoApplication.class)
@WebMvcTest(TodoController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TodoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TodoService todoService;

	@MockBean
	private TagService tagService;

	@Autowired
	public TodoApplicationTests(TodoService todoService, TagService tagService) {
		this.todoService = todoService;
		this.tagService = tagService;
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@Order(1)
	public void testPostTodos() throws Exception {
		List<Todo> todos = Arrays.asList(
				new Todo("Todo1", "Description1", "2023-03-30", Todo.statusEnum.TODO,List.of("tag1", "tag2")),
				new Todo("Todo2", "Description2", "2023-04-04", Todo.statusEnum.IN_PROGRESS)
		);

		mockMvc.perform(MockMvcRequestBuilders.post("/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Todo1\",\"description\":\"Description1\",\"dueDate\":\"2023-03-30\",\"status\":\"TODO\",\"tags\":[\"tag1\",\"tag2\"]}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Todo1"));

		mockMvc.perform(MockMvcRequestBuilders.post("/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Todo2\",\"description\":\"Description2\",\"dueDate\":\"2023-04-04\",\"status\":\"IN_PROGRESS\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Todo2"));
	}

	@Test
	@Order(2)
	public void testGetTodos() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/todos"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Todo1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].tags").value(List.of("tag1", "tag2")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Todo2"));
	}
}







