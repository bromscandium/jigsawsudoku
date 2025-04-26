import {Component} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {LanguageToggleComponent} from '../language-toggle/language-toggle.component';
import {DarkModeToggleComponent} from '../dark-mode-toggle/dark-mode-toggle.component';
import {SoundToggleComponent} from '../sound-toggle/sound-toggle.component';

@Component({
    selector: 'app-settings-widget',
    templateUrl: './settings-widget.component.html',
    imports: [
        NgOptimizedImage,
        LanguageToggleComponent,
        DarkModeToggleComponent,
        SoundToggleComponent
    ],
    styleUrls: ['./settings-widget.component.scss']
})
export class SettingsWidgetComponent {
}
