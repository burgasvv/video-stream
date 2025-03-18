import {Routes} from '@angular/router';
import {IdentityComponent} from './pages/identity-pages/identity/identity.component';
import {IdentitiesComponent} from './pages/identity-pages/identities/identities.component';
import {HomeComponent} from './pages/home/home.component';
import {StreamersComponent} from './pages/streamer-pages/streamers/streamers.component';
import {StreamerComponent} from './pages/streamer-pages/streamer/streamer.component';

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
    },
    {
        path: "streamers",
        component: StreamersComponent
    },
    {
        path: "streamers/by-id",
        component: StreamerComponent
    }
];
