package hexlet.code.spring.controller;


import hexlet.code.spring.dto.TaskStatusDTO;
import hexlet.code.spring.dto.create.TaskStatusCreateDTO;
import hexlet.code.spring.dto.update.TaskStatusUpdateDTO;
import hexlet.code.spring.service.BaseEntityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/task_statuses")
@AllArgsConstructor
public class TaskStatusController {
    private final BaseEntityService<TaskStatusDTO,
            TaskStatusCreateDTO,
            TaskStatusUpdateDTO> taskStatusService;

    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> index(@RequestParam(defaultValue = "5") Integer size,
                                               @RequestParam(defaultValue = "1") Integer number) {
        var page = taskStatusService.getAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.size()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO show(@PathVariable Long id) {
        return taskStatusService.show(id);
    }

    @PostMapping
    public ResponseEntity<TaskStatusDTO> create(@Valid @RequestBody TaskStatusCreateDTO dto) {
        var taskStatusDTO = taskStatusService.create(dto);
        return ResponseEntity
                .created(URI.create("api/taskStatuss/" + taskStatusDTO.getId()))
                .body(taskStatusDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody TaskStatusUpdateDTO dto) {
        var taskStatusDTO = taskStatusService.update(id, dto);
        return ResponseEntity.ok(taskStatusDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        taskStatusService.delete(id);
    }
}
