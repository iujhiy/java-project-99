package hexlet.code.spring.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusUpdateDTO implements BaseEntityUpdateDTO {
    @NotBlank
    private JsonNullable<String> name = JsonNullable.undefined();

    @NotBlank
    private JsonNullable<String> slug = JsonNullable.undefined();
}
