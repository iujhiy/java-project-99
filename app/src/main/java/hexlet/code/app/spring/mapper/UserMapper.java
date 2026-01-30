package hexlet.code.app.spring.mapper;

import hexlet.code.app.spring.dto.UserDTO;
import hexlet.code.app.spring.dto.create.UserCreateDTO;
import hexlet.code.app.spring.dto.update.UserUpdateDTO;
import hexlet.code.app.spring.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
    uses = {JsonNullableMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract UserDTO map(User model);

    @Mapping(target = "passwordDigest", source = "password",
            qualifiedByName = "encodePassword")
    public abstract User map(UserCreateDTO dto);

    public abstract User map(UserDTO dto);

    public abstract UserCreateDTO create(User model);

    @Mapping(target = "passwordDigest", source = "password",
            qualifiedByName = "encodePassword")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    @Named("encodePassword")
    protected String encodePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        return passwordEncoder.encode(password);
    }
}
