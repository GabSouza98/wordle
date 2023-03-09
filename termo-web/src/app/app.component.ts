import { HttpErrorResponse } from '@angular/common/http';
import { Component, HostListener } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { catchError, throwError } from 'rxjs';
import { WordValidations , PossibleWords, Letter } from './models/wordValidations';
import { UserService } from './services/user.service';
import { WordService } from './services/word.service';
import { UserWordsService } from './services/userwords.service';
import { Histogram, UserGameInfo, UserWords } from './models/users';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap'
import { ModalComponent } from './components/modal/modal.component';

export enum Key {
  ArrowLeft = 'ArrowLeft',
  ArrowRight = 'ArrowRight',
  Backspace = 'Backspace',
  Enter = 'Enter'
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [WordService]
})

export class AppComponent {
  title = 'termo-web';
  currentRow = 0;
  success = false;
  correctWord = '';
  uuid : any;

  constructor(private wordService: WordService, 
              private userService: UserService, 
              private userWordService: UserWordsService,
              private notifierService: NotifierService
              ,private modalService: NgbModal
              ) { }

  ngOnInit() {

    this.wordService.getRandomWord()
      .pipe(catchError((error: HttpErrorResponse) => {
        console.log(error);
        this.notifierService.notify('info', 'Não foi possível buscar uma palavra');
        return throwError(() => null)
      }))
      .subscribe(p => {
        var randomWord = p.word;
        this.correctWord = randomWord;        
      })

    if(localStorage.getItem("uuid") === null) {            
      this.userService.getUuid()
      .subscribe(p => {
        var uuid = p.uuid;
        localStorage.setItem("uuid", uuid);
        this.uuid = uuid;
      })                
    } else {
      this.uuid = localStorage.getItem("uuid");
    }

  }

  onClick(event: Event) {

    var div = event.target as HTMLElement;

    //faz com que a div com propriedade mude no clique, mas somente se o clique for na linha ativa
    if (!this.success) {
      if (div.parentElement?.getAttribute("row") == this.currentRow.toString()) {
        div.parentElement?.querySelector(".edit")?.classList.remove("edit");
        div?.classList.add("edit");
      }
    }
  }

  onKeyClick(event: Event) {

    if (!this.success) {
      var div = event.target as HTMLElement;
      var letter = div.getAttribute('keyboard-key') ?? '';

      if (/[a-zA-Z]/.test(letter) && letter.length == 1) {
        this.setCurrentPositionValue(letter);
        this.moveRight();
      } else if (letter == Key.ArrowRight) {
        this.moveRight();
      } else if (letter == Key.ArrowLeft) {
        this.moveLeft();
      } else if (letter == Key.Backspace) {
        if (document.querySelector('.edit')?.innerHTML == '') {
          this.moveLeft();
        }
        this.setCurrentPositionValue();
      } else if (letter == Key.Enter) {
        this.sendWord();
      }
    }
  }

  @HostListener('window:keyup', ['$event'])
  onKeyPress(event: KeyboardEvent) {
    if (!this.success) {

      var key = event.key;

      if (/[a-zA-Z]/.test(key) && key.length == 1) {
        this.setCurrentPositionValue(key);
        this.moveRight();
      } else if (key == Key.ArrowRight) {
        this.moveRight();
      } else if (key == Key.ArrowLeft) {
        this.moveLeft();
      } else if (key == Key.Backspace) {
        if (document.querySelector('.edit')?.innerHTML == '') {
          this.moveLeft();
        }
        this.setCurrentPositionValue();
      } else if (key == Key.Enter) {
        this.sendWord();
      }

    }
  }

  setCurrentPositionValue(value: string = '') {
    document.querySelector('.edit')?.replaceChildren(value);
  }

  moveRight() {
    var index = Number(document.querySelector('.edit')?.getAttribute('pos'));
    if (index < 4) {
      document.querySelector('.edit')?.parentElement?.children.item(index + 1)?.classList.add('edit');
      document.querySelector('.edit')?.classList.remove('edit');
    }
  }

