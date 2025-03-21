import { Component } from '@angular/core';

@Component({
  selector: 'app-empty',
  imports: [],
  templateUrl: './empty.component.html',
  styleUrl: './empty.component.css'
})
export class EmptyComponent {

    pageNotFound: string = "404 Page Not Found";
}
