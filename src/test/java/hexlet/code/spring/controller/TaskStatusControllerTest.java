package hexlet.code.spring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.spring.dto.TaskStatusDTO;
import hexlet.code.spring.dto.create.TaskStatusCreateDTO;
import hexlet.code.spring.model.TaskStatus;
import hexlet.code.spring.repository.TaskRepository;
import hexlet.code.spring.repository.TaskStatusRepository;
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
public class TaskStatusControllerTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private TaskStatus testTaskStatus;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        testTaskStatus = TestUtils.createTestTaskStatus();
        taskStatusRepository.save(testTaskStatus);
        token = jwt().jwt(builder -> builder.subject("test@example.com"));
    }

    @Test
    public void testIndex() throws Exception {
        // Создаем еще один статус для проверки списка
        TaskStatus anotherStatus = TestUtils.createAnotherTaskStatus();
        taskStatusRepository.save(anotherStatus);

        var result = mockMvc.perform(get("/api/task_statuses").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<TaskStatusDTO> statusDTOs = om.readValue(body, new TypeReference<>() { });

        // Проверяем заголовок X-Total-Count
        String totalCountHeader = result.getResponse().getHeader("X-Total-Count");
        assertThat(totalCountHeader).isEqualTo("2");

        // Проверяем, что все статусы присутствуют в ответе
        var expectedStatuses = taskStatusRepository.findAll();
        Assertions.assertThat(statusDTOs)
                .extracting(TaskStatusDTO::getId)
                .containsExactlyInAnyOrderElementsOf(
                        expectedStatuses.stream()
                                .map(TaskStatus::getId)
                                .toList()
                );
    }

    @Test
    public void testCreate() throws Exception {
        TaskStatusCreateDTO createDTO = new TaskStatusCreateDTO();
        createDTO.setName("Новый");
        createDTO.setSlug("new");

        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var result = taskStatusRepository.findBySlug(createDTO.getSlug())
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task status",
                                createDTO.getSlug()));

        assertNotNull(result);
        assertThat(result.getName()).isEqualTo(createDTO.getName());
        assertThat(result.getSlug()).isEqualTo(createDTO.getSlug());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        // Тест с пустым именем
        TaskStatusCreateDTO invalidDTO = new TaskStatusCreateDTO();
        invalidDTO.setName("");
        invalidDTO.setSlug("test");

        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(invalidDTO));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());

        // Тест с пустым slug
        invalidDTO.setName("Test");
        invalidDTO.setSlug("");

        request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(invalidDTO));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testFullUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("name", JsonNullable.of("Обновленный статус"));
        data.put("slug", JsonNullable.of("updated_slug"));

        var id = testTaskStatus.getId();
        var request = put("/api/task_statuses/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task status", id));

        assertThat(taskStatus.getName()).isEqualTo("Обновленный статус");
        assertThat(taskStatus.getSlug()).isEqualTo("updated_slug");
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("name", JsonNullable.of("Новое имя"));

        var id = testTaskStatus.getId();
        var request = put("/api/task_statuses/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task status", id));

        assertThat(taskStatus.getName()).isEqualTo("Новое имя");
        assertThat(taskStatus.getSlug()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testUpdateWithInvalidData() throws Exception {
        var data = new HashMap<>();
        data.put("name", JsonNullable.of("")); // Пустое имя

        var id = testTaskStatus.getId();
        var request = put("/api/task_statuses/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/task_statuses/" + testTaskStatus.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testTaskStatus.getId()),
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug()),
                v -> v.node("createdAt").isPresent()
        );
    }

    @Test
    public void testShowNotFound() throws Exception {
        var request = get("/api/task_statuses/9999").with(token);
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/task_statuses/" + testTaskStatus.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.existsById(testTaskStatus.getId())).isFalse();
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        // Тест без токена
        var request = get("/api/task_statuses/1");
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }
}
