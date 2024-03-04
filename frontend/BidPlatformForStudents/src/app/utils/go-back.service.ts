import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GoBackService {
  private backSubject = new Subject<string>();

  back$ = this.backSubject.asObservable();

  back(message: string): void {
    this.backSubject.next(message);
  }
}
