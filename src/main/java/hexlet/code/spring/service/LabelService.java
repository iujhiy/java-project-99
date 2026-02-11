package hexlet.code.spring.service;

import hexlet.code.spring.dto.LabelDTO;
import hexlet.code.spring.dto.create.LabelCreateDTO;
import hexlet.code.spring.dto.update.LabelUpdateDTO;
import hexlet.code.spring.mapper.LabelMapper;
import hexlet.code.spring.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import utils.ExceptionUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService implements BaseEntityService<LabelDTO, LabelCreateDTO, LabelUpdateDTO> {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        return labelRepository.findAll().stream().map(labelMapper::map).toList();
    }

    public LabelDTO show(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("label", id));
        return labelMapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO dto) {
        var label = labelMapper.map(dto);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO update(Long id, LabelUpdateDTO dto) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("label", id));
        labelMapper.update(dto, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
