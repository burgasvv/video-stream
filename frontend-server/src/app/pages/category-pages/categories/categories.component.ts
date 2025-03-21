import {Component, inject} from '@angular/core';
import {CategoryService} from '../../../services/category.service';
import {Category} from '../../../interfaces/category';
import {CategoryCardComponent} from '../../../cards/category-card/category-card.component';

@Component({
  selector: 'app-categories',
    imports: [
        CategoryCardComponent
    ],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css'
})
export class CategoriesComponent {

    categoryService = inject(CategoryService);
    categories: Category[] = [];

    constructor() {
        this.categoryService
            .getCategories()
            .subscribe(value => this.categories = value);
    }
}
