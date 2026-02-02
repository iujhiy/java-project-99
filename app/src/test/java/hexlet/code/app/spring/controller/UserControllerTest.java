package hexlet.code.app.spring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.spring.dto.UserDTO;
import hexlet.code.app.spring.mapper.UserMapper;
import hexlet.code.app.spring.repository.UserRepository;
import hexlet.code.app.spring.model.User;
import hexlet.code.app.utils.UserUtilsTest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper om;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private final UserUtilsTest userUtils = new UserUtilsTest();

    private User testUser;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        testUser = userUtils.createUser();
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/users").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<UserDTO> userDTOs = om.readValue(body, new TypeReference<>() { });
        var usersList = userDTOs.stream().map(userMapper::map).toList();
        var expected = userRepository.findAll();
        Assertions.assertThat(usersList).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testCreate() throws Exception {
        var user = userUtils.createUser();
        var userCreateDTO = userMapper.create(user);
        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(userCreateDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var result = userRepository.findByEmail(userCreateDTO.getEmail())
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("user", userCreateDTO.getEmail()));
        assertNotNull(result);
        assertThat(userCreateDTO.getFirstName()).isEqualTo(result.getFirstName());
        assertThat(userCreateDTO.getLastName()).isEqualTo(result.getLastName());
        assertThat(userCreateDTO.getEmail()).isEqualTo(result.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("firstName", JsonNullable.of("John"));
        var id = testUser.getId();
        var request = put("/api/users/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.throwResourceNotFoundException("user", id));
        assertThat(user.getFirstName()).isEqualTo("John");
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/users/" + testUser.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("email").isEqualTo(testUser.getEmail()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName())
        );
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/users/" + testUser.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isEqualTo(false);
    }

}
