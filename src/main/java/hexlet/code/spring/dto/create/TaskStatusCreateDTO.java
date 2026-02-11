package hexlet.code.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO implements BaseEntityCreateDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String slug;
}
