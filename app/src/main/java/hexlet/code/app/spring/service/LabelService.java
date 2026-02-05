package hexlet.code.app.spring.service;

import hexlet.code.app.spring.dto.LabelDTO;
import hexlet.code.app.spring.dto.create.LabelCreateDTO;
import hexlet.code.app.spring.dto.update.LabelUpdateDTO;
import hexlet.code.app.spring.mapper.LabelMapper;
import hexlet.code.app.spring.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ExceptionUtils;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getAll(Integer number, Integer size) {
//        var pageRequest = PageRequest.of(number - 1, size);
        return labelRepository.findAll().stream().map(labelMapper::map).toList();
    }

    public LabelDTO getLabelDTO(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils
                        .throwResourceNotFoundException("label", id));
        return labelMapper.map(label);
    }

    public LabelDTO createLabelDTO(LabelCreateDTO dto) {
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

    public void deleteLabel(Long id) {
        labelRepository.deleteById(id);
    }
}
