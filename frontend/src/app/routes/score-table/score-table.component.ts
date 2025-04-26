import {Component, AfterViewInit} from '@angular/core';
import {DatePipe, NgClass, NgForOf} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';

import {ScoreService} from '../../core/services/score.service';

import {Score} from '../../core/models/score.model';

import {BackButtonComponent} from '../../shared/back-button/back-button.component';

@Component({
    selector: 'app-score-table',
    standalone: true,
    templateUrl: './score-table.component.html',
    styleUrls: ['./score-table.component.scss'],
    imports: [
        NgForOf,
        NgClass,
        TranslateModule,
        DatePipe,
        BackButtonComponent
    ]
})
export class ScoreTableComponent implements AfterViewInit {
    nickname = '';
    personalScore: number = 0;
    personalPlayedOn?: Date;
    personalPosition: number | null = null;
    scores: Score[] = [];

    get personalRowClass(): string {
        switch (this.personalPosition) {
            case 1:
                return 'score-row personal gold';
            case 2:
                return 'score-row personal silver';
            case 3:
                return 'score-row personal bronze';
            default:
                return 'score-row personal';
        }
    }

    constructor(private scoreService: ScoreService) {
    }

    ngAfterViewInit(): void {
        this.nickname = localStorage.getItem('username') || 'player';

        this.loadPersonalScore();
        this.loadScoreTable();
    }

    private loadPersonalScore(): void {
        this.scoreService.getByPlayer(this.nickname).subscribe(personalScores => {
            const [latest] = personalScores;

            if (latest) {
                this.personalScore = latest.points;
                this.personalPlayedOn = latest.playedOn;
            } else {
                this.personalScore = 0;
                this.personalPlayedOn = undefined;
            }
        });
    }

    private loadScoreTable(): void {
        this.scoreService.getAll().subscribe(allScores => {
            const safeScores = allScores ?? [];
            this.scores = [...safeScores];

            const playerExists = this.scores.some(
                s => s.game === 'jigsawsudoku' && s.player === this.nickname
            );

            if (!playerExists) {
                this.personalPlayedOn = new Date(),
                this.scores.push({
                    player: this.nickname,
                    points: 0,
                    playedOn: this.personalPlayedOn,
                    game: 'jigsawsudoku'
                });
            }

            this.scores = this.scores.sort((a, b) => b.points - a.points);

            const index = this.scores.findIndex(s => s.player === this.nickname);
            this.personalPosition = index >= 0 ? index + 1 : null;

            setTimeout(() => {
                document.querySelector('.score-table-container')?.classList.add('show');
            }, 50);
        });
    }
}
