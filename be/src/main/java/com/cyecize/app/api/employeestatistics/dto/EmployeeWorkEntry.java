package com.cyecize.app.api.employeestatistics.dto;

import com.cyecize.app.api.validators.DateIsAfterField;
import com.cyecize.app.api.validators.DateNotFuture;
import com.cyecize.app.converters.DateConverter;
import com.cyecize.summer.areas.validation.annotations.RejectedValueExclude;
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
    @DateNotFuture(message = "\"Date From\" should not be a future date!")
    @RejectedValueExclude
    private LocalDate dateFrom;

    @DateConverter
    @DateNotFuture(message = "\"Date To\" should not be a future date!")
    @DateIsAfterField(value = "dateFrom", message = "\"Date To\" is not after \"Date From\"!")
    @RejectedValueExclude
    private LocalDate dateTo;
}
