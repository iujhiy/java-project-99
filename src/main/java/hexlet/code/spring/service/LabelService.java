package hexlet.code.spring.service;

import hexlet.code.spring.dto.LabelDTO;
import hexlet.code.spring.dto.create.LabelCreateDTO;
import hexlet.code.spring.dto.update.LabelUpdateDTO;
import hexlet.code.spring.mapper.LabelMapper;
import hexlet.code.spring.repository.LabelRepository;
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

    public List<LabelDTO> getAll() {
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
