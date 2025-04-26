import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

@Component({
    selector: 'app-auth',
    templateUrl: './auth.component.html',
    imports: [
        RouterOutlet
    ],
    standalone: true,
    styleUrls: ['./auth.component.scss']
})
export class AuthComponent {
}
