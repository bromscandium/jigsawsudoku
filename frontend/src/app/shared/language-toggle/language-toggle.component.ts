import {Component} from '@angular/core';
import {NgIf, NgOptimizedImage} from '@angular/common';
import {TranslateService} from '@ngx-translate/core';
import {SoundService} from '../../core/services/sound.service';

@Component({
    selector: 'app-language-toggle',
    standalone: true,
    templateUrl: './language-toggle.component.html',
    imports: [
        NgIf,
        NgOptimizedImage
    ],
    styleUrls: ['../styles/toggle-button.scss', './language-toggle.component.scss']
})
export class LanguageToggleComponent {
    currentLang: 'en' | 'uk' = 'en';

    constructor(private translate: TranslateService, private soundService: SoundService) {
        const storedLang = localStorage.getItem('language') as 'en' | 'uk';
        if (storedLang) {
            this.currentLang = storedLang;
            this.translate.use(storedLang);
        } else {
            this.translate.use('en');
        }
    }

    toggleLanguage(): void {
        this.currentLang = this.currentLang === 'en' ? 'uk' : 'en';
        localStorage.setItem('language', this.currentLang);
        this.soundService.playSound('assets/mp3/click.mp3');
        this.translate.use(this.currentLang);
    }
}
