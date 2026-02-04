package hexlet.code.app.spring.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskStatusUpdateDTO {
    @NotBlank
    private JsonNullable<String> name = JsonNullable.undefined();

    @NotBlank
    private JsonNullable<String> slug = JsonNullable.undefined();

    private JsonNullable<Set<Long>> taskIds;
}
