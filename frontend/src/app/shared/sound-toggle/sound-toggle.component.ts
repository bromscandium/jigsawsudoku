import {Component, OnInit} from '@angular/core';
import {NgIf, NgOptimizedImage} from '@angular/common';
import {SoundService} from '../../core/services/sound.service';

@Component({
    selector: 'app-sound-toggle',
    standalone: true,
    templateUrl: './sound-toggle.component.html',
    imports: [
        NgIf,
        NgOptimizedImage
    ],
    styleUrls: ['../styles/toggle-button.scss', './sound-toggle.component.scss']
})
export class SoundToggleComponent implements OnInit {
    isSoundOff = false;

    constructor(private soundService: SoundService) {
    }

    ngOnInit(): void {
        const storedSound = localStorage.getItem('sound');
        this.isSoundOff = storedSound !== 'off';
    }

    toggleSound(): void {
        this.isSoundOff = !this.isSoundOff;
        localStorage.setItem('sound', this.isSoundOff ? 'on' : 'off');
        this.soundService.playSound('assets/mp3/click.mp3');
    }
}
