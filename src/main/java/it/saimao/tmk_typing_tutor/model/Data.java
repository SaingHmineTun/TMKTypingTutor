package it.saimao.tmk_typing_tutor.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    public static final ObservableList<String> musicList = FXCollections.observableArrayList("No Background Music", "Classroom Timer withRelaxing WAVES Music", "Soft Peaceful Music Timer", "Timer with Relaxing MusicandAlarm", "NatureBackground Sound", "Peaceful Ambient StressRelief", "CalmNature Sounds", "Rain and Thunderstorm Sounds", "Waterfall Relaxing Nature Sounds", "Relaxing Music for Stress Relief");

    public static final Map<String, String> musicNameToFileMap = Map.of(
            musicList.get(0), "",
            musicList.get(1), "bgsound1.mp3",
            musicList.get(2), "bgsound2.mp3",
            musicList.get(3), "bgsound3.mp3",
            musicList.get(4), "bgsound4.mp3",
            musicList.get(5), "bgsound5.mp3",
            musicList.get(6), "bgsound6.mp3",
            musicList.get(7), "bgsound7.mp3",
            musicList.get(8), "bgsound8.mp3",
            musicList.get(9), "bgsound9.mp3");
}
