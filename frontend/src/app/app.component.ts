import {Component, OnInit} from '@angular/core';
import {NgIf} from '@angular/common';
import {RouterOutlet} from '@angular/router';

import {AuthService} from './core/services/auth.service';

import {HelpWidgetComponent} from './shared/help-widget/help-widget.component';
import {SettingsWidgetComponent} from './shared/settings-widget/settings-widget.component';
import {ProfileWidgetComponent} from './shared/profile-widget/profile-widget.component';
import {GameLabelComponent} from './shared/game-label/game-label.component';
import {NotificationComponent} from './shared/notification/notification.component';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    standalone: true,
    imports: [
        RouterOutlet,
        NgIf,
        HelpWidgetComponent,
        SettingsWidgetComponent,
        ProfileWidgetComponent,
        GameLabelComponent,
        NotificationComponent
    ]
})
export class AppComponent implements OnInit {
    isLoggedIn = false;
    nickname = '';

    constructor(private authService: AuthService) {
    }

    ngOnInit(): void {
        this.initLocalStorageDefaults();
        this.authService.checkSession().subscribe({
            next: () => this.isLoggedIn = true,
            error: () => this.isLoggedIn = false
        });
        this.authService.nickname$.subscribe(nick => {
            this.nickname = nick || localStorage.getItem('username') || 'Player';
        });
    }

    private initLocalStorageDefaults(): void {
        localStorage.setItem('theme', localStorage.getItem('theme') || 'light');
        localStorage.setItem('language', localStorage.getItem('language') || 'en');
        localStorage.setItem('sound', localStorage.getItem('sound') || 'off');
    }
}
