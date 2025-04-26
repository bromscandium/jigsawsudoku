import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {TranslateModule, TranslatePipe, TranslateService} from '@ngx-translate/core';

import {ReviewRequest} from '../../core/models/review-request.model';
import {CommentService} from '../../core/services/comment.service';
import {RatingService} from '../../core/services/rating.service';
import {NotificationService} from '../../core/services/notification.service';
import {SoundService} from '../../core/services/sound.service';

import {BackButtonComponent} from '../../shared/back-button/back-button.component';
import {forkJoin} from 'rxjs';

@Component({
    selector: 'app-reviews',
    standalone: true,
    templateUrl: './reviews.component.html',
    styleUrls: ['./reviews.component.scss'],
    imports: [
        TranslatePipe,
        TranslateModule,
        BackButtonComponent,
        FormsModule,
        NgForOf,
        DatePipe,
        NgIf
    ]
})
export class ReviewsComponent implements OnInit {
    nickname = localStorage.getItem('username') || 'player';

    personalComment: string = '';
    personalRating = 0;
    personalDate?: Date;

    commentIdent?: number;
    ratingIdent?: number;

    allComments: ReviewRequest[] = [];
    editing = false;
    isFocused = false;

    page = 0;
    pageSize = 3;

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
        setTimeout(() => {
            document.querySelector('.reviews-container')?.classList.add('show');
        }, 100);
    }

    toggleEdit(): void {
        this.editing = !this.editing;
        this.soundService.playSound('assets/mp3/click.mp3');
    }

    deleteReview(commentIdent?: number, ratingIdent?: number): void {
        if (ratingIdent == null && commentIdent == null) {
            this.translate.get('REVIEWS.NOTHING_TO_DELETE').subscribe(text => {
                this.loadReviews();
                this.notification.error(text);
            });
        }

        const deleteObservables = [];

        if (commentIdent != null) {
            deleteObservables.push(this.commentService.delete(commentIdent));
        }

        if (ratingIdent != null) {
            deleteObservables.push(this.ratingService.delete(ratingIdent));
        }

        forkJoin(deleteObservables).subscribe({
            next: () => {
                if (commentIdent != null) {
                    this.personalComment = '';
                }
                if (ratingIdent != null) {
                    this.personalRating = 0;
                }

                this.loadReviews();
                this.translate.get('REVIEWS.REVIEW_DELETED').subscribe(text => {
                    this.notification.success(text);
                });
            },
            error: (err) => {
                console.error('Error deleting review:', err);
                this.notification.error('Error deleting review');
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

        let commentSaved = false;
        let ratingSaved = false;

        const checkSave = () => {
            if (commentSaved && ratingSaved) {
                this.translate.get('REVIEWS.REVIEW_UPDATED').subscribe(msg => {
                    this.notification.success(msg);
                });
                this.editing = false;
                this.loadReviews();
            }
        };

        this.commentService.update(commentPayload).subscribe({
            next: () => {
                commentSaved = true;
                checkSave();
            },
            error: () => this.showError('REVIEWS.UPDATE_COMMENT_FAILED')
        });

        if (this.personalRating > 0) {
            this.ratingService.update(ratingPayload).subscribe({
                next: () => {
                    ratingSaved = true;
                    checkSave();
                },
                error: () => this.showError('REVIEWS.UPDATE_RATING_FAILED')
            });
        } else {
            ratingSaved = true;
            checkSave();
        }
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

    loadReviews(): void {
        this.commentService.getAll().subscribe(comments => {
            this.ratingService.getAll().subscribe(ratings => {
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
                this.commentIdent = myComment?.ident;
                this.personalRating = myRating?.rating ?? 0;
                this.ratingIdent = myRating?.ident;
                this.personalDate = myRating?.ratedOn;
            });
        });
    }

    sort(type: 'best' | 'worst' | 'oldest' | 'newest'): void {
        const sortFns = {
            best: (a: ReviewRequest, b: ReviewRequest) => b.rating - a.rating,
            worst: (a: ReviewRequest, b: ReviewRequest) => a.rating - b.rating,
            oldest: (a: ReviewRequest, b: ReviewRequest) =>
                new Date(a.commentedOn).getTime() - new Date(b.commentedOn).getTime(),
            newest: (a: ReviewRequest, b: ReviewRequest) =>
                new Date(b.commentedOn).getTime() - new Date(a.commentedOn).getTime()
        };

        this.allComments.sort(sortFns[type]);
        this.soundService.playSound('assets/mp3/click.mp3');
        this.page = 0;
    }

    paginatedReviews(): ReviewRequest[] {
        const start = this.page * this.pageSize;
        return this.allComments.slice(start, start + this.pageSize);
    }

    nextPage(): void {
        if ((this.page + 1) * this.pageSize < this.allComments.length) {
            this.changePage(this.page + 1);
        }
    }

    prevPage(): void {
        if (this.page > 0) {
            this.changePage(this.page - 1);
        }
    }

    private changePage(newPage: number): void {
        this.soundService.playSound('assets/mp3/click.mp3');
        this.page = newPage;
    }
}
