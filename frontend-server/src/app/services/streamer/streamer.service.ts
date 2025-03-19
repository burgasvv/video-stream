import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Streamer} from '../../interfaces/streamer';

@Injectable({
  providedIn: 'root'
})
export class StreamerService {

    httpClient = inject(HttpClient);
    baseUrl = "http://localhost:8888"

    public getStreamers() {
        return this.httpClient.get<Streamer[]>(this.baseUrl + "/streamers");
    }
}
