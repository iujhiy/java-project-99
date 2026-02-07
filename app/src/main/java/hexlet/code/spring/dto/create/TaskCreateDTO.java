package hexlet.code.spring.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {

    private Integer index;


    @JsonProperty("title")
    @NotBlank
    private String name;

    @JsonProperty("content")
    private String description;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private Set<Long> taskLabelIds;

    @JsonProperty("status")
    @NotBlank
    private String taskStatus;
}
