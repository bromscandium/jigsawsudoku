import {Routes} from '@angular/router';
import {AuthGuard} from './core/guards/auth.guard';
import {RedirectGuard} from './core/guards/redirect.guard';
import {DummyComponent} from './shared/dummy/dummy.component';

export const routes: Routes = [
    {
        path: 'auth',
        canActivate: [AuthGuard],
        loadChildren: () =>
            import('./routes/auth/auth.module').then(m => m.AuthModule)
    },
    {
        path: '',
        canActivate: [AuthGuard],
        loadComponent: () =>
            import('./routes/menu/menu.component').then(m => m.MenuComponent)
    },
    {
        path: 'game',
        canActivate: [AuthGuard],
        loadChildren: () =>
            import('./routes/game/game.module').then(m => m.GameModule)
    },
    {
        path: 'score-table',
        canActivate: [AuthGuard],
        loadComponent: () =>
            import('./routes/score-table/score-table.component').then(m => m.ScoreTableComponent)
    },
    {
        path: 'reviews',
        canActivate: [AuthGuard],
        loadComponent: () =>
            import('./routes/reviews/reviews.component').then(m => m.ReviewsComponent)
    },
    {
        path: '**',
        canActivate: [RedirectGuard],
        component: DummyComponent
    }
];
