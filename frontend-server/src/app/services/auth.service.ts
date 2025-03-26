import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    httpClient = inject(HttpClient);
    baseUrl = 'http://localhost:8888';

    public login(payload: {username: string, password: string}) {
        return this.httpClient
            .post(this.baseUrl + "/login", payload);
    }
}
