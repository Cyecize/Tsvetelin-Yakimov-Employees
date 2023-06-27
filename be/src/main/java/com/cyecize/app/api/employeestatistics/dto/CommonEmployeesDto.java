package com.cyecize.app.api.employeestatistics.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class CommonEmployeesDto {

    private final int employee1Id;
    private final int employee2Id;
    private long elapsedDays = 0;

    @JsonIgnore
    private final Map<Integer, CommonProjectDto> commonProjects = new HashMap<>();

    @JsonProperty("commonProjects")
    public Collection<CommonProjectDto> getCommonProjects() {
        return this.commonProjects.values();
    }

    public void addElapsedTime(int projectId, long timeInDays) {
        this.elapsedDays += timeInDays;
        final CommonProjectDto commonProject = this.commonProjects.computeIfAbsent(
                projectId,
                CommonProjectDto::new
        );

        commonProject.setElapsedDays(commonProject.getElapsedDays() + timeInDays);
    }
}