  moveLeft() {
    var index = Number(document.querySelector('.edit')?.getAttribute('pos'));
    if (index > 0) {
      document.querySelector('.edit')?.parentElement?.children.item(index - 1)?.classList.add('edit');
      document.querySelectorAll('.edit')[1].classList.remove('edit');
    }
  }

  sendWord() {
    var word = this.getWord();

    if (word.length < 5) {
      this.notifierService.notify('info', 'Só palavras com 5 letras');
    } else {
      this.getValidations(word.toLocaleLowerCase());
    }
  }

  getWord() {
    var word = '';
    const row = document.querySelector(`[row="${this.currentRow}"]`);

    if (row != undefined) {
      for (let index = 0; index < row?.children.length; index++) {
        const letter = row?.children.item(index)?.innerHTML;
        if (letter != undefined) {
          word = word + letter;
        }
      }
    }
    return word;
  }

  getValidations(word: string) {

    this.wordService.getValidations(word, this.correctWord)
      .pipe(catchError((error: HttpErrorResponse) => {
        this.notifierService.notify('info', 'Palavra não aceita');
        return throwError(() => null)
      }))
      .subscribe(response => {

        if(response.status === 204) {
          this.notifierService.notify('info', 'Palavra não existe!');
          return;
        }

        var validations = response.body!;        
        const row = document.querySelector(`[row="${this.currentRow}"]`);

        for (let index = 0; index < validations.letters.length; index++) {
          var validationLetter = validations.letters[index];

          if (validationLetter.exists) {
            if (validationLetter.rightPlace) {
              row?.querySelector(`[pos="${index}"]`)?.classList.add('right');
            } else {
              row?.querySelector(`[pos="${index}"]`)?.classList.add('place');
            }
          } else {
            row?.querySelector(`[pos="${index}"]`)?.classList.add('wrong');
          }

          setTimeout(() => {
            row?.querySelector(`[pos="${index}"]`)?.classList.remove('edit');
            row?.querySelector(`[pos="${index}"]`)?.classList.remove('active');
          }, 2000);

        }

        setTimeout(() => {
          if (validations.success) {
            this.success = true;
            this.notifierService.notify('info', 'Sucesso!');

            console.log("GANHOU");

            var userWords : UserWords = {
              uuid: this.uuid,
              word: this.correctWord,
              attempts: this.currentRow + 1
            }      

            //salvar jogo
            this.userWordService.saveGame(userWords)
            .pipe(catchError((error: HttpErrorResponse) => {
              console.log(error);
              this.notifierService.notify('info', 'Não foi possível salvar este jogo');
              return throwError(() => null)
            }))
            .subscribe(p => {
              console.log("requisicao OK");
              this.getUserStatistics();
            })                                

          } else {
            this.enableNextRow();
          }
          this.setKeyboardColors(validations);
        }, 1800);

      })

  }

  enableNextRow() {
    this.currentRow++;

    const row = document.querySelector(`[row="${this.currentRow}"]`);
    if (row != undefined) {
      for (let index = 0; index < row?.children.length; index++) {
        const letter = row?.children.item(index);
        letter?.classList.add('active');
        if (index == 0) {
          letter?.classList.add('edit');
        }
      }
    } else {
      console.log("perdeu");     

      var userWords : UserWords = {
        uuid: this.uuid,
        word: this.correctWord,
        attempts: this.currentRow + 1
      }

      this.userWordService.saveGame(userWords)
      .pipe(catchError((error: HttpErrorResponse) => {
        console.log(error);
        this.notifierService.notify('info', 'Não foi possível salvar este jogo');
        return throwError(() => null)
      }))
      .subscribe(p => {
        console.log("requisicao OK");
      })

      //pegar dados para a modal
      this.getUserStatistics();
    }
  }

  setKeyboardColors(validations: WordValidations) {
    for (let index = 0; index < validations.letters.length; index++) {
      const validationLetter = validations.letters[index];
      var element = document.querySelector(`[keyboard-key="${validationLetter.letter?.toUpperCase()}"]`)

      if (validationLetter.exists) {
        if (validationLetter.rightPlace) {
          element?.classList.remove('place');
          element?.classList.add('right');
        } else {
          element?.classList.add('place');
        }
      } else {
        element?.classList.add('keyboard-wrong');
      }
    }
  }


