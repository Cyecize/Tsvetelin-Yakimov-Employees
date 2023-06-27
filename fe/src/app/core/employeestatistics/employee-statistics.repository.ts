import {Injectable} from '@angular/core';
import {HttpClientService} from '../../shared/http/http-client.service';
import {Observable} from 'rxjs';
import {CommonEmployeesModel} from './common-employees.model';
import {Endpoints} from '../../shared/http/endpoints';

@Injectable({providedIn: 'root'})
export class EmployeeStatisticsRepository {

  constructor(private http: HttpClientService) {
  }

  getLongestCommonEmployees(payload: FormData): Observable<CommonEmployeesModel> {
    return this.http.post<FormData, CommonEmployeesModel>(
      Endpoints.PROCESS_LONGEST_COMMON_EMPLOYEES,
      payload
    );
  }
}
