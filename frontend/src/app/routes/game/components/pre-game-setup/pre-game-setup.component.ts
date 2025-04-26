import {Component, OnInit} from '@angular/core';
import {NgForOf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {switchMap} from 'rxjs/operators';

import {SaveRequest} from '../../../../core/models/save-request.model';
import {Board} from '../../../../core/models/board.model';

import {BackButtonComponent} from '../../../../shared/back-button/back-button.component';

import {SoundService} from '../../../../core/services/sound.service';
import {GameService} from '../../../../core/services/game.service';
import {SaveService} from '../../../../core/services/save.service';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
    selector: 'app-pre-game-setup',
    standalone: true,
    imports: [NgForOf, FormsModule, BackButtonComponent, TranslatePipe],
    templateUrl: './pre-game-setup.component.html',
    styleUrls: ['./pre-game-setup.component.scss']
})
export class PreGameSetupComponent implements OnInit {
    difficulties = ['Easy', 'Medium', 'Hard'] as const;
    sizes = [3, 4, 5, 6, 7, 8, 9];

    selectedDifficulty = '';
    selectedSize: number | null = null;

    constructor(
        private gameService: GameService,
        private saveService: SaveService,
        private soundService: SoundService,
        private router: Router
    ) {
    }

    ngOnInit() {
        setTimeout(() => {
            document.querySelector('.pre-game-setup-container')?.classList.add('show');
        }, 50)
    }

    startGame(): void {
        if (!this.selectedSize || this.selectedDifficulty == '') {
            return;
        }

        const difficultyParam = this.selectedDifficulty.toUpperCase() as 'EASY' | 'MEDIUM' | 'HARD';
        const sizeParam = this.selectedSize;

        this.gameService.generateSudoku(sizeParam, difficultyParam)
            .pipe(
                switchMap((board: Board) => {
                    const saveReq: SaveRequest = {
                        login: localStorage.getItem('username') || 'player',
                        boardJson: JSON.stringify(board),
                        attempts: 0,
                        timeSeconds: 0,
                        difficulty: this.selectedDifficulty,
                        size: sizeParam
                    };
                    return this.saveService.storeGame(saveReq);
                })
            )
            .subscribe({
                next: () => {
                    this.soundService.playSound('assets/mp3/start.mp3');
                    this.router.navigate(['/game'], {
                        queryParams: {
                            difficulty: difficultyParam,
                            size: sizeParam
                        }
                    });
                },
            });
    }
}
