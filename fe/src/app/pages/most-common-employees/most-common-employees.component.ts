import {Component, OnInit} from '@angular/core';
import {EmployeeStatisticsService} from '../../core/employeestatistics/employee-statistics.service';
import {FieldError} from '../../shared/field-error/field-error';
import {CommonEmployeesModel} from '../../core/employeestatistics/common-employees.model';

@Component({
  selector: 'app-most-common-employees',
  templateUrl: './most-common-employees.component.html',
  styleUrls: ['./most-common-employees.component.scss']
})
export class MostCommonEmployeesComponent implements OnInit {

  displayingData = false;
  errors: FieldError[] = [];
  responseData!: CommonEmployeesModel;

  constructor(private employeeStatisticsService: EmployeeStatisticsService) {
  }

  ngOnInit(): void {
  }

  async onFormSubmitted($event: FormData): Promise<void> {
    this.errors = [];
    const resp = await this.employeeStatisticsService.getLongestCommonEmployees($event);
    this.errors = resp.errors;
    if (resp.isSuccess) {
      this.responseData = resp.response;
      this.displayingData = true;
    }
  }

  showForm(): void {
    this.displayingData = false;
  }
}
