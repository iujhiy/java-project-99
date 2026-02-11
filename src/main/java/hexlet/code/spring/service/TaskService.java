package hexlet.code.spring.service;

import hexlet.code.spring.dto.TaskDTO;
import hexlet.code.spring.dto.create.TaskCreateDTO;
import hexlet.code.spring.dto.update.TaskUpdateDTO;
import hexlet.code.spring.mapper.TaskMapper;
import hexlet.code.spring.model.Task;
import hexlet.code.spring.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utils.ExceptionUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService implements BaseEntityService<TaskDTO, TaskCreateDTO, TaskUpdateDTO> {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private static final String TASK_NAME = "task";

    public List<TaskDTO> getAll(Specification<Task> spec, Integer number, Integer size) {
        var pageRequest = PageRequest.of(number - 1, size);
        return taskRepository.findAll(spec, pageRequest).stream().map(taskMapper::map).toList();
    }

    public List<TaskDTO> getAll() {
        return taskRepository.findAll().stream().map(taskMapper::map).toList();
    }

    public TaskDTO show(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException(TASK_NAME, id));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(Long id, TaskUpdateDTO dto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException(TASK_NAME, id));
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

}
