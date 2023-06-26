package com.cyecize.app.api.employeestatistics;

import com.cyecize.app.api.employeestatistics.dto.CommonEmployeesDto;
import com.cyecize.app.api.employeestatistics.dto.EmployeeWorkEntry;
import java.util.List;

public interface EmployeeStatisticsService {

    CommonEmployeesDto extractLongestCommonEmployees(List<EmployeeWorkEntry> workEntries);
}
