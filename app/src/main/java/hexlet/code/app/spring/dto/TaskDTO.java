package hexlet.code.app.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private String content;
    private String status;
    private String title;
    private Long assigneeId;
    private LocalDate createdAt;
}
