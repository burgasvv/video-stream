import {Routes} from '@angular/router';
import {IdentityComponent} from './pages/identity/identity.component';
import {IdentitiesComponent} from './pages/identities/identities.component';
import {HomeComponent} from './pages/home/home.component';

export const routes: Routes = [
  {
    path: "",
    component: HomeComponent
  },
  {
    path: "identities",
    component: IdentitiesComponent
  },
  {
    path: "identities/by-id",
    component: IdentityComponent
  }
];
