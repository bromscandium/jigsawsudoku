import {Component, OnInit} from '@angular/core';
import {NotificationData, NotificationService} from '../../core/services/notification.service';
import {NgClass, NgIf} from '@angular/common';


@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.scss'],
    imports: [
        NgClass,
        NgIf
    ],
    standalone: true
})
export class NotificationComponent implements OnInit {
    message = '';
    type: 'success' | 'error' = 'success';
    visible = false;
    progress = 100;
    private intervalId: any;
    private duration = 5000;

    constructor(private notificationService: NotificationService) {
    }

    ngOnInit(): void {
        this.notificationService.notification$.subscribe((data: NotificationData) => {
            if (this.visible) {
                this.resetAndShow(data.message, data.type);
            } else {
                this.show(data.message, data.type);
            }
        });
    }

    private show(message: string, type: 'success' | 'error') {
        this.message = message;
        this.type = type;
        this.visible = true;
        this.progress = 100;

        const step = 100 / (this.duration / 50);
        clearInterval(this.intervalId);
        this.intervalId = setInterval(() => {
            this.progress -= step;
            if (this.progress <= 0) {
                clearInterval(this.intervalId);
                setTimeout(() => this.hide(), 200);
            }
        }, 50);
    }

    private hide() {
        clearInterval(this.intervalId);
        this.visible = false;
    }

    private resetAndShow(message: string, type: 'success' | 'error') {
        this.hide();
        setTimeout(() => this.show(message, type), 50);
    }
}
