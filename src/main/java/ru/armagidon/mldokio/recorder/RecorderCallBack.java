package ru.armagidon.mldokio.recorder;

public interface RecorderCallBack
{
    default void onStopPlaying(){}
    default void onStartPlaying(){}
}
