import {Component, Input} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {Streamer} from '../../interfaces/streamer';

@Component({
  selector: 'app-streamer-card',
    imports: [
        NgOptimizedImage
    ],
  templateUrl: './streamer-card.component.html',
  styleUrl: './streamer-card.component.css'
})
export class StreamerCardComponent {

    @Input() streamer!: Streamer;
}
