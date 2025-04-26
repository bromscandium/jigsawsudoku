import {Component} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {TranslateModule, TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-help-widget',
    templateUrl: './help-widget.component.html',
    imports: [
        NgOptimizedImage,
        TranslateModule
    ],
    styleUrls: ['./help-widget.component.scss']
})
export class HelpWidgetComponent {
    constructor(private translate: TranslateService) {
        const lang = localStorage.getItem('language') || 'en';
        translate.use(lang);
    }
}
