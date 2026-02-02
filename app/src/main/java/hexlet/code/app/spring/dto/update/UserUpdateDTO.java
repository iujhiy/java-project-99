package hexlet.code.app.spring.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserUpdateDTO {
    @NotBlank
    private JsonNullable<String> firstName = JsonNullable.undefined();

    @NotBlank
    private JsonNullable<String> lastName = JsonNullable.undefined();

    @Email
    private JsonNullable<String> email = JsonNullable.undefined();

    @NotBlank
    @Size(min = 3)
    private JsonNullable<String> password = JsonNullable.undefined();
}
