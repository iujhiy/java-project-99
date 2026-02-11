package hexlet.code.spring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.spring.dto.TaskDTO;
import hexlet.code.spring.dto.create.TaskCreateDTO;
import hexlet.code.spring.mapper.TaskMapper;
import hexlet.code.spring.model.Label;
import hexlet.code.spring.model.Task;
import hexlet.code.spring.model.TaskStatus;
import hexlet.code.spring.model.User;
import hexlet.code.spring.repository.LabelRepository;
import hexlet.code.spring.repository.TaskRepository;
import hexlet.code.spring.repository.TaskStatusRepository;
import hexlet.code.spring.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import utils.ExceptionUtils;

import java.util.HashMap;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskMapper taskMapper;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private TaskStatus testTaskStatus;

    private Task testTask;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
        labelRepository.deleteAll();
        testTaskStatus = TestUtils.createTestTaskStatus();
        User testUser = TestUtils.createUser();
        userRepository.save(testUser);
        testLabel = TestUtils.createTestLabel();
        labelRepository.save(testLabel);
        taskStatusRepository.save(testTaskStatus);
        testTask = TestUtils.createTestTask(testTaskStatus);
        testUser.addTask(testTask);
        taskRepository.save(testTask);
        testTask.addLabel(testLabel);
        taskRepository.save(testTask);
        token = jwt().jwt(builder -> builder.subject("test@example.com"));
    }

    @Test
    public void testIndex() throws Exception {
        // Создаем еще один статус для проверки списка
        Task anotherTask = TestUtils.createTestTask(testTaskStatus);
        taskRepository.save(anotherTask);

        var result = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<TaskDTO> taskDTOs = om.readValue(body, new TypeReference<>() { });
        var taskList = taskDTOs.stream().map(taskMapper::map).toList();
        var expected = taskRepository.findAll();
        Assertions.assertThat(taskList).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testCreate() throws Exception {
        TaskCreateDTO createDTO = new TaskCreateDTO();
        createDTO.setName("Уборка");
        createDTO.setTaskStatus(testTaskStatus.getSlug());

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var result = taskRepository.findByName(createDTO.getName())
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task status",
                                createDTO.getName()));

        assertNotNull(result);
        assertThat(result.getName()).isEqualTo("Уборка");
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        // Тест с пустым названием
        TaskCreateDTO invalidDTO = new TaskCreateDTO();
        invalidDTO.setName("");
        invalidDTO.setTaskStatus("test");

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(invalidDTO));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());

        // Тест с пустым status
        invalidDTO.setName("Test");
        invalidDTO.setTaskStatus("");

        request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(invalidDTO));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("title", JsonNullable.of("Новая задача"));

        var id = testTask.getId();
        var request = put("/api/tasks/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task status",
                                id));

        assertThat(task.getName()).isEqualTo("Новая задача");
        assertThat(task.getTaskStatus().getId()).isEqualTo(testTask.getTaskStatus().getId());
    }

    @Test
    public void testFullUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("title", JsonNullable.of("Новая задача"));
        data.put("status", JsonNullable.of(testTaskStatus.getSlug()));

        var id = testTask.getId();
        var request = put("/api/tasks/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task status",
                                id));

        assertThat(task.getName()).isEqualTo("Новая задача");
        assertThat(task.getTaskStatus().getId()).isEqualTo(testTaskStatus.getId());
    }

    @Test
    public void testUpdateWithInvalidData() throws Exception {
        var data = new HashMap<>();
        data.put("title", JsonNullable.of("")); // Пустое название

        var id = testTask.getId();
        var request = put("/api/tasks/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/tasks/" + testTask.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testTask.getId()),
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                v -> v.node("createdAt").isPresent()
        );
    }

    @Test
    public void testShowNotFound() throws Exception {
        var request = get("/api/tasks/9999").with(token);
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/tasks/" + testTask.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(testTask.getId())).isFalse();
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        // Тест без токена
        var request = get("/api/tasks");
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFiltersTitleCont() throws Exception {
        var title = testTask.getName();
        var request = get("/api/tasks?titleCont=" + title).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isArray()
                .hasSize(1)
                .first()
                .and(v -> v.node("id").isEqualTo(testTask.getId()));

        var shortTitle = title.substring(2); // проверка поиска по неполному title
        var request2 = get("/api/tasks?titleCont=" + shortTitle).with(token);
        var result2 = mockMvc.perform(request2)
                .andExpect(status().isOk())
                .andReturn();

        var body2 = result2.getResponse().getContentAsString();
        assertThatJson(body2)
                .isArray()
                .hasSize(1)
                .first()
                .and(v -> v.node("id").isEqualTo(testTask.getId()));    }

    @Test
    public void testFiltersNotEqualTitleCont() throws Exception {
        // faker возвращает английский текст, используем русский для гарантированного несоответствия
        var request = get("/api/tasks?titleCont=Тест").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isArray()
                .hasSize(0);
    }

    @Test
    public void testFiltersAssigneeId() throws Exception {
        var assigneeId = testTask.getAssignee().getId();
        var request = get("/api/tasks?assigneeId=" + assigneeId).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isArray()
                .hasSize(1)
                .first()
                .and(v -> v.node("id").isEqualTo(testTask.getId()));


    }

    @Test
    public void testFiltersIncorrectAssigneeId() throws Exception {
        var request = get("/api/tasks?assigneeId=9999").with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isArray()
                .hasSize(0);
    }

        @Test
    public void testFiltersStatus() throws Exception {
        var request = get("/api/tasks?status=" + testTaskStatus.getSlug()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isArray()
                .hasSize(1)
                .first()
                .and(v -> v.node("id").isEqualTo(testTask.getId()));
    }

    @Test
    public void testFiltersLabelId() throws Exception {
        var request = get("/api/tasks?labelId=" + testLabel.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isArray()
                .hasSize(1)
                .first()
                .and(v -> v.node("id").isEqualTo(testTask.getId()));
    }

}
