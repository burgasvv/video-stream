import {Component, Input} from '@angular/core';
import {Streamer} from '../../interfaces/streamer';

@Component({
  selector: 'app-streamer-card',
    imports: [],
  templateUrl: './streamer-card.component.html',
  styleUrl: './streamer-card.component.css'
})
export class StreamerCardComponent {

    @Input() streamer!: Streamer;
}
