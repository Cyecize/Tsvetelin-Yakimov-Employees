package com.cyecize.app.api.employeestatistics.dto;

import lombok.Data;

@Data
public class CommonProjectDto {

    private final int projectId;
    private long elapsedDays = 0;
}
