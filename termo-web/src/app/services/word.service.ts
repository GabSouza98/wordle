import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WordValidations , Word, PossibleWords, PossibleWordsResult } from '../models/wordValidations';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class WordService {

  apiUrl = environment.baseUrl + '/v1/words';

  constructor(private http: HttpClient) { }

  getRandomWord(): Observable<Word> {
    return this.http.get<Word>(this.apiUrl);
  }

  getValidations(word: string, correctWord: string): Observable<HttpResponse<WordValidations>> {
    return this.http.get<WordValidations>(`${this.apiUrl}/validations?word=${word}&correctWord=${correctWord}`,{observe: 'response'});
  }

  getPossibleWords(possibleWords: PossibleWords): Observable<PossibleWordsResult> {
    return this.http.post<PossibleWordsResult>(`${this.apiUrl}/tips`, possibleWords);
  }  

}
