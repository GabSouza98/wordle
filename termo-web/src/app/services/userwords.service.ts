import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserGameInfo, UserWords } from '../models/users';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class UserWordsService {

  apiUrl = environment.baseUrl + '/v1/user-words';

  constructor(private http: HttpClient) { }

  saveGame(userWords: UserWords): Observable<void> {
    return this.http.post<void>(this.apiUrl, userWords);
  }  

  getUserGameInfo(uuid : string): Observable<UserGameInfo> {
    return this.http.get<UserGameInfo>(`${this.apiUrl}/info/${uuid}`)
  }

  

}




