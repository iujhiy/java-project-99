package hexlet.code.app.spring.controller;


import hexlet.code.app.spring.dto.TaskStatusDTO;
import hexlet.code.app.spring.dto.create.TaskStatusCreateDTO;
import hexlet.code.app.spring.dto.update.TaskStatusUpdateDTO;
import hexlet.code.app.spring.service.TaskStatusService;
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
@RequestMapping("/api/task_statuses")
public class TaskStatusController {
    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskStatusDTO>> index(@RequestParam(defaultValue = "5") Integer size,
                                               @RequestParam(defaultValue = "1") Integer number) {
        var page = taskStatusService.getAll(number, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.size()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");
        return ResponseEntity.ok().headers(headers).body(page);
//        return taskStatusService.getAll(number, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO show(@PathVariable Long id) {
        return taskStatusService.getTaskStatusDTO(id);
    }

    @PostMapping
    public ResponseEntity<TaskStatusDTO> create(@Valid @RequestBody TaskStatusCreateDTO dto) {
        var taskStatusDTO = taskStatusService.createTaskStatusDTO(dto);
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
        taskStatusService.deleteTaskStatus(id);
    }
}
