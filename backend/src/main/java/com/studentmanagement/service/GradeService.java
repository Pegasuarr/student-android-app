package com.studentmanagement.service;

import com.studentmanagement.dto.GradeDTO;
import com.studentmanagement.dto.GradeMapper;
import com.studentmanagement.entity.Grade;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.GradeRepository;
import com.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final GradeMapper gradeMapper;

    public GradeService(
            GradeRepository gradeRepository,
            StudentRepository studentRepository,
            GradeMapper gradeMapper
    ) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.gradeMapper = gradeMapper;
    }

    @Transactional(readOnly = true)
    public List<GradeDTO> getGradesByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        return grades.stream()
                .map(gradeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public GradeDTO addGrade(Long studentId, GradeDTO gradeDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        Grade grade = gradeMapper.toEntity(gradeDTO);
        grade.setStudent(student);

        Grade savedGrade = gradeRepository.save(grade);
        return gradeMapper.toDTO(savedGrade);
    }

    public GradeDTO updateGrade(Long gradeId, GradeDTO gradeDTO) {
        Grade existingGrade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new ResourceNotFoundException("Grade record not found with id: " + gradeId));

        existingGrade.setSubject(gradeDTO.getSubject());
        existingGrade.setScore(gradeDTO.getScore());
        existingGrade.setAssessmentDate(gradeDTO.getAssessmentDate());
        existingGrade.setRemarks(gradeDTO.getRemarks());

        Grade updatedGrade = gradeRepository.save(existingGrade);
        return gradeMapper.toDTO(updatedGrade);
    }

    public void deleteGrade(Long gradeId) {
        Grade existingGrade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new ResourceNotFoundException("Grade record not found with id: " + gradeId));
        gradeRepository.delete(existingGrade);
    }
}
