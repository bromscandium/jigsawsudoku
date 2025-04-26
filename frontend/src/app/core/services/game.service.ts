import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Board} from '../models/board.model';
import {environment} from '../../../environment/environment';

@Injectable({providedIn: 'root'})
export class GameService {
    private readonly baseUrl = `${environment.apiUrl}/game`;

    constructor(private http: HttpClient) {
    }

    validateSudoku(board: Board): Observable<boolean> {
        return this.http.post<boolean>(
            `${this.baseUrl}/validate`,
            board,
            {withCredentials: true}
        );
    }

    generateSudoku(size: number, difficulty: 'EASY' | 'MEDIUM' | 'HARD'): Observable<Board> {
        return this.http.get<Board>(
            `${this.baseUrl}/generate`,
            {
                params: {
                    size: size.toString(),
                    difficulty
                },
                withCredentials: true
            }
        );
    }
}
