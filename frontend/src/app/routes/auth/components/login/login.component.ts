import {AfterViewInit, Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {TranslateModule, TranslateService} from '@ngx-translate/core';

import {AuthService} from '../../../../core/services/auth.service';
import {SoundService} from '../../../../core/services/sound.service';
import {NotificationService} from '../../../../core/services/notification.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['../../auth.component.scss'],
    standalone: true,
    imports: [FormsModule, TranslateModule]
})
export class LoginComponent implements AfterViewInit {
    login = '';
    password = '';

    constructor(
        private authService: AuthService,
        private soundService: SoundService,
        private notificationService: NotificationService,
        private translate: TranslateService
    ) {
        const lang = localStorage.getItem('language') || 'en';
        this.translate.use(lang);
    }

    ngAfterViewInit(): void {
        setTimeout(() => {
            const container = document.querySelector('.auth-container');
            container?.querySelector('.welcome-message')?.classList.add('show');
            container?.querySelector('.form-container')?.classList.add('show');
        }, 50);
    }

    checkForm(): boolean {
        const rules = [
            {field: this.login, key: 'ERRORS.EMPTY_LOGIN'},
            {field: this.password, key: 'ERRORS.EMPTY_PASSWORD'}
        ];

        for (const rule of rules) {
            if (rule.field === '') {
                this.soundService.playSound('assets/mp3/error.mp3');
                this.translate.get(rule.key).subscribe(text => this.notificationService.error(text));
                return false;
            }
        }

        return true;
    }

    onLogin(): void {
        if (!this.checkForm()) return;

        this.authService.login(this.login, this.password).subscribe({
            next: (res) => {
                localStorage.setItem('username', res.nickname);
                window.location.reload();
            },
            error: () => {
                this.soundService.playSound('assets/mp3/error.mp3');
                this.translate.get('ERRORS.LOGIN_FAILED').subscribe(text =>
                    this.notificationService.error(text)
                );
            }
        });
    }

    onRedirect(): void {
        this.authService.onRedirect('/auth/register');
    }
}
