import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';

@Injectable({providedIn: 'root'})
export class RedirectGuard implements CanActivate {
    constructor(private router: Router) {
    }

    canActivate(): boolean {
        const hasPlayer = !!localStorage.getItem('player');
        this.router.navigate([hasPlayer ? '/' : '/auth']);
        return false;
    }
}
