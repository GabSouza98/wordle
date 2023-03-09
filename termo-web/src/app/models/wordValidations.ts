export interface WordValidations {
    letters: Letter[],
    success: boolean

}

export interface Letter {
    letter?: string,
    exists?: boolean,
    rightPlace?: boolean,
    position?: number
}

export interface PossibleWords {
    wordSize: number,
    existingLetters: Letter[],
    rightLetters: Letter[],
    absentLetters: Letter[],
}

export interface PossibleWordsResult {
    possibleWords: string[]
}

export interface Word {
    word: string;
}