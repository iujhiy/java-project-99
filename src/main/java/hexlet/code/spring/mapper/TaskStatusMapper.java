package hexlet.code.spring.mapper;

import hexlet.code.spring.dto.TaskStatusDTO;
import hexlet.code.spring.dto.create.TaskStatusCreateDTO;
import hexlet.code.spring.dto.update.TaskStatusUpdateDTO;
import hexlet.code.spring.model.TaskStatus;
import org.mapstruct.Mapper;
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
    public abstract TaskStatusDTO map(TaskStatus model);
    public abstract TaskStatus map(TaskStatusCreateDTO dto);
    public abstract TaskStatus map(TaskStatusDTO dto);
    public abstract TaskStatusCreateDTO create(TaskStatus model);
    public abstract void update(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus model);

}
