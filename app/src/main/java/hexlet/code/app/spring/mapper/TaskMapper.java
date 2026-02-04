package hexlet.code.app.spring.mapper;

import hexlet.code.app.spring.dto.TaskDTO;
import hexlet.code.app.spring.dto.create.TaskCreateDTO;
import hexlet.code.app.spring.dto.update.TaskUpdateDTO;
import hexlet.code.app.spring.model.Task;
import hexlet.code.app.spring.model.TaskStatus;
import hexlet.code.app.spring.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import utils.ExceptionUtils;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public String map(TaskStatus taskStatus) {
        return taskStatus != null ? taskStatus.getName() : null;
    }

    public TaskStatus map(String taskStatusName) {
        return taskStatusName != null
                ? taskStatusRepository.findByName(taskStatusName)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task status", taskStatusName, "toTask"))
                : null;
    }

    @Mapping(target = "status", source = "taskStatus")
    @Mapping(target = "assigneeId", source = "assignee")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee", source = "assigneeId")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee", source = "assigneeId")
    public abstract Task map(TaskDTO dto);

    @Mapping(target = "status", source = "taskStatus")
    @Mapping(target = "assigneeId", source = "assignee")
    public abstract TaskCreateDTO create(Task model);

    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee", source = "assigneeId")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

}
