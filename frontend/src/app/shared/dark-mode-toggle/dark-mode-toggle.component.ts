import {Component, OnInit} from '@angular/core';
import {NgIf, NgOptimizedImage} from '@angular/common';
import {SoundService} from '../../core/services/sound.service';

@Component({
    selector: 'app-dark-mode-toggle',
    standalone: true,
    templateUrl: './dark-mode-toggle.component.html',
    imports: [
        NgIf,
        NgOptimizedImage
    ],
    styleUrls: ['../styles/toggle-button.scss', './dark-mode-toggle.component.scss'],
})
export class DarkModeToggleComponent implements OnInit {
    isDarkMode = false;

    constructor(private soundService: SoundService) {
    }

    ngOnInit(): void {
        const storedMode = localStorage.getItem('theme');

        if (storedMode === 'dark' || (!storedMode && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
            this.isDarkMode = true;
        }

        this.applyTheme();
    }

    toggleDarkMode(): void {
        this.isDarkMode = !this.isDarkMode;
        localStorage.setItem('theme', this.isDarkMode ? 'dark' : 'light');
        this.soundService.playSound('assets/mp3/click.mp3');
        this.applyTheme();
    }

    applyTheme() {
        const html = document.documentElement;

        if (this.isDarkMode) {
            html.classList.add('dark-mode');
        } else {
            html.classList.remove('dark-mode');
        }
    }
}
