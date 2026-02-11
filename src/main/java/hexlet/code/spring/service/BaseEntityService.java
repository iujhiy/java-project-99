package hexlet.code.spring.service;

import hexlet.code.spring.dto.BaseEntityDTO;
import hexlet.code.spring.dto.create.BaseEntityCreateDTO;
import hexlet.code.spring.dto.update.BaseEntityUpdateDTO;

import java.util.List;

public interface BaseEntityService<DTO extends BaseEntityDTO,
        DTO_CREATE extends BaseEntityCreateDTO,
        DTO_UPDATE extends BaseEntityUpdateDTO> {
    List<DTO> getAll();
    DTO show(Long id);
    DTO create(DTO_CREATE dto);
    DTO update(Long id, DTO_UPDATE dto);
    void delete(Long id);
}
