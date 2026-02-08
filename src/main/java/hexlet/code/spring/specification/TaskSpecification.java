package hexlet.code.spring.specification;

import hexlet.code.spring.dto.parametres.TaskParamsDTO;
import hexlet.code.spring.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO dto) {
        return withTitleCont(dto.getTitleCont())
                .and(withAssigneeId(dto.getAssigneeId()))
                .and(withStatus(dto.getStatus()))
                .and(withLabelId(dto.getLabelId()));
    }

    private Specification<Task> withTitleCont(String title) {
        return (root, query, cb) ->
                title != null
                        ? cb.like(cb.lower(root.get("name")), "%" + title.trim().toLowerCase() + "%")
                        : cb.conjunction();
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) ->
                assigneeId != null ? cb.equal(root.get("assignee").get("id"), assigneeId) : cb.conjunction();
    }

    private Specification<Task> withStatus(String slug) {
        return (root, query, cb) ->
                slug != null ? cb.equal(root.get("taskStatus").get("slug"), slug) : cb.conjunction();
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) ->
                labelId != null ? cb.equal(root.get("labels").get("id"), labelId) : cb.conjunction();
    }

}
