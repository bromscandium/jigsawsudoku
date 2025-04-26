import {Component} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {Router} from '@angular/router';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-game-label',
    templateUrl: './game-label.component.html',
    imports: [NgOptimizedImage, TranslatePipe],
    styleUrls: ['./game-label.component.scss'],
})
export class GameLabelComponent {
    translationKey: string = 'MENU.TITLE';

    constructor(private router: Router, private translate: TranslateService) {
        this.router.events.subscribe(() => {
            const url = this.router.url;
            if (url.includes('/score-table')) {
                this.translationKey = 'MENU.SCORE_TABLE';
            } else if (url.includes('/reviews')) {
                this.translationKey = 'MENU.REVIEWS';
            } else {
                this.translationKey = 'MENU.TITLE';
            }
        });
    }
}
