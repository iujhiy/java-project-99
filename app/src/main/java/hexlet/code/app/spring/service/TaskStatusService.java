package hexlet.code.app.spring.service;

import hexlet.code.app.spring.dto.TaskStatusDTO;
import hexlet.code.app.spring.dto.create.TaskStatusCreateDTO;
import hexlet.code.app.spring.dto.update.TaskStatusUpdateDTO;
import hexlet.code.app.spring.mapper.TaskStatusMapper;
import hexlet.code.app.spring.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ExceptionUtils;

import java.util.List;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll(Integer number, Integer size) {
//        var pageRequest = PageRequest.of(number - 1, size);
        return taskStatusRepository.findAll().stream().map(taskStatusMapper::map).toList();
    }

    public TaskStatusDTO getTaskStatusDTO(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("taskStatus", id, "taskStatusCreate"));
        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO createTaskStatusDTO(TaskStatusCreateDTO dto) {
        var taskStatus = taskStatusMapper.map(dto);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO dto) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("taskStatus", id, "taskStatusUpdate"));
        taskStatusMapper.update(dto, taskStatus);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    public void deleteTaskStatus(Long id) {
        taskStatusRepository.deleteById(id);
    }

}
