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
        Map<User, HashMap<Film, Double>> inputData = filmStorage.getRateData();
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

    private void buildDifferencesMatrix(Map<User, HashMap<Film, Double>> data) {
        for (HashMap<Film, Double> user : data.values()) {
            for (Map.Entry<Film, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<>());
                    freq.put(e.getKey(), new HashMap<>());
                }
                for (Map.Entry<Film, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey());
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey());
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Film film : diff.keySet()) {
            for (Film film1 : diff.get(film).keySet()) {
                double oldValue = diff.get(film).get(film1);
                int count = freq.get(film).get(film1);
                diff.get(film).put(film1, oldValue / count);
            }
        }
    }

    private void predict(Map<User, HashMap<Film, Double>> data) {
        outputData = new HashMap<>();
        HashMap<Film, Double> uPred = new HashMap<>();
        HashMap<Film, Integer> uFreq = new HashMap<>();
        for (Map.Entry<User, HashMap<Film, Double>> e : data.entrySet()) {
            for (Film film : diff.keySet()) {
                uFreq.put(film, 0);
                uPred.put(film, 0.0);
            }
            for (Film film : e.getValue().keySet()) {
                for (Film film1 : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(film1).get(film) + e.getValue().get(film);
                        double finalValue = predictedValue * freq.get(film1).get(film);
                        uPred.put(film1, uPred.get(film1) + finalValue);
                        uFreq.put(film1, uFreq.get(film1) + freq.get(film1).get(film));
                    } catch (NullPointerException e1) {
                        // do nothing
                    }
                }
            }
            HashMap<Film, Double> clean = new HashMap<>();
            for (Film film : uPred.keySet()) {
                if (uFreq.get(film) > 0) {
                    clean.put(film, uPred.get(film) / uFreq.get(film));
                }
            }
            for (Film film : filmStorage.findAll()) {
                if (e.getValue().containsKey(film)) {
                    clean.put(film, e.getValue().get(film));
                } else if (!clean.containsKey(film)) {
                    clean.put(film, 1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }
    }
}
