package com.example.wordle.service;

import com.example.wordle.domain.Histogram;
import com.example.wordle.domain.UserGameInfo;
import com.example.wordle.domain.UserWordsDTO;
import com.example.wordle.repository.UserWordsRepository;
import com.example.wordle.repository.UsersRepository;
import com.example.wordle.repository.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Service
public class UserWordsService {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WordsRepository wordsRepository;

    @Autowired
    private UserWordsRepository userWordsRepository;


    public void save(UserWordsDTO userWordsDTO) {

        try {
            Long userId = usersRepository.getUserIdByUuid(userWordsDTO.getUuid());
            Long wordId = wordsRepository.getWordId(userWordsDTO.getWord());

            if(nonNull(userId) && nonNull(wordId) && nonNull(userWordsDTO.getAttempts())) {
                userWordsRepository.save(wordId, userId, userWordsDTO.getAttempts());
            }
        } catch (Exception e) {
            throw e;
        }

    }

    public UserGameInfo getUserGameInfo(String uuid) {

        try {
            Long userId = usersRepository.getUserIdByUuid(uuid);

            if(nonNull(userId)) {
                var totalGames = userWordsRepository.getTotalGames(userId);
                var totalWins = userWordsRepository.getTotalWins(userId);
                var percentWin = df.format(100 * (double) totalWins / (double) totalGames);
                List<Histogram> histogramList = userWordsRepository.getHistogram(userId);

                return new UserGameInfo(totalGames, totalWins, percentWin, histogramList);
            } else {
                return null;
            }

        } catch (Exception e) {
            throw e;
        }
    }
}
