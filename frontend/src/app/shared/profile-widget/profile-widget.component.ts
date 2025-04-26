import {Component} from '@angular/core';
import {NgIf, NgOptimizedImage} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import {AuthService} from '../../core/services/auth.service';
import {SoundService} from '../../core/services/sound.service';
import {NotificationService} from '../../core/services/notification.service';

@Component({
    selector: 'app-profile-widget',
    templateUrl: './profile-widget.component.html',
    imports: [NgOptimizedImage, NgIf, FormsModule, TranslatePipe],
    styleUrls: ['./profile-widget.component.scss']
})
export class ProfileWidgetComponent {
    editing = false;
    nickname = localStorage.getItem('user') || '';
    tempNickname = this.nickname;

    constructor(private authService: AuthService, private translate: TranslateService, private soundService: SoundService, private notificationService: NotificationService) {
        const lang = localStorage.getItem('language') || 'en';
        translate.use(lang);
    }

    enableEditing() {
        this.tempNickname = this.nickname;
        this.editing = true;
        this.soundService.playSound('assets/mp3/click.mp3');
        setTimeout(() => {
            const input = document.getElementById('nickname-input') as HTMLInputElement;
            input?.focus();
        });
    }

    handleKey(event: KeyboardEvent) {
        if (event.key === 'Enter') {
            this.saveNickname();
        }
    }

    saveNickname() {
        this.nickname = this.tempNickname.trim();

        if (this.nickname.length > 0) {
            this.authService.update(this.nickname).subscribe({
                next: () => {
                    this.translate.get('NOTIFICATIONS.NICKNAME_UPDATED').subscribe(text => {
                        this.notificationService.success(text);
                    });
                    this.authService.updateNicknameLocally(this.nickname);
                },
                error: (err) => {
                    console.error('Nickname update failed:', err);
                    this.translate.get('ERRORS.NICKNAME_UPDATE_FAILED').subscribe(text => {
                        this.notificationService.error(text);
                    });
                }
            });
            this.editing = false;
        } else {
            this.translate.get('ERRORS.EMPTY_NICKNAME').subscribe(text => {
                this.notificationService.error(text);
            });
        }
    }
}
