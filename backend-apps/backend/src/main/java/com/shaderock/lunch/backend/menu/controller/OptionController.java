package com.shaderock.lunch.backend.menu.controller;

import com.shaderock.lunch.backend.menu.model.dto.OptionDto;
import com.shaderock.lunch.backend.menu.service.OptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/option")
@RequiredArgsConstructor
public class OptionController {

  private final OptionService optionService;

  @PostMapping
  public ResponseEntity<OptionDto> create(
      @RequestBody @NotNull @Valid final OptionDto OptionDto) {
    OptionDto persistedOptionDto = optionService.createAndMapToDto(OptionDto);
    return ResponseEntity.ok(persistedOptionDto);
  }

  @GetMapping
  public ResponseEntity<OptionDto> read(@RequestParam @NotBlank final String name) {
    return ResponseEntity.ok(optionService.readAndMapToDto(name));
  }

  @PutMapping
  public ResponseEntity<OptionDto> update(
      @RequestBody @NotNull @Valid final OptionDto OptionDto) {
    OptionDto updatedOptionDto = optionService.updateAndMapToDto(OptionDto);
    return ResponseEntity.ok(updatedOptionDto);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam @NotNull UUID id) {
    optionService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
