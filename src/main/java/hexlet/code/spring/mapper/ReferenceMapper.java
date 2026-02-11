package hexlet.code.spring.mapper;

import hexlet.code.spring.model.BaseEntity;
import hexlet.code.spring.model.Label;
import hexlet.code.spring.model.Task;
import hexlet.code.spring.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public class ReferenceMapper {
    @PersistenceContext
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

        Set<Long> ids = valueSet.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (ids.isEmpty()) {
            return new HashSet<>();
        }
        var resultList = entityManager
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.id IN :ids",
                        entityClass)
                .setParameter("ids", ids)
                .getResultList();
        return new HashSet<>(resultList);
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
