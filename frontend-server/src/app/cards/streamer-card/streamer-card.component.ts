import {Component, Input} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {Streamer} from '../../interfaces/streamer';
import {ImgUrlPipe} from '../../pipes/img-url.pipe';

@Component({
  selector: 'app-streamer-card',
    imports: [
        NgOptimizedImage,
        ImgUrlPipe
    ],
  templateUrl: './streamer-card.component.html',
  styleUrl: './streamer-card.component.css'
})
export class StreamerCardComponent {

    @Input() streamer!: Streamer;
}
