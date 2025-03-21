import {Component, Input} from '@angular/core';
import {Category} from '../../interfaces/category';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-category-card',
    imports: [
        NgOptimizedImage,
        RouterLink
    ],
  templateUrl: './category-card.component.html',
  styleUrl: './category-card.component.css'
})
export class CategoryCardComponent {

    @Input() category!: Category;
}
