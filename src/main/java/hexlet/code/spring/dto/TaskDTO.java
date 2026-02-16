package hexlet.code.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class TaskDTO implements BaseEntityDTO {
    private Long id;

    private Set<Long> taskLabelIds;

    private Integer index;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    @JsonProperty("title")
    private String name;

    @JsonProperty("content")
    private String description;

    @JsonProperty("status")
    private String taskStatus;
}
