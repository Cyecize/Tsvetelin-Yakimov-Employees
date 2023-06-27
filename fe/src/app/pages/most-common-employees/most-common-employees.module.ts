import {NgModule} from '@angular/core';
import {SharedModule} from '../../shared/shared.module';
import {MostCommonEmployeesComponent} from './most-common-employees.component';
import {RouterModule} from '@angular/router';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([
    {
      path: '',
      component: MostCommonEmployeesComponent
    }
  ])],
  declarations: [MostCommonEmployeesComponent],
  exports: [MostCommonEmployeesComponent]
})
export class MostCommonEmployeesModule {
  constructor() {
  }
}
