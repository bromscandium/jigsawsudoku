import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Comment} from '../models/comment.model';
import {environment} from '../../../environment/environment';

@Injectable({providedIn: 'root'})
export class CommentService {
    private readonly baseUrl = `${environment.apiUrl}/comment`;

    constructor(private http: HttpClient) {
    }

    getAll(): Observable<Comment[]> {
        return this.http.get<Comment[]>(`${this.baseUrl}/all`, {withCredentials: true});
    }

    update(comment: Comment): Observable<any> {
        return this.http.put(`${this.baseUrl}/comment/update`, comment, {withCredentials: true});
    }

    delete(commentId: number): Observable<any> {
        return this.http.delete(`${this.baseUrl}/comment/delete/${commentId}`, { withCredentials: true });
    }
}
