import {Component, OnInit} from '@angular/core';
import {NgClass, NgIf} from '@angular/common';
import {Router} from '@angular/router';

import {PostGameReviewComponent} from '../post-game-review/post-game-review.component';
import {SaveService} from '../../../../core/services/save.service';

import {Save} from '../../../../core/models/save.model';
import {TranslatePipe} from '@ngx-translate/core';
import {SoundService} from '../../../../core/services/sound.service';

@Component({
    selector: 'app-post-game-result',
    standalone: true,
    imports: [
        NgIf,
        PostGameReviewComponent,
        TranslatePipe,
        NgClass
    ],
    templateUrl: './post-game-result.component.html',
    styleUrls: ['./post-game-result.component.scss']
})

export class PostGameResultComponent implements OnInit {
    nickname = localStorage.getItem('username') || 'player';
    score = localStorage.getItem('score') || '0';
    state = localStorage.getItem('state') || 'RESULT.LOOSE';
    save?: Save;

    constructor(
        private saveService: SaveService,
        private router: Router,
        private soundService: SoundService,
    ) {
    }

    formatTime(seconds: number): string {
        const h = Math.floor(seconds / 3600);
        const m = Math.floor((seconds % 3600) / 60);
        const s = seconds % 60;

        const hours = h > 0 ? `${h}:` : '';
        const minutes = m < 10 && h > 0 ? `0${m}` : `${m}`;
        const secs = s < 10 ? `0${s}` : `${s}`;

        return `${hours}${minutes}:${secs}`;
    }

    ngOnInit(): void {
        this.saveService.loadGame(this.nickname).subscribe({
            next: s => {
                this.save = s;

                setTimeout(() => {
                    const container = document.querySelector('.post-game-result-container');
                    if (container) {
                        container.classList.add('show');
                        console.log('Клас show додано!');
                    }
                }, 50);
            },
            error: () => this.router.navigate(['/'])
        });
    }

    newGame(): void {
        const nickname = localStorage.getItem('username') || 'player';
        this.soundService.playSound('assets/mp3/click.mp3');
        localStorage.removeItem('state');
        localStorage.removeItem('score');
        this.saveService.deleteSave(nickname).subscribe({
            next: () => this.router.navigate(['/game/setup']),
            error: () => this.router.navigate(['/'])
        });
    }

    goMenu(): void {
        const nickname = localStorage.getItem('username') || 'player';
        this.soundService.playSound('assets/mp3/click.mp3');
        localStorage.removeItem('state');
        localStorage.removeItem('score');
        this.saveService.deleteSave(nickname).subscribe({
            next: () => this.router.navigate(['/']),
            error: () => this.router.navigate(['/'])
        });
    }
}
