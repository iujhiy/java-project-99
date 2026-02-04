package hexlet.code.app.spring.service;

import hexlet.code.app.spring.dto.TaskDTO;
import hexlet.code.app.spring.dto.create.TaskCreateDTO;
import hexlet.code.app.spring.dto.update.TaskUpdateDTO;
import hexlet.code.app.spring.mapper.TaskMapper;
import hexlet.code.app.spring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ExceptionUtils;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskDTO> getAll(Integer number, Integer size) {
//        var pageRequest = PageRequest.of(number - 1, size);
        return taskRepository.findAll().stream().map(taskMapper::map).toList();
    }

    public TaskDTO getTaskDTO(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task", id, "taskCreate"));
        return taskMapper.map(task);
    }

    public TaskDTO createTaskDTO(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(Long id, TaskUpdateDTO dto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task", id, "taskUpdate"));
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
