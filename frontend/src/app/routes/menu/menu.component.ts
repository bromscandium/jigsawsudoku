import {Component, OnInit, AfterViewInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateModule, TranslateService} from '@ngx-translate/core';

import {AuthService} from '../../core/services/auth.service';
import {SoundService} from '../../core/services/sound.service';
import {NotificationService} from '../../core/services/notification.service';

import {SaveService} from '../../core/services/save.service';
import {DecimalPipe, NgIf} from '@angular/common';
import {RatingService} from '../../core/services/rating.service';

@Component({
    selector: 'app-menu',
    standalone: true,
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.scss'],
    imports: [TranslateModule, NgIf, DecimalPipe]
})
export class MenuComponent implements OnInit {
    greeting = '';
    nickname = '';
    hasSavedGame :boolean = false;
    average: number | null = null;

    constructor(
        private router: Router,
        public authService: AuthService,
        private translate: TranslateService,
        private soundService: SoundService,
        private notificationService: NotificationService,
private ratingService: RatingService,
        private saveService: SaveService
    ) {
    }

    ngOnInit(): void {
        this.authService.nickname$.subscribe(nick => {
            this.nickname = nick;
            this.checkSave();
        });

        const hour = new Date().getHours();
        this.greeting =
            hour >= 5 && hour < 12 ? 'MORNING' :
                hour >= 12 && hour < 17 ? 'DAY' :
                    hour >= 17 && hour < 21 ? 'EVENING' :
                        'NIGHT';

        this.ratingService.getAll().subscribe(ratings => {
            if (ratings.length > 0) {
                const total = ratings.reduce((sum, r) => sum + r.rating, 0);
                this.average = total / ratings.length;
            } else {
                this.average = 0;
            }

            setTimeout(() => {
                const container = document.querySelector('.average-rating');
                if (container) {
                    document.querySelector('.menu-container')?.classList.add('show');
                    container.classList.add('show');
                }
            }, 50);
        });
    }

    navigateTo(path: string): void {
        document.querySelector('.average-rating')?.classList.remove('show');
        document.querySelector('.menu-container')?.classList.remove('show');
        this.soundService.playSound('assets/mp3/click.mp3');
        this.router.navigate([path]);
    }

    checkSave(): void {
        if (!this.nickname) return;

        this.saveService.loadGame(this.nickname).subscribe({
            next: () => {
                this.hasSavedGame = true;
            },
            error: () => {
                this.hasSavedGame = false;
            }
        });
    }

    play(): void {
        const nickname = this.nickname;

        this.saveService.loadGame(nickname).subscribe({
            next: () => {
                this.navigateTo('/game');
            },
            error: () => {
                this.navigateTo('/game/setup');
            }
        });
    }

    newGame(): void {
        this.navigateTo('/game/setup');
    }

    scoreTable(): void {
        this.navigateTo('/score-table');
    }

    reviews(): void {
        this.navigateTo('/reviews');
    }

    exit(): void {
        this.authService.logout().subscribe({
            next: () => {
                localStorage.removeItem('username');
                document.querySelector('.menu-container')?.classList.remove('show');
                document.querySelector('.average-rating')?.classList.remove('show');
                window.location.reload();
            },
            error: err => {
                console.error('Logout failed', err);
                this.soundService.playSound('assets/mp3/error.mp3');
                this.translate.get('ERRORS.LOGOUT_FAILED').subscribe(text =>
                    this.notificationService.error(text)
                );
            }
        });
    }
}
