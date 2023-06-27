import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {FieldError} from '../../../shared/field-error/field-error';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {

  form!: UntypedFormGroup;

  @Output()
  formSubmitted: EventEmitter<FormData> = new EventEmitter<FormData>();

  @Input()
  errors: FieldError[] = [];

  constructor(private fb: UntypedFormBuilder) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      file: [null, [Validators.required]]
    });
  }

  handleFileInput(eventTarget: any): void {
    this.errors = [];
    this.form.get('file')?.setValue(eventTarget.files[0]);
  }

  onFormSubmit(): void {
    const formData = new FormData();
    formData.append('file', this.form.get('file')?.value);
    this.formSubmitted.emit(formData);
  }

  downloadCSV(): void {
    const csvContent = `EmpID,ProjectID,DateFrom,DateTo
1,5,2011-05-22,2015-01-01
2,5,2014-01-01,2014-01-10
3,5,2005-01-01,2006-01-01
1,5,2004-01-01,2005-01-10
1,4,2005-11-01,2005-11-30
3,4,2005-11-01,2005-11-11
2,4,2005-11-01,2005-11-10`;

    this.downloadFile(csvContent, 'Sample.csv');
  }

  downloadFile(content: string, fileName: string): void {
    const blob = new Blob([content], {type: 'text/csv;charset=utf-8;'});
    const link = document.createElement('a');
    if (link.download !== undefined) {
      // For modern browsers
      const url = URL.createObjectURL(blob);
      link.setAttribute('href', url);
      link.setAttribute('download', fileName);
      link.style.visibility = 'hidden';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  }
}
