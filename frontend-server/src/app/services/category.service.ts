import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Category} from '../interfaces/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

    httpClient = inject(HttpClient);
    baseUrl: string = "http://localhost:8888";

    public getCategories() {
        return this.httpClient
            .get<Category[]>(this.baseUrl + "/categories");
    }

    public getCategory() {
        return this.httpClient
            .get<Category>(this.baseUrl + "/categories/by-id");
    }
}
