package hexlet.code.app.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LabelCreateDTO {
    @NotBlank
    @Size(min = 3, max = 1000)
    private String name;

    private Set<Long> taskIds;
}
