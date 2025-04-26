import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';

import {GameComponent} from './game.component';
import {GameBoardComponent} from './components/game-board/game-board.component';
import {PostGameResultComponent} from './components/post-game-result/post-game-result.component';
import {PreGameSetupComponent} from './components/pre-game-setup/pre-game-setup.component';

const routes: Routes = [
    {path: '', component: GameBoardComponent},
    {path: 'setup', component: PreGameSetupComponent},
    {path: 'result', component: PostGameResultComponent}
];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        GameComponent
    ],
    exports: [GameComponent]
})
export class GameModule {
}
