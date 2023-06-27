import {NgModule} from '@angular/core';
import {SharedModule} from '../../shared/shared.module';
import {MostCommonEmployeesComponent} from './most-common-employees.component';
import {RouterModule} from '@angular/router';
import {FormComponent} from './form/form.component';
import {DataGridComponent} from './data-grid/data-grid.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([
    {
      path: '',
      component: MostCommonEmployeesComponent
    }
  ])],
  declarations: [MostCommonEmployeesComponent, FormComponent, DataGridComponent],
  exports: [MostCommonEmployeesComponent]
})
export class MostCommonEmployeesModule {
  constructor() {
  }
}
