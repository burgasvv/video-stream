import {Component, inject} from '@angular/core';
import {CategoryService} from '../../../services/category.service';
import {Category} from '../../../interfaces/category';

@Component({
  selector: 'app-category',
  imports: [],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent {

    categoryService = inject(CategoryService);
    category!: Category;

    public getCategory() {
        return this.categoryService
            .getCategory()
            .subscribe(value => this.category = value);
    }
}
