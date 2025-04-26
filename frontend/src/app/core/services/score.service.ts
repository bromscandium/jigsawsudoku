import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Score} from '../models/score.model';
import {environment} from '../../../environment/environment';

@Injectable({providedIn: 'root'})
export class ScoreService {
    private readonly baseUrl = `${environment.apiUrl}/score`;

    constructor(private http: HttpClient) {
    }

    getAll(): Observable<Score[]> {
        return this.http.get<Score[]>(`${this.baseUrl}/jigsawsudoku`, {withCredentials: true});
    }

    getByPlayer(nickname: string): Observable<Score[]> {
        return this.http.get<Score[]>(`${this.baseUrl}/player/${nickname}`, {withCredentials: true});
    }

    update(score: Score): Observable<any> {
        return this.http.put(`${this.baseUrl}/score/update`, score, { withCredentials: true });
    }
}
