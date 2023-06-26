package com.cyecize.app.api.employeestatistics.dto;

import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class EmployeeWorkEntry {

    @NotNull
    private Integer employeeId;

    @NotNull
    private Integer projectId;

    @NotNull
    private LocalDate dateFrom;

    private LocalDate dateTo;
}
