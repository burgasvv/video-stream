import { Component } from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
    imports: [
        ReactiveFormsModule
    ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

    form = new FormGroup({
        username: new FormControl(null),
        password: new FormControl(null)
    })

    public onSubmit() {
        console.log(this.form.value);
    }
}
