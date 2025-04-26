import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {SoundService} from './sound.service';

export interface NotificationData {
    message: string;
    type: 'success' | 'error';
}

@Injectable({providedIn: 'root'})
export class NotificationService {
    private notificationSubject = new Subject<NotificationData>();
    public notification$ = this.notificationSubject.asObservable();

    constructor(private soundService: SoundService) {
    }

    success(message: string) {
        this.soundService.playSound('assets/mp3/success.mp3');
        this.notify(message, 'success');
    }

    error(message: string) {
        this.soundService.playSound('assets/mp3/error.mp3');
        this.notify(message, 'error');
    }

    private notify(message: string, type: 'success' | 'error') {
        this.notificationSubject.next({message, type});
    }
}
