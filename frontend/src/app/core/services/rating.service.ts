import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Rating} from '../models/rating.model';
import {environment} from '../../../environment/environment';

@Injectable({providedIn: 'root'})
export class RatingService {
    private readonly baseUrl = `${environment.apiUrl}/rating`;

    constructor(private http: HttpClient) {
    }

    getAll(): Observable<Rating[]> {
        return this.http.get<Rating[]>(`${this.baseUrl}/all`, {withCredentials: true});
    }

    update(rating: Rating): Observable<any> {
        return this.http.put(`${this.baseUrl}/rating/update`, rating, {withCredentials: true});
    }

    getAverage(game: string): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/average/${game}`, {withCredentials: true});
    }

    delete(ratingId: number): Observable<any> {
        return this.http.delete(`${this.baseUrl}/rating/delete/${ratingId}`, { withCredentials: true });
    }
}
