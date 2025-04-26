import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {BehaviorSubject, Observable} from 'rxjs';
import {SoundService} from './sound.service';
import {environment} from '../../../environment/environment';

@Injectable({providedIn: 'root'})
export class AuthService {
    private readonly baseUrl = `${environment.apiUrl}/auth`;
    private nicknameSubject = new BehaviorSubject<string>(localStorage.getItem('username') || 'Player');
    nickname$ = this.nicknameSubject.asObservable();

    constructor(
        private http: HttpClient,
        private router: Router,
        private soundService: SoundService
    ) {
        const nickname = localStorage.getItem('username');
        if (nickname) {
            this.nicknameSubject.next(nickname);
        }
    }

    login(login: string, password: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/login`, {login, password}, {withCredentials: true});
    }

    register(nickname: string, login: string, password: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/register`, {nickname, login, password}, {withCredentials: true});
    }

    update(nickname: string): Observable<any> {
        const oldNickname = localStorage.getItem('username');
        return this.http.put(`${this.baseUrl}/update?oldNickname=${oldNickname}&newNickname=${nickname}`, {}, {
            withCredentials: true
        });
    }

    checkSession(): Observable<any> {
        return this.http.get(`${this.baseUrl}/check`, {withCredentials: true});
    }

    logout(): Observable<any> {
        return this.http.post(`${this.baseUrl}/logout`, {}, {withCredentials: true});
    }

    onRedirect(path: string): void {
        this.soundService.playSound('assets/mp3/click.mp3');

        const container = document.querySelector('.auth-container');
        container?.querySelector('.welcome-message')?.classList.remove('show');
        container?.querySelector('.form-container')?.classList.remove('show');

        setTimeout(() => this.router.navigate([path]), 400);
    }

    updateNicknameLocally(newNickname: string): void {
        localStorage.setItem('username', newNickname);
        this.nicknameSubject.next(newNickname);
    }
}