  onButtonTipClick(event: Event) {       

    var existingLetters : Letter[] = [];
    var rightLetters : Letter[] = [];
    var absentLetters : Letter[] = [];

    if (this.currentRow > 0) {

      for (let i = 0; i < this.currentRow; i++) {

        const row = document.querySelector(`[row="${i}"]`);

        if (row != undefined) {

          for (let j = 0; j < row?.children.length; j++) {

            const letter = row?.children.item(j);
          
            if(letter?.classList.contains("right")) {
              var rightLetter : Letter = {
                letter: letter.innerHTML,
                position: j
              }
              rightLetters.push(rightLetter);
            }

            if(letter?.classList.contains("place")) {
              var existingLetter : Letter = {
                letter: letter.innerHTML,
                position: j
              }
              existingLetters.push(existingLetter);
            }

            if(letter?.classList.contains("wrong")) {
              var wrongLetter : Letter = {
                letter: letter.innerHTML
              }
              absentLetters.push(wrongLetter);
            }          
          }
        }

      }

      var possibleWords : PossibleWords = {
        wordSize: 5,
        existingLetters: existingLetters,
        rightLetters: rightLetters,
        absentLetters: absentLetters,
      }

      this.wordService.getPossibleWords(possibleWords)
      .pipe(catchError((error: HttpErrorResponse) => {
        this.notifierService.notify('info', 'Nao foi possível obter dicas');
        return throwError(() => null)
      }))
      .subscribe(p => {
        var tips = p.possibleWords;          

        //pega a primeira palavra
        var concatedString = tips[0];
        //remove a primeira palavra
        tips.shift();
        //itera as demais palavras, adicionando a virgula
        tips.forEach(x => concatedString = concatedString + ", "+ x);

        var tipsDiv = document.getElementById("tips");
        if(tipsDiv != undefined) {
          tipsDiv.innerHTML = concatedString;
        }        
      })

    }    
  }

  getUserStatistics() {
    this.userWordService.getUserGameInfo(this.uuid)
    .subscribe( userGameInfo => 
      this.openModal(userGameInfo, this.success, this.currentRow)
    )
  }

  openModal(userInfo: UserGameInfo, success: boolean, currentRow: number) {    
    const modalRef = this.modalService.open(ModalComponent, { centered: true });
    modalRef.componentInstance.userInfo = userInfo;
    modalRef.componentInstance.success = success;
    modalRef.componentInstance.currentRow = currentRow;

    modalRef.result.then(
      (data: string) => {
        if(data == "newgame") {
          this.newGame();
        }        
      }
    )

  }

  checkButton() {
    return this.currentRow < 4
  }

  newGame() {    
    this.ngOnInit();
    this.currentRow = 0;
    this.success = false;

    //apagar dicas quando reiniciar
    var tipsDiv = document.getElementById("tips");    
    tipsDiv!.innerHTML = '';
    
    //lista de classes para remover das linhas e do teclado
    const cls = ["place", "wrong", "right", "edit", "active", "keyboard-wrong"]; 

    //reseta as linhas
    for (let i = 0; i < 6; i++) {
      const row = document.querySelector(`[row="${i}"]`);

      if (row != undefined) {
        if(i==0) {
          //logic for first row
          for (let j = 0; j < row?.children.length; j++) {
            const letter = row?.children.item(j);                   
            letter?.classList.remove(...cls);               
            letter?.replaceChildren('');        
            letter?.classList.add("active");      
            if(j==0) {
              letter?.classList.add("edit"); 
            }                    
          }
        } else {
          //logic for other rows
          for (let j = 0; j < row?.children.length; j++) {
            const letter = row?.children.item(j);                   
            letter?.classList.remove(...cls);               
            letter?.replaceChildren('');                                  
          }
        }        
      }
    }

    //reseta o teclado
    const keyboardKey = document.querySelectorAll("div[keyboard-key]");
    if(keyboardKey != undefined) {
      keyboardKey.forEach(key => {
        key?.classList.remove(...cls);
      })
    }


  }

}
