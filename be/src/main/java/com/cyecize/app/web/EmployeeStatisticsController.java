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
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
public class EmployeeStatisticsController {

    private final EmployeeStatisticsService employeeStatisticsService;

    private final CSVParser csvParser;

    @PostMapping(Endpoints.PROCESS_LONGEST_COMMON_EMPLOYEES)
    public CommonEmployeesDto processLongestCommonEmployees(@Valid CSVDto fileDto) {
        final List<EmployeeWorkEntry> workEntries = this.csvParser.parse(
                fileDto.getFile(),
                EmployeeWorkEntry.class
        );

        return this.employeeStatisticsService.extractLongestCommonEmployees(workEntries);
    }

    @Data
    static class CSVDto {

        @NotNull
        @MinLength(length = 1)
        @MaxLength(length = General.MAX_UPLOAD_FILE_SIZE_BYTES)
        private UploadedFile file;
    }
}