import {Injector, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FieldErrorModule} from './field-error/field-error.module';


@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    FieldErrorModule
  ],
  exports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    FieldErrorModule,
  ]
})
export class SharedModule {
  private static injector: Injector;

  public static getInjector(): Injector {
    return SharedModule.injector;
  }

  constructor(injector: Injector) {
    SharedModule.injector = injector;
  }
}
