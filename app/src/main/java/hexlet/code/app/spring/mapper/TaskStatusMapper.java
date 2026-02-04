package hexlet.code.app.spring.mapper;

import hexlet.code.app.spring.dto.TaskStatusDTO;
import hexlet.code.app.spring.dto.create.TaskStatusCreateDTO;
import hexlet.code.app.spring.dto.update.TaskStatusUpdateDTO;
import hexlet.code.app.spring.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    @Mapping(target = "taskIds", source = "tasks")
    public abstract TaskStatusDTO map(TaskStatus model);

    @Mapping(target = "tasks", source = "taskIds")
    public abstract TaskStatus map(TaskStatusCreateDTO dto);

    @Mapping(target = "tasks", source = "taskIds")
    public abstract TaskStatus map(TaskStatusDTO dto);

    @Mapping(target = "taskIds", source = "tasks")
    public abstract TaskStatusCreateDTO create(TaskStatus model);

    @Mapping(target = "tasks", source = "taskIds")
    public abstract void update(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus model);

}
