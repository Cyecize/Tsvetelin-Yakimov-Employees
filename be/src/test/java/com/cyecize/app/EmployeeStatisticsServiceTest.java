package com.cyecize.app;

import com.cyecize.app.api.employeestatistics.EmployeeStatisticsServiceImpl;
import com.cyecize.app.api.employeestatistics.dto.CommonEmployeesDto;
import com.cyecize.app.api.employeestatistics.dto.EmployeeWorkEntry;
import java.time.LocalDate;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmployeeStatisticsServiceTest {

    private EmployeeStatisticsServiceImpl employeeStatisticsService;

    @Before
    public void init() {
        this.employeeStatisticsService = new EmployeeStatisticsServiceImpl();
    }

    @Test
    public void testExtractLongestCommonEmployees_simplePayload() {
        EmployeeWorkEntry workEntry1 = new EmployeeWorkEntry();
        workEntry1.setEmployeeId(1);
        workEntry1.setProjectId(10);
        workEntry1.setDateFrom(LocalDate.of(2010, 1, 1));
        workEntry1.setDateTo(LocalDate.of(2010, 1, 10));

        EmployeeWorkEntry workEntry2 = new EmployeeWorkEntry();
        workEntry2.setEmployeeId(2);
        workEntry2.setProjectId(10);
        workEntry2.setDateFrom(LocalDate.of(2009, 1, 1));
        workEntry2.setDateTo(LocalDate.of(2010, 1, 5));

        CommonEmployeesDto res = this.employeeStatisticsService.extractLongestCommonEmployees(
                List.of(workEntry1, workEntry2)
        );

        Assert.assertEquals(4, res.getElapsedDays());
        Assert.assertEquals(1, res.getCommonProjects().size());
        Assert.assertEquals(4, res.getCommonProjects().stream().findFirst().get().getElapsedDays());
    }

    @Test
    public void testExtractLongestCommonEmployees_noMatchingProjects() {
        EmployeeWorkEntry workEntry1 = new EmployeeWorkEntry();
        workEntry1.setEmployeeId(1);
        workEntry1.setProjectId(10);
        workEntry1.setDateFrom(LocalDate.of(2010, 1, 1));
        workEntry1.setDateTo(LocalDate.of(2010, 1, 10));

        EmployeeWorkEntry workEntry2 = new EmployeeWorkEntry();
        workEntry2.setEmployeeId(2);
        workEntry2.setProjectId(11);
        workEntry2.setDateFrom(LocalDate.of(2009, 1, 1));
        workEntry2.setDateTo(LocalDate.of(2010, 1, 5));

        CommonEmployeesDto res = this.employeeStatisticsService.extractLongestCommonEmployees(
                List.of(workEntry1, workEntry2)
        );

        Assert.assertNull(res);
    }

    @Test
    public void testExtractLongestCommonEmployees_noOverlappingDates() {
        EmployeeWorkEntry workEntry1 = new EmployeeWorkEntry();
        workEntry1.setEmployeeId(1);
        workEntry1.setProjectId(10);
        workEntry1.setDateFrom(LocalDate.of(2010, 1, 1));
        workEntry1.setDateTo(LocalDate.of(2010, 1, 10));

        EmployeeWorkEntry workEntry2 = new EmployeeWorkEntry();
        workEntry2.setEmployeeId(2);
        workEntry2.setProjectId(10);
        workEntry2.setDateFrom(LocalDate.of(2007, 1, 1));
        workEntry2.setDateTo(LocalDate.of(2008, 1, 5));

        CommonEmployeesDto res = this.employeeStatisticsService.extractLongestCommonEmployees(
                List.of(workEntry1, workEntry2)
        );

        Assert.assertNull(res);
    }

    @Test
    public void testExtractLongestCommonEmployees_multipleMatchingProjects() {
        EmployeeWorkEntry workEntry1 = new EmployeeWorkEntry();
        workEntry1.setEmployeeId(1);
        workEntry1.setProjectId(10);
        workEntry1.setDateFrom(LocalDate.of(2010, 1, 1));
        workEntry1.setDateTo(LocalDate.of(2010, 1, 10));

        EmployeeWorkEntry workEntry2 = new EmployeeWorkEntry();
        workEntry2.setEmployeeId(1);
        workEntry2.setProjectId(80);
        workEntry2.setDateFrom(LocalDate.of(2015, 7, 15));
        workEntry2.setDateTo(LocalDate.of(2015, 7, 20));

        EmployeeWorkEntry workEntry3 = new EmployeeWorkEntry();
        workEntry3.setEmployeeId(2);
        workEntry3.setProjectId(10);
        workEntry3.setDateFrom(LocalDate.of(2009, 1, 1));
        workEntry3.setDateTo(LocalDate.of(2010, 1, 5));

        EmployeeWorkEntry workEntry4 = new EmployeeWorkEntry();
        workEntry4.setEmployeeId(2);
        workEntry4.setProjectId(80);
        workEntry4.setDateFrom(LocalDate.of(2015, 7, 18));
        workEntry4.setDateTo(LocalDate.of(2020, 1, 5));


        CommonEmployeesDto res = this.employeeStatisticsService.extractLongestCommonEmployees(
                List.of(workEntry1, workEntry2, workEntry3, workEntry4)
        );

        Assert.assertEquals(6, res.getElapsedDays());
        Assert.assertEquals(2, res.getCommonProjects().size());
        Assert.assertEquals(4, res.getCommonProjects().stream().findFirst().get().getElapsedDays());
        Assert.assertEquals(2, res.getCommonProjects().stream().skip(1).findFirst().get().getElapsedDays());
    }

    @Test
    public void testExtractLongestCommonEmployees_employeeWorksOnSameProjectMoreThanOnce() {
        EmployeeWorkEntry workEntry1 = new EmployeeWorkEntry();
        workEntry1.setEmployeeId(1);
        workEntry1.setProjectId(10);
        workEntry1.setDateFrom(LocalDate.of(2010, 1, 1));
        workEntry1.setDateTo(LocalDate.of(2010, 1, 11));

        EmployeeWorkEntry workEntry2 = new EmployeeWorkEntry();
        workEntry2.setEmployeeId(1);
        workEntry2.setProjectId(10);
        workEntry2.setDateFrom(LocalDate.of(2015, 7, 15));
        workEntry2.setDateTo(LocalDate.of(2015, 7, 20));

        EmployeeWorkEntry workEntry3 = new EmployeeWorkEntry();
        workEntry3.setEmployeeId(2);
        workEntry3.setProjectId(10);
        workEntry3.setDateFrom(LocalDate.of(2001, 1, 1));
        workEntry3.setDateTo(LocalDate.of(2011, 1, 1));

        EmployeeWorkEntry workEntry4 = new EmployeeWorkEntry();
        workEntry4.setEmployeeId(1);
        workEntry4.setProjectId(10);
        workEntry4.setDateFrom(LocalDate.of(2012, 11, 10));
        workEntry4.setDateTo(LocalDate.of(2012, 11, 15));

        EmployeeWorkEntry workEntry5 = new EmployeeWorkEntry();
        workEntry5.setEmployeeId(2);
        workEntry5.setProjectId(10);
        workEntry5.setDateFrom(LocalDate.of(2011, 12, 1));
        workEntry5.setDateTo(LocalDate.of(2022, 1, 1));

        CommonEmployeesDto res = this.employeeStatisticsService.extractLongestCommonEmployees(
                List.of(workEntry1, workEntry2, workEntry3, workEntry4, workEntry5)
        );

        Assert.assertEquals(20, res.getElapsedDays());
        Assert.assertEquals(1, res.getCommonProjects().size());
    }

    @Test
    public void testExtractLongestCommonEmployees_nullToDate() {
        EmployeeWorkEntry workEntry1 = new EmployeeWorkEntry();
        workEntry1.setEmployeeId(1);
        workEntry1.setProjectId(10);
        workEntry1.setDateFrom(LocalDate.now().minusDays(10));
        workEntry1.setDateTo(null);

        EmployeeWorkEntry workEntry2 = new EmployeeWorkEntry();
        workEntry2.setEmployeeId(2);
        workEntry2.setProjectId(10);
        workEntry2.setDateFrom(LocalDate.now().minusDays(30));
        workEntry2.setDateTo(LocalDate.now().minusDays(4));

        CommonEmployeesDto res = this.employeeStatisticsService.extractLongestCommonEmployees(
                List.of(workEntry1, workEntry2)
        );

        Assert.assertEquals(6, res.getElapsedDays());
        Assert.assertEquals(1, res.getCommonProjects().size());
        Assert.assertEquals(6, res.getCommonProjects().stream().findFirst().get().getElapsedDays());
    }
}
