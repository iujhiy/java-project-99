package hexlet.code.app.spring.controller;

import hexlet.code.app.spring.dto.TaskDTO;
import hexlet.code.app.spring.dto.create.TaskCreateDTO;
import hexlet.code.app.spring.dto.update.TaskUpdateDTO;
import hexlet.code.app.spring.service.TaskService;
import jakarta.validation.Valid;
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
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index(@RequestParam(defaultValue = "5") Integer size,
                                                     @RequestParam(defaultValue = "1") Integer number) {
        var page = taskService.getAll(number, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.size()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");
        return ResponseEntity.ok().headers(headers).body(page);
//        return taskService.getAll(number, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable Long id) {
        return taskService.getTaskDTO(id);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@Valid @RequestBody TaskCreateDTO dto) {
        var taskDTO = taskService.createTaskDTO(dto);
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
        taskService.deleteTask(id);
    }
}
