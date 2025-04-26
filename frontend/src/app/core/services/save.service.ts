import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environment/environment';
import { Observable } from 'rxjs';
import { Save } from '../models/save.model';
import {SaveRequest} from '../models/save-request.model';

@Injectable({ providedIn: 'root' })
export class SaveService {
    private readonly baseUrl = `${environment.apiUrl}/save`;

    constructor(private http: HttpClient) {}

    storeGame(saveRequest: SaveRequest): Observable<any> {
        return this.http.post(`${this.baseUrl}/store`, saveRequest, { withCredentials: true });
    }

    loadGame(nickname: string): Observable<Save> {
        return this.http.get<Save>(`${this.baseUrl}/load/${nickname}`, { withCredentials: true });
    }

    deleteSave(nickname: string): Observable<any> {
        return this.http.delete(`${this.baseUrl}/delete/${nickname}`, { withCredentials: true });
    }
}
