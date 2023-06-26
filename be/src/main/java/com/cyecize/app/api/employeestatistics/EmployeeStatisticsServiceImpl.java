package com.cyecize.app.api.employeestatistics;

import com.cyecize.app.api.employeestatistics.dto.CommonEmployeesDto;
import com.cyecize.app.api.employeestatistics.dto.EmployeeWorkEntry;
import com.cyecize.ioc.annotations.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class EmployeeStatisticsServiceImpl implements EmployeeStatisticsService {

    @Override
    public CommonEmployeesDto extractLongestCommonEmployees(List<EmployeeWorkEntry> workEntries) {
        return null;
    }
}
