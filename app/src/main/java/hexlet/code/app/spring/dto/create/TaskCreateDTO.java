package hexlet.code.app.spring.dto.create;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    private Integer index;

    @Lob
    private String content;

    @NotBlank
    private String title;

    @NotBlank
    private String status;

    private Long assigneeId;
}
