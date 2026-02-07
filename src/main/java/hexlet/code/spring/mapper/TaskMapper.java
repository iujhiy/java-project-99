package hexlet.code.spring.mapper;

import hexlet.code.spring.dto.TaskDTO;
import hexlet.code.spring.dto.create.TaskCreateDTO;
import hexlet.code.spring.dto.update.TaskUpdateDTO;
import hexlet.code.spring.model.Task;
import hexlet.code.spring.model.TaskStatus;
import hexlet.code.spring.repository.TaskStatusRepository;
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
        return taskStatus != null ? taskStatus.getSlug() : null;
    }

    public TaskStatus map(String taskStatusName) {
        return taskStatusName != null
                ? taskStatusRepository.findBySlug(taskStatusName)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("task status", taskStatusName))
                : null;
    }

    @Mapping(source = "labels", target = "taskLabelIds")
    @Mapping(source = "assignee", target = "assigneeId")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "taskLabelIds", target = "labels")
    @Mapping(source = "assigneeId", target = "assignee")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "taskLabelIds", target = "labels")
    @Mapping(source = "assigneeId", target = "assignee")
    public abstract Task map(TaskDTO dto);

    @Mapping(source = "labels", target = "taskLabelIds")
    @Mapping(source = "assignee", target = "assigneeId")
    public abstract TaskCreateDTO create(Task model);

    @Mapping(source = "taskLabelIds", target = "labels")
    @Mapping(source = "assigneeId", target = "assignee")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

}
