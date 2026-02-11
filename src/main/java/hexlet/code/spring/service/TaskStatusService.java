package hexlet.code.spring.service;

import hexlet.code.spring.dto.TaskStatusDTO;
import hexlet.code.spring.dto.create.TaskStatusCreateDTO;
import hexlet.code.spring.dto.update.TaskStatusUpdateDTO;
import hexlet.code.spring.mapper.TaskStatusMapper;
import hexlet.code.spring.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import utils.ExceptionUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService implements BaseEntityService<TaskStatusDTO, TaskStatusCreateDTO, TaskStatusUpdateDTO> {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        return taskStatusRepository.findAll().stream().map(taskStatusMapper::map).toList();
    }

    public TaskStatusDTO show(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("taskStatus", id));
        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        var taskStatus = taskStatusMapper.map(dto);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO dto) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("taskStatus", id));
        taskStatusMapper.update(dto, taskStatus);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    public void delete(Long id) {
        taskStatusRepository.deleteById(id);
    }

}
