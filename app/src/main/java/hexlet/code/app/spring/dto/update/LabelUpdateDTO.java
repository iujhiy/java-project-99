package hexlet.code.app.spring.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class LabelUpdateDTO {
    @NotBlank
    @Size(min = 3, max = 1000)
    private JsonNullable<String> name = JsonNullable.undefined();

    private JsonNullable<Set<Long>> taskIds = JsonNullable.undefined();
}
