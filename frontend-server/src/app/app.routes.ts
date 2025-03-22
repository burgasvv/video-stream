import {Routes} from '@angular/router';
import {HomeComponent} from './pages/main-pages/home/home.component';
import {StreamersComponent} from './pages/streamer-pages/streamers/streamers.component';
import {StreamerComponent} from './pages/streamer-pages/streamer/streamer.component';
import {CategoriesComponent} from './pages/category-pages/categories/categories.component';
import {CategoryComponent} from './pages/category-pages/category/category.component';
import {LoginComponent} from './pages/main-pages/login/login.component';
import {LayoutComponent} from './layout/layout.component';

export const routes: Routes = [
    {
        path: "", component: LayoutComponent, children: [
            {path: "", component: HomeComponent},
            {path: "streamers", component: StreamersComponent},
            {path: "streamers/by-id", component: StreamerComponent},
            {path: "categories", component: CategoriesComponent},
            {path: "categories/by-id", component: CategoryComponent},
        ]
    },
    {path: "login", component: LoginComponent}
];
