import { Component } from '@angular/core';
import {StreamerCardComponent} from '../../../cards/streamer-card/streamer-card.component';

@Component({
  selector: 'app-streamer',
    imports: [
        StreamerCardComponent
    ],
  templateUrl: './streamer.component.html',
  styleUrl: './streamer.component.css'
})
export class StreamerComponent {

}
