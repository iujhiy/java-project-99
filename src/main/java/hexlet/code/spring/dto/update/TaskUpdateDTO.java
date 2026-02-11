package hexlet.code.spring.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO implements BaseEntityUpdateDTO {
    private JsonNullable<Integer> index = JsonNullable.undefined();

    private JsonNullable<Set<Long>> taskLabelIds;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    @JsonProperty("title")
    @NotBlank
    private JsonNullable<String> name;

    @JsonProperty("content")
    @Lob
    private JsonNullable<String> description;

    @JsonProperty("status")
    @NotBlank
    private JsonNullable<String> taskStatus;
}
