package com.lusr.pig.bean;

public class HomeParam {
    private String homeId;
    private String conditionName;
    private String conditionLimit;
    private String conditionMinLimit;
    private String conditionData;
    private int status;

    public String getConditionMinLimit() {
        return conditionMinLimit;
    }

    public void setConditionMinLimit(String conditionMinLimit) {
        this.conditionMinLimit = conditionMinLimit;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getConditionLimit() {
        return conditionLimit;
    }

    public void setConditionLimit(String conditionLimit) {
        this.conditionLimit = conditionLimit;
    }

    public String getConditionData() {
        return conditionData;
    }

    public void setConditionData(String conditionData) {
        this.conditionData = conditionData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
