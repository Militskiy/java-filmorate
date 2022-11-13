package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecommendationService {
    private Map<Film, Map<Film, Double>> diff;
    private Map<Film, Map<Film, Integer>> freq;
    private Map<User, HashMap<Film, Double>> outputData;
    private final FilmDao filmStorage;

    public Collection<Film> getRecommendations(User inputUser) {
        diff = new HashMap<>();
        freq = new HashMap<>();
        Map<User, Map<Film, Double>> inputData = filmStorage.getRateData();
        buildDifferencesMatrix(inputData);
        predict(inputData);
        if (outputData.containsKey(inputUser)) {
            return outputData.get(inputUser).keySet()
                    .stream()
                    .filter(film -> !film.getUserLikes().contains(inputUser))
                    .filter(film -> outputData.get(inputUser).get(film) > 5)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private void buildDifferencesMatrix(Map<User, Map<Film, Double>> data) {
        data.forEach((user, userRates) -> userRates.forEach((film, rate) -> {
            if (!diff.containsKey(film)) {
                diff.put(film, new HashMap<>());
                freq.put(film, new HashMap<>());
            }
            userRates.forEach(((film1, rate1) -> {
                int oldCount = 0;
                if (freq.get(film).containsKey(film1)) {
                    oldCount = freq.get(film).get(film1);
                }
                double oldDiff = 0.0;
                if (diff.get(film).containsKey(film1)) {
                    oldDiff = diff.get(film).get(film1);
                }
                double observedDiff = rate - rate1;
                freq.get(film).put(film1, oldCount + 1);
                diff.get(film).put(film1, oldDiff + observedDiff);
            }));
        }));
        diff.forEach(((film, rateDiff) -> rateDiff.forEach((film1, rateDiff1) -> {
            double oldValue = rateDiff1;
            int count = freq.get(film).get(film1);
            diff.get(film).put(film1, oldValue / count);
        })));
    }

    private void predict(Map<User, Map<Film, Double>> data) {
        outputData = new HashMap<>();
        HashMap<Film, Double> uPred = new HashMap<>();
        HashMap<Film, Integer> uFreq = new HashMap<>();

        data.forEach(((user, userRates) -> {
            diff.forEach((film, rateDiff) -> {
                uFreq.put(film, 0);
                uPred.put(film, 0.0);
            });
            userRates.forEach(((film, rate) -> diff.forEach(((film1, rateDiff) -> {
                if (rateDiff.containsKey(film)) {
                    double predictedValue = rateDiff.get(film) + userRates.get(film);
                    double finalValue = predictedValue * freq.get(film1).get(film);
                    uPred.put(film1, uPred.get(film1) + finalValue);
                    uFreq.put(film1, uFreq.get(film1) + freq.get(film1).get(film));
                }
            }))));
            HashMap<Film, Double> clean = new HashMap<>();
            uPred.forEach(((film, predRate) -> {
                if (uFreq.get(film) > 0) {
                    clean.put(film, uPred.get(film) / uFreq.get(film));
                }
            }));
            filmStorage.findAll().forEach(film -> {
                if (userRates.containsKey(film)) {
                    clean.put(film, userRates.get(film));
                } else if (!clean.containsKey(film)) {
                    clean.put(film, 1.0);
                }
            });
            outputData.put(user, clean);
        }));
    }
}
