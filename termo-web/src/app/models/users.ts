export interface UuidDTO {
    uuid: string;
}

export interface UserWords {
    uuid?: string,
    word?: string,
    attempts?: number
}

export interface Histogram {
    attempts: number,
    sum: number
}

export interface UserGameInfo {
    totalGames: number,
    totalWins: number,
    percentWin: string,
    histogramList: Histogram[]
}