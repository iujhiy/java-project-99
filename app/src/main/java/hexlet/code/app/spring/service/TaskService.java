package hexlet.code.app.spring.service;

import hexlet.code.app.spring.dto.TaskDTO;
import hexlet.code.app.spring.dto.create.TaskCreateDTO;
import hexlet.code.app.spring.dto.update.TaskUpdateDTO;
import hexlet.code.app.spring.mapper.TaskMapper;
import hexlet.code.app.spring.model.Task;
import hexlet.code.app.spring.repository.TaskRepository;
import hexlet.code.app.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utils.ExceptionUtils;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    private static final String TASK_NAME = "task";
    private static final String ASSIGNEE_NAME = "assignee";


    public Page<TaskDTO> getAll(Specification<Task> spec, Integer number, Integer size) {
        var pageRequest = PageRequest.of(number - 1, size);
        return taskRepository.findAll(spec, pageRequest).map(taskMapper::map);
    }

    public TaskDTO getTaskDTO(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException(TASK_NAME, id));
        return taskMapper.map(task);
    }

    public TaskDTO createTaskDTO(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        //createAssigneeId(task, dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(Long id, TaskUpdateDTO dto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException(TASK_NAME, id));
        //updateAssigneeId(task, dto);
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private void createAssigneeId(Task task, TaskCreateDTO dto) {
        var assigneeId = dto.getAssigneeId();
        if (assigneeId != null) {
            var assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> ExceptionUtils.throwResourceNotFoundException(ASSIGNEE_NAME, assigneeId));
            task.setAssignee(assignee);
        }
    }

    private void updateAssigneeId(Task task, TaskUpdateDTO dto) {
        var assigneeIdOptional = dto.getAssigneeId();
        if (assigneeIdOptional.isPresent()) {
            var assigneeId = assigneeIdOptional.get();
            var assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> ExceptionUtils.throwResourceNotFoundException(ASSIGNEE_NAME, assigneeId));
            task.setAssignee(assignee);
        }
    }
}
