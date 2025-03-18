import {Component, inject} from '@angular/core';
import {StreamerCardComponent} from '../../../cards/streamer-card/streamer-card.component';
import {JsonPipe} from '@angular/common';
import {StreamerService} from '../../../services/streamer/streamer.service';

@Component({
  selector: 'app-streamers',
    imports: [
        StreamerCardComponent,
        JsonPipe
    ],
  templateUrl: './streamers.component.html',
  styleUrl: './streamers.component.css'
})
export class StreamersComponent {

    streamerService = inject(StreamerService);
    streamers: any = [];

    constructor() {
        this.streamerService
            .getStreamers()
            .subscribe(value => this.streamers = value);
    }
}
