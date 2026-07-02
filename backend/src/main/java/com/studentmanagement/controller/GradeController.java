package com.studentmanagement.controller;

import com.studentmanagement.dto.GradeDTO;
import com.studentmanagement.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/students/{studentId}/grades")
    public ResponseEntity<List<GradeDTO>> getGradesByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getGradesByStudentId(studentId));
    }

    @PostMapping("/students/{studentId}/grades")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GradeDTO> addGradeToStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody GradeDTO gradeDTO
    ) {
        return new ResponseEntity<>(gradeService.addGrade(studentId, gradeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/grades/{gradeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GradeDTO> updateGrade(
            @PathVariable Long gradeId,
            @Valid @RequestBody GradeDTO gradeDTO
    ) {
        return ResponseEntity.ok(gradeService.updateGrade(gradeId, gradeDTO));
    }

    @DeleteMapping("/grades/{gradeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long gradeId) {
        gradeService.deleteGrade(gradeId);
        return ResponseEntity.noContent().build();
    }
}
