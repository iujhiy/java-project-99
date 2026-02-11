package hexlet.code.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LabelDTO implements BaseEntityDTO {
    private Long id;
    private String name;
    private LocalDate createdAt;
}
