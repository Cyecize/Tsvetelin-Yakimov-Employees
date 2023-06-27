import {NgModule} from '@angular/core';
import {HomeComponent} from './home.component';
import {SharedModule} from '../../shared/shared.module';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  }
];

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
  ],
  declarations: [HomeComponent],
  exports: [HomeComponent]
})
export class HomeModule {

}
