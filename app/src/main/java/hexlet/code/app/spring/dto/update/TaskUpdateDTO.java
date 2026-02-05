package hexlet.code.app.spring.dto.update;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<Integer> index = JsonNullable.undefined();

    @Lob
    private JsonNullable<String> content = JsonNullable.undefined();

    @NotBlank
    private JsonNullable<String> title = JsonNullable.undefined();

    @NotBlank
    private JsonNullable<String> status = JsonNullable.undefined();

    private JsonNullable<Long> assigneeId = JsonNullable.undefined();

    private JsonNullable<Set<Long>> labelIds = JsonNullable.undefined();

}
