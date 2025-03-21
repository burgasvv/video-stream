import {Component, Input} from '@angular/core';
import {Category} from '../../interfaces/category';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-category-card',
    imports: [
        NgOptimizedImage
    ],
  templateUrl: './category-card.component.html',
  styleUrl: './category-card.component.css'
})
export class CategoryCardComponent {

    @Input() category!: Category;
}
