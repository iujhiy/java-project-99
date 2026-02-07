package hexlet.code.spring.mapper;

import hexlet.code.spring.dto.LabelDTO;
import hexlet.code.spring.dto.create.LabelCreateDTO;
import hexlet.code.spring.dto.update.LabelUpdateDTO;
import hexlet.code.spring.model.Label;
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
public abstract class LabelMapper {
    public abstract LabelDTO map(Label model);

    public abstract Label map(LabelCreateDTO dto);

    public abstract Label map(LabelDTO dto);

    public abstract LabelCreateDTO create(Label model);

    public abstract void update(LabelUpdateDTO dto, @MappingTarget Label model);

}
