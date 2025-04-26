import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class SoundService {
    playSound(path: string) {
        const soundSetting = localStorage.getItem('sound');

        if (soundSetting === 'on') {
            return;
        }

        const audio = new Audio(path);
        audio.volume = 0.5;
        audio.src = path;
        audio.load();
        audio.play();
    }
}
