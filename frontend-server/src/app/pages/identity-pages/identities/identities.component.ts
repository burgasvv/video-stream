import { Component } from '@angular/core';
import {IdentityCardComponent} from '../../../cards/identity-card/identity-card.component';

@Component({
  selector: 'app-identities',
    imports: [
        IdentityCardComponent
    ],
  templateUrl: './identities.component.html',
  styleUrl: './identities.component.css'
})
export class IdentitiesComponent {

}
