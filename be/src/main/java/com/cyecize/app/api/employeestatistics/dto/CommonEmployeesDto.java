package com.cyecize.app.api.employeestatistics.dto;

import java.util.List;
import lombok.Data;

@Data
public class CommonEmployeesDto {

    private Integer employee1Id;
    private Integer employee2Id;
    private List<CommonProjectDto> commonProjects;
}
