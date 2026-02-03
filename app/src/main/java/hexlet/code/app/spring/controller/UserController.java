package hexlet.code.app.spring.controller;

import hexlet.code.app.spring.dto.UserDTO;
import hexlet.code.app.spring.dto.create.UserCreateDTO;
import hexlet.code.app.spring.dto.update.UserUpdateDTO;
import hexlet.code.app.spring.service.UserService;
import hexlet.code.app.spring.util.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> index(@RequestParam(defaultValue = "5") Integer size,
                               @RequestParam(defaultValue = "1") Integer number) {
        var page = userService.getAll(number, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.size()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");
        return ResponseEntity.ok().headers(headers).body(page);
//        return userService.getAll(number, size);
    }

//    @GetMapping
//    public ResponseEntity<List<UserDTO>> index(
//            @RequestParam(defaultValue = "5") Integer size,
//            @RequestParam(defaultValue = "1") Integer number) {
//
//        try {
//            Page<UserDTO> page = userService.getAll(number, size);
//
//            System.out.println("=== DEBUG Page Info ===");
//            System.out.println("Total elements: " + page.getTotalElements());
//            System.out.println("Content size: " + page.getContent().size());
//            System.out.println("Page number: " + page.getNumber());
//            System.out.println("Page size: " + page.getSize());
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
//            headers.add("Access-Control-Expose-Headers", "X-Total-Count");
//
//            // Попробуйте сериализовать вручную для отладки
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.registerModule(new JavaTimeModule());
//            String json = mapper.writeValueAsString(page);
//            System.out.println("Serialized JSON: " + json.substring(0, Math.min(200, json.length())));
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(page.getContent());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error serializing page", e);
//        }
//    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        return userService.getUserDTO(id);
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO dto) {
        var userDTO = userService.createUserDTO(dto);
        return ResponseEntity
                .created(URI.create("api/users/" + userDTO.getId()))
                .body(userDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody UserUpdateDTO dto) {
        var userDTO = userService.update(id, dto);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userUtils.isCurrentUser(#id)") // возвращает 500 статус вместо 403
    public void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
