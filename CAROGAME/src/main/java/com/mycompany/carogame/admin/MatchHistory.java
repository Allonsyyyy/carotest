package com.mycompany.carogame.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MatchHistory {

    private final StringProperty historyId;
    private final StringProperty matchId;
    private final StringProperty playerId;
    private final StringProperty moveNumber;
    private final StringProperty moveTime;

    public MatchHistory(String historyId, String matchId, String playerId, String moveNumber, String moveTime) {
        this.historyId = new SimpleStringProperty(historyId);
        this.matchId = new SimpleStringProperty(matchId);
        this.playerId = new SimpleStringProperty(playerId);
        this.moveNumber = new SimpleStringProperty(moveNumber);
        this.moveTime = new SimpleStringProperty(moveTime);
    }

    public String getHistoryId() {
        return historyId.get();
    }

    public StringProperty historyIdProperty() {
        return historyId;
    }

    public String getMatchId() {
        return matchId.get();
    }

    public StringProperty matchIdProperty() {
        return matchId;
    }

    public String getPlayerId() {
        return playerId.get();
    }

    public StringProperty playerIdProperty() {
        return playerId;
    }

    public String getMoveNumber() {
        return moveNumber.get();
    }

    public StringProperty moveNumberProperty() {
        return moveNumber;
    }


    public String getMoveTime() {
        return moveTime.get();
    }

    public StringProperty moveTimeProperty() {
        return moveTime;
    }
}
