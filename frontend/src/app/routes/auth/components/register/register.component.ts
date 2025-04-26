import {AfterViewInit, Component} from '@angular/core';
import {AuthService} from '../../../../core/services/auth.service';
import {FormsModule} from '@angular/forms';

import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {SoundService} from '../../../../core/services/sound.service';
import {NotificationService} from '../../../../core/services/notification.service';

@Component({
    selector: 'app-register',
    imports: [
        FormsModule,
        TranslateModule
    ],
    templateUrl: './register.component.html',
    styleUrls: ['../../auth.component.scss']
})

export class RegisterComponent implements AfterViewInit {
    nickname = '';
    login = '';
    password = '';

    constructor(private notificationService: NotificationService, private authService: AuthService, private translate: TranslateService, private soundService: SoundService) {
        const lang = localStorage.getItem('language') || 'en';
        translate.use(lang);
    }

    checkForm() {
        const validationRules = [
            {field: this.nickname, key: 'ERRORS.EMPTY_NICKNAME'},
            {field: this.login, key: 'ERRORS.EMPTY_LOGIN'},
            {
                field: this.password,
                key: 'ERRORS.WEAK_PASSWORD',
                condition: (value: string) => value.length >= 8
            }
        ];

        for (let rule of validationRules) {
            if (rule.field === '' || (rule.condition && !rule.condition(rule.field))) {
                this.soundService.playSound('assets/mp3/error.mp3');
                this.translate.get(rule.key).subscribe(text => {
                    this.notificationService.error(text);
                });
                return false;
            }
        }

        return true;
    }


    onRegister() {
        if (!this.checkForm()) return;

        this.authService.register(this.nickname, this.login, this.password).subscribe({
            next: () => {
                localStorage.setItem('username', this.nickname);
                window.location.reload();
            },
            error: (err) => {
                this.soundService.playSound('assets/mp3/error.mp3');
                const errorCode = err.error?.errorCode;

                let translationKey = 'ERRORS.REGISTER_FAILED';
                if (errorCode === 1) translationKey = 'ERRORS.REGISTER.CHOSEN_NICKNAME';
                else if (errorCode === 2) translationKey = 'ERRORS.REGISTER.CHOSEN_LOGIN';

                this.translate.get(translationKey).subscribe(text => {
                    this.notificationService.error(text);
                });
            }
        });
    }

    ngAfterViewInit() {
        setTimeout(() => {
            const container = document.querySelector('.auth-container');
            if (container) {
                const welcomeMessage = container.querySelector('.welcome-message');
                const formContainer = container.querySelector('.form-container');
                if (welcomeMessage) welcomeMessage.classList.add('show');
                if (formContainer) formContainer.classList.add('show');
            }
        }, 50);
    }

    onRedirect() {
        this.authService.onRedirect('/auth/login');
    }
}
