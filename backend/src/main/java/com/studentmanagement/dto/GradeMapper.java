package com.studentmanagement.dto;

import com.studentmanagement.entity.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GradeMapper {
    GradeMapper INSTANCE = Mappers.getMapper(GradeMapper.class);

    @Mapping(source = "student.id", target = "studentId")
    GradeDTO toDTO(Grade grade);

    @Mapping(source = "studentId", target = "student.id")
    Grade toEntity(GradeDTO gradeDTO);
}
