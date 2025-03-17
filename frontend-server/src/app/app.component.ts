import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {IdentityCardComponent} from './cards/identity-card/identity-card.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, IdentityCardComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend-server';
}
