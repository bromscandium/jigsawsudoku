import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

@Component({
    selector: 'app-game',
    templateUrl: 'game.component.html',
    imports: [
        RouterOutlet,
    ]
})
export class GameComponent {
}
