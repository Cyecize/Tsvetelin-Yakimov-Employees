import {Injectable} from '@angular/core';
import {EmployeeStatisticsRepository} from './employee-statistics.repository';
import {FieldErrorWrapper, WrappedResponse} from '../../shared/util/field-error-wrapper';
import {CommonEmployeesModel} from './common-employees.model';


@Injectable({providedIn: 'root'})
export class EmployeeStatisticsService {
  constructor(private repo: EmployeeStatisticsRepository) {
  }

  public async getLongestCommonEmployees(payload: FormData): Promise<WrappedResponse<CommonEmployeesModel>> {
    return new FieldErrorWrapper(() => this.repo.getLongestCommonEmployees(payload)).execute();
  }
}
