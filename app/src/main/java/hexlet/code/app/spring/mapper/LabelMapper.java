package hexlet.code.app.spring.mapper;

import hexlet.code.app.spring.dto.LabelDTO;
import hexlet.code.app.spring.dto.create.LabelCreateDTO;
import hexlet.code.app.spring.dto.update.LabelUpdateDTO;
import hexlet.code.app.spring.model.Label;
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
public abstract class LabelMapper {
    @Mapping(target = "taskIds", source = "tasks")
    public abstract LabelDTO map(Label model);

    @Mapping(target = "tasks", source = "taskIds")
    public abstract Label map(LabelCreateDTO dto);

    @Mapping(target = "tasks", source = "taskIds")
    public abstract Label map(LabelDTO dto);

    @Mapping(target = "taskIds", source = "tasks")
    public abstract LabelCreateDTO create(Label model);

    @Mapping(target = "tasks", source = "taskIds")
    public abstract void update(LabelUpdateDTO dto, @MappingTarget Label model);

}
