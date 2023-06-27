package com.cyecize.app.api.employeestatistics;

import com.cyecize.app.api.employeestatistics.dto.CommonEmployeesDto;
import com.cyecize.app.api.employeestatistics.dto.EmployeeWorkEntry;
import com.cyecize.ioc.annotations.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeStatisticsServiceImpl implements EmployeeStatisticsService {

    @Override
    public CommonEmployeesDto extractLongestCommonEmployees(List<EmployeeWorkEntry> workEntries) {
        final Map<Integer, Map<Integer, EmployeeProjectId>> employeeProjects = new HashMap<>();
        final Map<Integer, Set<Integer>> projectEmployeesIndex = new HashMap<>();

        // Set up indexes
        for (final EmployeeWorkEntry workEntry : workEntries) {
            employeeProjects
                    .computeIfAbsent(workEntry.getEmployeeId(), k -> new HashMap<>())
                    .computeIfAbsent(workEntry.getProjectId(), i -> new EmployeeProjectId(
                            workEntry.getEmployeeId(),
                            workEntry.getProjectId()
                    ))
                    .workEntries.add(new WorkEntryDto(
                            workEntry.getDateFrom(),
                            Objects.requireNonNullElse(workEntry.getDateTo(), LocalDate.now())
                    ));

            projectEmployeesIndex.computeIfAbsent(workEntry.getProjectId(), k -> new HashSet<>())
                    .add(workEntry.getEmployeeId());
        }

        // Calculate
        final Map<CommonEmployeesKey, CommonEmployeesDto> employeePairs = new HashMap<>();
        for (Map<Integer, EmployeeProjectId> emp1 : employeeProjects.values()) {
            for (EmployeeProjectId employeeProject : emp1.values()) {
                // Use index to grab only employee ids who worked on that project on some point.
                final Set<Integer> colleagueIds = projectEmployeesIndex.get(
                        employeeProject.getProjectId()
                );

                for (Integer colleagueId : colleagueIds) {
                    if (colleagueId.equals(employeeProject.getEmployeeId())
                            || employeeProject.getMatchedEmployees().contains(colleagueId)) {
                        // This employee-employee-project pair was already checked or
                        // we are checking the same person
                        continue;
                    }

                    final EmployeeProjectId colleagueProject = employeeProjects
                            .get(colleagueId)
                            .get(employeeProject.getProjectId());

                    colleagueProject.getMatchedEmployees().add(employeeProject.getEmployeeId());
                    employeeProject.getMatchedEmployees().add(colleagueId);

                    // Matching employees' work entries for a given project to find any overlaps
                    long total = 0L;
                    for (WorkEntryDto workEntry : employeeProject.getWorkEntries()) {
                        for (WorkEntryDto colleagueWorkEntry : colleagueProject.getWorkEntries()) {
                            total += calculateDuration(workEntry, colleagueWorkEntry);
                        }
                    }

                    if (total <= 0L) {
                        // These colleagues never met on that project
                        continue;
                    }

                    employeePairs.computeIfAbsent(
                            CommonEmployeesKey.of(employeeProject.getEmployeeId(), colleagueId),
                            employeesPairKey -> new CommonEmployeesDto(
                                    employeeProject.getEmployeeId(),
                                    colleagueId
                            )
                    ).addElapsedTime(employeeProject.getProjectId(), total);
                }
            }
        }

        return employeePairs.values().stream()
                .max(Comparator.comparingLong(CommonEmployeesDto::getElapsedDays))
                .orElse(null);
    }

    public static <T> boolean containsAny(Set<T> set1, Set<T> set2) {
        for (T value : set2) {
            if (set1.contains(value)) {
                return true;
            }
        }
        return false;
    }

    private static long calculateDuration(WorkEntryDto w1, WorkEntryDto w2) {

        final LocalDate overlapStart = w1.getFrom().isAfter(w2.getFrom())
                ? w1.getFrom() : w2.getFrom();

        final LocalDate overlapEnd =
                w1.getTo() == null || w2.getTo() == null || w1.getTo().isBefore(w2.getTo())
                        ? w1.getTo() : w2.getTo();

        if (overlapStart.isAfter(overlapEnd) || overlapStart.equals(overlapEnd)) {
            return 0; // No overlap or the overlap duration is zero
        }

        return ChronoUnit.DAYS.between(overlapStart, overlapEnd);
    }

    @Data
    static class WorkEntryDto {

        private final LocalDate from;
        private final LocalDate to;
    }

    @Data
    static class EmployeeProjectId {

        private final int employeeId;
        private final int projectId;
        // Employee might have worked on a given project multiple times, this accounts for it.
        private final List<WorkEntryDto> workEntries = new ArrayList<>();
        private final Set<Integer> matchedEmployees = new HashSet<>();
    }

    @EqualsAndHashCode
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class CommonEmployeesKey {

        private final int employee1Id;
        private final int employee2Id;

        static CommonEmployeesKey of(int e1, int e2) {
            return new CommonEmployeesKey(e1, e2);
        }
    }

}
