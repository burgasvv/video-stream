import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-login',
    imports: [
        ReactiveFormsModule
    ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

    authService = inject(AuthService);

    form = new FormGroup({
        username: new FormControl(null),
        password: new FormControl(null)
    })

    public onSubmit() {
        if (this.form.valid) {
            // @ts-ignore
            this.authService.login(this.form.value)
        }
    }
}
