package hexlet.code.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskStatusDTO implements BaseEntityDTO {
    private Long id;
    private String name;
    private String slug;
    private LocalDate createdAt;
}
