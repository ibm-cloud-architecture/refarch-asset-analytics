import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';
import { Router }   from '@angular/router';

@Component({
  selector: 'app-tile',
  templateUrl: './tile.component.html',
  styleUrls: ['./tile.component.css']
})
export class TileComponent implements OnInit {
  @Input()
  id: string = '';
  @Input()
  color: string = '#8c4507';
  @Input()
  title: string = 'title';
  @Input()
  description: string = 'Short description';
  @Input()
  buttonName: string = 'Submit';
  @Input()
  urlPath: string = 'home';
  style: string;
  externalUrl: boolean = false;

  constructor(private router: Router) { }

  ngOnInit() {
    this.style = '10px 10px 3px ' + this.color;
    this.externalUrl = (this.urlPath.startsWith("http"));
  }

  submit() {
    console.log('Go to ' + this.urlPath);
    this.router.navigate([this.urlPath]);
  }

}
