package hexlet.code.spring.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO implements BaseEntityCreateDTO {
    private String firstName;

    private String lastName;

    @Email
    private String email;

    @NotBlank
    @Size(min = 3, message = "Пароль должен быть минимум 3 символа")
    private String password;
}
