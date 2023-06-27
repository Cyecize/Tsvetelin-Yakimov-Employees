package com.cyecize.app.api.employeestatistics.dto;

import static com.cyecize.app.constants.General.INVALID_VALUE_MSG;

import com.cyecize.app.converters.DateConverter;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class EmployeeWorkEntry {

    @NotNull(message = INVALID_VALUE_MSG)
    private Integer employeeId;

    @NotNull(message = INVALID_VALUE_MSG)
    private Integer projectId;

    @NotNull(message = INVALID_VALUE_MSG)
    @DateConverter
    private LocalDate dateFrom;

    @DateConverter
    private LocalDate dateTo;
}
