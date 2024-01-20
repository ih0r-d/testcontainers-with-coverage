package com.example.controller;

import com.example.dto.ApplianceDto;
import com.example.mapper.ApplianceMapper;
import com.example.service.ApplianceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/appliances")
@RequiredArgsConstructor
@Validated
public class ApplianceRestController {

    private final ApplianceService service;
    private final ApplianceMapper mapper;

    @PostMapping
    public ResponseEntity<ApplianceDto> create(@Valid @RequestBody ApplianceDto dto){
        var appliance = service.create(mapper.from(dto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.to(appliance));
    }

    @GetMapping
    public ResponseEntity<List<ApplianceDto>> getAppliances(){
        var appliances = service.getAllAppliances().stream()
                .map(mapper::to)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appliances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplianceDto> getApplianceById(@PathVariable long id){
        var appliance = service.getApplianceById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.to(appliance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable long id){
        service.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
