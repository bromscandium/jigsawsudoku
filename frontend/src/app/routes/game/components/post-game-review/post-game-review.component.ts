import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import {CommentService} from '../../../../core/services/comment.service';
import {RatingService} from '../../../../core/services/rating.service';
import {NotificationService} from '../../../../core/services/notification.service';
import {SoundService} from '../../../../core/services/sound.service';
import {ReviewRequest} from '../../../../core/models/review-request.model';
import {FormsModule} from '@angular/forms';
import {forkJoin} from 'rxjs';

@Component({
    selector: 'app-post-game-review',
    standalone: true,
    imports: [
        NgForOf,
        NgIf,
        TranslatePipe,
        FormsModule
    ],
    templateUrl: 'post-game-review.component.html',
    styleUrls: ['post-game-review.component.scss']
})
export class PostGameReviewComponent implements OnInit {
    nickname = localStorage.getItem('username') || 'player';
    personalComment = '';
    personalRating = 0;

    commentIdent?: number;
    ratingIdent?: number;

    isFocused = false;
    isShown = false;

    allComments: ReviewRequest[] = [];

    constructor(
        private commentService: CommentService,
        private ratingService: RatingService,
        private notification: NotificationService,
        private soundService: SoundService,
        private translate: TranslateService
    ) {
    }

    ngOnInit(): void {
        this.loadReviews();
    }

    loadReviews(): void {
        forkJoin({
            comments: this.commentService.getAll(),
            ratings: this.ratingService.getAll()
        }).subscribe(({comments, ratings}) => {
            const reviews = comments.map(comment => {
                const rating = ratings.find(r => r.player === comment.player);
                return {
                    player: comment.player,
                    comment: comment.comment,
                    commentedOn: comment.commentedOn,
                    rating: rating?.rating ?? 0
                };
            });

            this.allComments = reviews.filter(r => r.player !== this.nickname);

            const myComment = comments.find(c => c.player === this.nickname);
            const myRating = ratings.find(r => r.player === this.nickname);

            this.personalComment = myComment?.comment ?? '';
            this.personalRating = myRating?.rating ?? 0;
            this.commentIdent = myComment?.ident;
            this.ratingIdent = myRating?.ident;

            if (!this.personalComment || !this.personalRating) {
                this.isShown = true;
            }
        });
    }

    saveReview(): void {
        if (this.personalComment == '') {
            this.showError('REVIEWS.EMPTY_COMMENT')
            return;
        }
        const now = new Date();

        const commentPayload = {
            ident: this.commentIdent,
            game: 'jigsawsudoku',
            player: this.nickname,
            comment: this.personalComment,
            commentedOn: now
        };

        const ratingPayload = {
            ident: this.ratingIdent,
            game: 'jigsawsudoku',
            player: this.nickname,
            rating: this.personalRating,
            ratedOn: now
        };

        forkJoin([
            this.commentService.update(commentPayload),
            this.ratingService.update(ratingPayload)
        ]).subscribe({
            next: () => {
                this.translate.get('REVIEWS.REVIEW_UPDATED').subscribe(msg => {
                    this.notification.success(msg);
                });
            },
            error: () => {
                this.showError('REVIEWS.SAVE_FAILED');
            }
        });
    }

    onRatingChange(rating: number): void {
        this.soundService.playSound('assets/mp3/click.mp3');
        this.personalRating = rating;
    }

    showError(key: string): void {
        this.translate.get(key).subscribe(msg => {
            this.notification.error(msg);
        });
    }
}
