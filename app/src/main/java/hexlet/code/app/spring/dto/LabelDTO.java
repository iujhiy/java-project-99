package hexlet.code.app.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class LabelDTO {
    private Long id;
    private String name;
    private Set<Long> taskIds;
    private LocalDate createdAt;
}
