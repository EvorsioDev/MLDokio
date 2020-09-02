package ru.armagidon.mldokio.util.data;

import lombok.Getter;

public class Pair<F, S>
{
    private @Getter final F first;
    private @Getter final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> of(F first, S second){
        return new Pair<>(first, second);
    }
}
