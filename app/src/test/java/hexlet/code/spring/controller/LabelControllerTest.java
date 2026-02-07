package hexlet.code.spring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.spring.dto.LabelDTO;
import hexlet.code.spring.dto.create.LabelCreateDTO;
import hexlet.code.spring.model.Label;
import hexlet.code.spring.repository.LabelRepository;
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
public class LabelControllerTest {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        labelRepository.deleteAll();
        testLabel = TestUtils.createTestLabel();
        labelRepository.save(testLabel);
        token = jwt().jwt(builder -> builder.subject("test@example.com"));
    }

    @Test
    public void testIndex() throws Exception {
        // Создаем еще один тэг для проверки списка
        Label anotherLabel = TestUtils.createTestLabel();
        labelRepository.save(anotherLabel);

        var result = mockMvc.perform(get("/api/labels").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<LabelDTO> statusDTOs = om.readValue(body, new TypeReference<>() { });

        // Проверяем заголовок X-Total-Count
        String totalCountHeader = result.getResponse().getHeader("X-Total-Count");
        assertThat(totalCountHeader).isEqualTo("2");

        // Проверяем, что все статусы присутствуют в ответе
        var expectedStatuses = labelRepository.findAll();
        Assertions.assertThat(statusDTOs)
                .extracting(LabelDTO::getId)
                .containsExactlyInAnyOrderElementsOf(
                        expectedStatuses.stream()
                                .map(Label::getId)
                                .toList()
                );
    }

    @Test
    public void testCreate() throws Exception {
        LabelCreateDTO createDTO = new LabelCreateDTO();
        createDTO.setName("Тест");

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var result = labelRepository.findByName(createDTO.getName())
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("label", createDTO.getName()));

        assertNotNull(result);
        assertThat(result.getName()).isEqualTo("Тест");
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        LabelCreateDTO invalidDTO = new LabelCreateDTO();
        invalidDTO.setName("");

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(invalidDTO));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("name", JsonNullable.of("Тест"));

        var id = testLabel.getId();
        var request = put("/api/labels/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var label = labelRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("label", id));

        assertThat(label.getName()).isEqualTo("Тест");
        assertThat(label.getId()).isEqualTo(testLabel.getId());
    }


    @Test
    public void testUpdateWithInvalidData() throws Exception {
        var data = new HashMap<>();
        data.put("name", JsonNullable.of("")); // Пустое название

        var id = testLabel.getId();
        var request = put("/api/labels/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/labels/" + testLabel.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testLabel.getId()),
                v -> v.node("name").isEqualTo(testLabel.getName()),
                v -> v.node("createdAt").isPresent()
        );
    }

    @Test
    public void testShowNotFound() throws Exception {
        var request = get("/api/labels/9999").with(token);
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/labels/" + testLabel.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(labelRepository.existsById(testLabel.getId())).isFalse();
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        // Тест без токена
        var request = get("/api/labels");
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }
}
