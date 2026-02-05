package hexlet.code.app.spring.mapper;

import hexlet.code.app.spring.model.BaseEntity;
import hexlet.code.app.spring.model.Label;
import hexlet.code.app.spring.model.Task;
import hexlet.code.app.spring.model.User;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;

    public Set<Task> toTask(JsonNullable<Set<Long>> jsonSet) {
        return toSet(jsonSet, Task.class);
    }

    public Set<Label> toLabel(JsonNullable<Set<Long>> jsonSet) {
        return toSet(jsonSet, Label.class);
    }

    public Set<User> toUsers(JsonNullable<Set<Long>> jsonSet) {
        return toSet(jsonSet, User.class);
    }

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    public <T extends BaseEntity> Set<T> toSet(JsonNullable<Set<Long>> entities,
                                               @TargetType Class<T> entityClass) {
        if (!entities.isPresent()) {
            return null;
        }
        var valueSet = entities.get();
        if (valueSet == null) {
            return null;
        }
        if (valueSet.isEmpty()) {
            return new HashSet<>();
        }
        return valueSet.stream()
                .map(value -> toEntity(value, entityClass))
                .collect(Collectors.toSet());
    }

    public <T extends BaseEntity> Long toId(T entity) {
        if (entity == null) {
            return null;
        }
        try {
            var getIdMethod = entity.getClass().getMethod("getId");
            var id = getIdMethod.invoke(entity);
            return id != null ? ((Number) id).longValue() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public <T extends BaseEntity> Set<Long> toSetLong(Set<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        return entities.stream()
                .map(this::toId)
                .collect(Collectors.toSet());
    }

}
