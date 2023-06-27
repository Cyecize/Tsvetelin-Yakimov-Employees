import {NgModule} from '@angular/core';
import {PreloadAllModules, RouterModule, Routes} from '@angular/router';
import {AppRoutingPath} from './app-routing.path';

const routes: Routes = [
  {
    path: AppRoutingPath.MOST_COMMON_EMPLOYEES.path,
    loadChildren: () => import('./pages/most-common-employees/most-common-employees.module').then(m => m.MostCommonEmployeesModule),
    data: {
      title: 'contacts'
    }
  },
  {
    path: AppRoutingPath.HOME.path,
    loadChildren: () => import('./pages/home/home.module').then(m => m.HomeModule),
    pathMatch: 'full'
  },
  {
    path: '**',
    loadChildren: () => import('./pages/not-found/not-found.module').then(m => m.NotFoundModule)
  }
];

// canActivate: [AuthenticationGuard],
// canActivateChild: [AuthenticationGuard],

@NgModule({
  imports: [RouterModule.forRoot(routes, {preloadingStrategy: PreloadAllModules})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
