import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateModule} from '@ngx-translate/core';
import {SoundService} from '../../core/services/sound.service';
import {SaveService} from '../../core/services/save.service';

@Component({
    selector: 'app-back-button',
    standalone: true,
    templateUrl: './back-button.component.html',
    styleUrls: ['./back-button.component.scss'],
    imports: [
        TranslateModule,
    ]
})
export class BackButtonComponent implements OnInit {
    nickname = '';

    constructor(private router: Router, private soundService: SoundService, private saveService: SaveService) {
    }

    ngOnInit(): void {
        setTimeout(() => {
            const container = document.querySelector('.back-button');
            if (container) {
                container.classList.add('show');
            }
        }, 50);
    }

    exit(): void {
        ['.back-button', '.reviews-container', '.score-table-container', '.post-game-result-container'].forEach(selector => {
            const element = document.querySelector(selector);
            if (element) {
                element.classList.remove('show');
            }
        });
        this.soundService.playSound('assets/mp3/click.mp3');
        this.router.navigate(['/']);
    }
}
