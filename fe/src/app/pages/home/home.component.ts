import {Component, OnInit} from '@angular/core';
import {AppRoutingPath} from '../../app-routing.path';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  routes = AppRoutingPath;

  ngOnInit(): void {

  }

}
