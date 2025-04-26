import {Component, OnInit, OnDestroy} from '@angular/core';
import {NgForOf, NgIf, NgStyle} from '@angular/common';
import {Router} from '@angular/router';

import {Subscription, switchMap, interval, of} from 'rxjs';

import {SaveService} from '../../../../core/services/save.service';
import {GameService} from '../../../../core/services/game.service';
import {ScoreService} from '../../../../core/services/score.service';
import {NotificationService} from '../../../../core/services/notification.service';
import {SoundService} from '../../../../core/services/sound.service';

import {Board} from '../../../../core/models/board.model';
import {SaveRequest} from '../../../../core/models/save-request.model';

import {BackButtonComponent} from '../../../../shared/back-button/back-button.component';
import {TranslateModule, TranslatePipe, TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-game-board',
    standalone: true,
    templateUrl: './game-board.component.html',
    styleUrls: ['./game-board.component.scss'],
    imports: [NgStyle, NgIf, NgForOf, BackButtonComponent, TranslatePipe, TranslateModule]
})
export class GameBoardComponent implements OnInit, OnDestroy {
    nickname = localStorage.getItem('username') || 'player';
    state = undefined;
    score = undefined;

    board?: Board;
    difficulty = 'Easy';
    size = 0;

    timeElapsed = 0;
    attemptsLeft = 3;

    private timerSub?: Subscription;

    constructor(
        private saveService: SaveService,
        private gameService: GameService,
        private scoreService: ScoreService,
        private notificationService: NotificationService,
        private soundService: SoundService,
        private translateService: TranslateService,
        private router: Router
    ) {
    }

    ngOnInit(): void {
        this.saveService.loadGame(this.nickname).subscribe(save => {
            this.board = JSON.parse(save.boardJson) as Board;
            this.timeElapsed = save.timeSeconds;
            this.difficulty = save.difficulty;
            this.size = save.size;
            this.saveProgress();

            this.timerSub = interval(1000).subscribe(() => {
                this.timeElapsed++;
            });

            setTimeout(() => {
                const container = document.querySelector('.game-board-container');
                if (container) {
                    container.classList.add('show');
                }
            }, 50);
        });
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

    ngOnDestroy(): void {
        this.timerSub?.unsubscribe();
    }

    getColor(region: number): string {
        const hue = (region * 360) / (this.board?.size || 1);

        const isDarkTheme = localStorage.getItem('theme') === 'dark';

        const lightness = isDarkTheme ? 45 : 80;

        return `hsl(${hue}, 60%, ${lightness}%)`;
    }

    validateInput(event: any): void {
        const inputValue = event.target.value;
        const maxValue = this.board?.size || 9;

        const regex = new RegExp(`^[1-${maxValue}]$`);

        if (!regex.test(inputValue)) {
            event.target.value = '';
            return;
        }
    }

    onValueChange(event: any, i: number, j: number): void {
        const v = parseInt(event.target.value, 10);
        this.saveProgress();
        this.board!.cells[i][j].value = isNaN(v) ? 0 : v;
        if (this.isBoardFilled()) {
            this.checkBoard();
        }
    }

    isBoardFilled(): boolean {
        return this.board!.cells.every(row =>
            row.every(cell => cell.fixed || cell.value > 0)
        );
    }

    private checkBoard(): void {
        this.gameService.validateSudoku(this.board!).subscribe(isValid => {
            if (isValid) {
                this.handleWin();
            } else {
                this.attemptsLeft--;
                this.saveProgress();
                if (this.attemptsLeft <= 0) {
                    const size = this.size;
                    const multiplier =
                        this.difficulty.toLowerCase() === 'easy' ? 1 :
                            this.difficulty.toLowerCase() === 'medium' ? 2 : 3;
                    const timeFactor = 3600 - Math.min(this.timeElapsed, 3599);
                    const scoreVal = size * size * multiplier * timeFactor * this.attemptsLeft;
                    localStorage.setItem('score', String(scoreVal))
                    localStorage.setItem('state', 'RESULT.LOOSE')
                    this.soundService.playSound('assets/mp3/loose.mp3');
                    this.router.navigate(['/game/result']);
                }
                if (this.attemptsLeft > 0) {
                    this.translateService.get('GAME.MISTAKE').subscribe(text => {
                        this.notificationService.error(text);
                    });
                }
            }
        });
    }

    private handleWin(): void {
        const size = this.size;
        const multiplier =
            this.difficulty.toLowerCase() === 'easy' ? 1 :
                this.difficulty.toLowerCase() === 'medium' ? 2 : 3;
        const timeFactor = 3600 - Math.min(this.timeElapsed, 3599);
        const scoreVal = size * size * multiplier * timeFactor * this.attemptsLeft;
        localStorage.setItem('score', String(scoreVal))
        localStorage.setItem('state', 'RESULT.WIN')

        this.scoreService
            .getByPlayer(this.nickname)
            .pipe(
                switchMap(scores => {
                    const safeScores = scores ?? [];
                    const existing = safeScores.find(
                        s => s.game === 'jigsawsudoku' && s.player === this.nickname
                    );
                    if (!existing || scoreVal > existing.points) {
                        return this.scoreService.update({
                            ident: existing?.ident,
                            game: 'jigsawsudoku',
                            player: this.nickname,
                            points: scoreVal,
                            playedOn: new Date()
                        });
                    }
                    return of(null);
                })
            )
            .subscribe(() => {
                this.saveProgress();
                this.soundService.playSound('assets/mp3/win.mp3');
                this.router.navigate(['/game/result']);
            });
    }

    private saveProgress(): void {
        if (!this.board) return;
        const req: SaveRequest = {
            login: this.nickname,
            boardJson: JSON.stringify(this.board),
            attempts: this.attemptsLeft,
            timeSeconds: this.timeElapsed,
            difficulty: this.difficulty,
            size: this.size
        };
        this.saveService.storeGame(req).subscribe()
    }
}
