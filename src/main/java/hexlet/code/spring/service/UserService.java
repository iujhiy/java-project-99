package hexlet.code.spring.service;

import hexlet.code.spring.dto.UserDTO;
import hexlet.code.spring.dto.create.UserCreateDTO;
import hexlet.code.spring.dto.update.UserUpdateDTO;
import hexlet.code.spring.mapper.UserMapper;
import hexlet.code.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ExceptionUtils;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getAll() {
//        var pageRequest = PageRequest.of(number - 1, size);
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }

    public UserDTO getUserDTO(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("user", id));
        return userMapper.map(user);
    }

    public UserDTO createUserDTO(UserCreateDTO dto) {
        var user = userMapper.map(dto);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public UserDTO update(Long id, UserUpdateDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("user", id));
        userMapper.update(dto, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
