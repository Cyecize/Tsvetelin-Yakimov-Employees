import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CommonEmployeesModel} from '../../../core/employeestatistics/common-employees.model';

@Component({
  selector: 'app-data-grid',
  templateUrl: './data-grid.component.html',
  styleUrls: ['./data-grid.component.scss']
})
export class DataGridComponent implements OnInit {

  @Output()
  uploadNewFileClick: EventEmitter<void> = new EventEmitter<void>();

  @Input()
  data!: CommonEmployeesModel;

  constructor() {
  }

  ngOnInit(): void {
  }

  onUploadNewFileClick(): void {
    this.uploadNewFileClick.emit();
  }
}
