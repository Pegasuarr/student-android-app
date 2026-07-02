package com.studentmanagement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {

    private Long id;

    private Long studentId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be at least 0.0")
    @Max(value = 100, message = "Score cannot exceed 100.0")
    private Double score;

    @NotNull(message = "Assessment date is required")
    private LocalDate assessmentDate;

    private String remarks;
}
