import {Injectable} from '@angular/core';
import {CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {Observable, of} from 'rxjs';
import {catchError, map} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(private authService: AuthService, private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        const isPublicRoute = state.url.startsWith('/auth/login') || state.url.startsWith('/auth/register');

        return this.authService.checkSession().pipe(
            map(() => {
                if (isPublicRoute) {
                    this.router.navigate(['/']);
                    return false;
                }
                return true;
            }),
            catchError((err) => {
                if (!isPublicRoute) {
                    this.router.navigate(['/auth/login']);
                    return of(false);
                }
                return of(true);
            })
        );
    }
}
