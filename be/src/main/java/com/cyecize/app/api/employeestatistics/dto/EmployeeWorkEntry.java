package com.cyecize.app.api.employeestatistics.dto;

import static com.cyecize.app.constants.General.INVALID_VALUE_MSG;

import com.cyecize.app.converters.DateConverter;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class EmployeeWorkEntry {

    @NotNull(message = "Missing or invalid \"Employee Id\"")
    private Integer employeeId;

    @NotNull(message = "Missing or invalid \"Project Id\"")
    private Integer projectId;

    @NotNull(message = "Missing or invalid \"Date From\"")
    @DateConverter
    private LocalDate dateFrom;

    @DateConverter
    private LocalDate dateTo;
}
