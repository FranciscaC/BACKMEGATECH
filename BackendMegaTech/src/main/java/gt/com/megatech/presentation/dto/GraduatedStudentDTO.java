package gt.com.megatech.presentation.dto;

import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.enums.MonthEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GraduatedStudentDTO {

    private Long id;
    private MonthEnum graduationMonth;
    private Integer graduationYear;
    private LocalDate graduationDate;
    private StudentEntity studentEntity;
}
