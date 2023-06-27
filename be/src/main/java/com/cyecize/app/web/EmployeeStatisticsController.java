package com.cyecize.app.web;

import com.cyecize.app.api.csv.CSVParser;
import com.cyecize.app.api.employeestatistics.EmployeeStatisticsService;
import com.cyecize.app.api.employeestatistics.dto.CommonEmployeesDto;
import com.cyecize.app.api.employeestatistics.dto.EmployeeWorkEntry;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.summer.areas.routing.interfaces.UploadedFile;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.MinLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import com.cyecize.summer.areas.validation.exceptions.ConstraintValidationException;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.areas.validation.services.ObjectValidationService;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
public class EmployeeStatisticsController {

    private final EmployeeStatisticsService employeeStatisticsService;

    private final CSVParser csvParser;

    private final ObjectValidationService objectValidationService;

    private final BindingResult bindingResult;

    @PostMapping(Endpoints.PROCESS_LONGEST_COMMON_EMPLOYEES)
    public CommonEmployeesDto processLongestCommonEmployees(@Valid CSVDto fileDto) {
        final List<EmployeeWorkEntry> workEntries = this.csvParser.parse(
                fileDto.getFile(),
                EmployeeWorkEntry.class
        );

        this.objectValidationService.validateBindingModel(
                new EmployeeWorkEntriesDto(workEntries),
                this.bindingResult
        );

        if (bindingResult.hasErrors()) {
            throw new ConstraintValidationException(String.format(
                    "Object validation completed with %d errors",
                    bindingResult.getErrors().size())
            );
        }

        return this.employeeStatisticsService.extractLongestCommonEmployees(workEntries);
    }

    @Data
    public static class CSVDto {

        @NotNull
        @MinLength(length = 1)
        @MaxLength(length = General.MAX_UPLOAD_FILE_SIZE_BYTES)
        private UploadedFile file;
    }

    @Data
    @AllArgsConstructor
    public static class EmployeeWorkEntriesDto {

        @Valid
        private List<EmployeeWorkEntry> workEntries;
    }
}
