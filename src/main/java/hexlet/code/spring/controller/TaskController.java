package hexlet.code.spring.controller;

import hexlet.code.spring.dto.TaskDTO;
import hexlet.code.spring.dto.create.TaskCreateDTO;
import hexlet.code.spring.dto.parametres.TaskParamsDTO;
import hexlet.code.spring.dto.update.TaskUpdateDTO;
import hexlet.code.spring.service.TaskService;
import hexlet.code.spring.specification.TaskSpecification;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Autowired
    private TaskSpecification taskSpecification;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index(@RequestParam(defaultValue = "5") Integer size,
                               @RequestParam(defaultValue = "1") Integer number,
                               TaskParamsDTO dto) {
        var spec = taskSpecification.build(dto);
        var page = taskService.getAll(spec, number, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.size()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable Long id) {
        return taskService.show(id);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@Valid @RequestBody TaskCreateDTO dto) {
        var taskDTO = taskService.create(dto);
        return ResponseEntity
                .created(URI.create("api/tasks/" + taskDTO.getId()))
                .body(taskDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id,
                                                @Valid @RequestBody TaskUpdateDTO dto) {
        var taskDTO = taskService.update(id, dto);
        return ResponseEntity.ok(taskDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        taskService.delete(id);
    }
}
