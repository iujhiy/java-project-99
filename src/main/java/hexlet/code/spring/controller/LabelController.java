package hexlet.code.spring.controller;

import hexlet.code.spring.dto.LabelDTO;
import hexlet.code.spring.dto.create.LabelCreateDTO;
import hexlet.code.spring.dto.update.LabelUpdateDTO;
import hexlet.code.spring.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @GetMapping
    public ResponseEntity<List<LabelDTO>> index() {
        var page = labelService.getAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.size()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable Long id) {
        return labelService.getLabelDTO(id);
    }

    @PostMapping
    public ResponseEntity<LabelDTO> create(@Valid @RequestBody LabelCreateDTO dto) {
        var labelDTO = labelService.createLabelDTO(dto);
        return ResponseEntity
                .created(URI.create("api/labels/" + labelDTO.getId()))
                .body(labelDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody LabelUpdateDTO dto) {
        var labelDTO = labelService.update(id, dto);
        return ResponseEntity.ok(labelDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}
