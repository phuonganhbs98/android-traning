package com.atom.traningandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class StatisticList {
    @SerializedName("statistics")
    private List<Statistic> statistics;

    public StatisticList(List<Statistic> statistics) {
        this.statistics = statistics;
    }

    public StatisticList() {
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }
}
