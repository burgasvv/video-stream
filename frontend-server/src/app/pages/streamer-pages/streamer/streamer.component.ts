import {Component, inject, Input} from '@angular/core';
import {StreamerService} from '../../../services/streamer.service';
import {Streamer} from '../../../interfaces/streamer';

@Component({
  selector: 'app-streamer',
    imports: [],
  templateUrl: './streamer.component.html',
  styleUrl: './streamer.component.css'
})
export class StreamerComponent {

    streamerService = inject(StreamerService);
    @Input() streamer!: Streamer;

    constructor() {
        this.streamerService
            .getStreamer()
            .subscribe(value => this.streamer = value);
    }
}
