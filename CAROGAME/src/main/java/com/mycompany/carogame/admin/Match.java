package com.mycompany.carogame.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Match {

    private final StringProperty matchId;
    private final StringProperty player1Id;
    private final StringProperty player2Id;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty winnerId;
    
    
    public Match(String matchId, String player1Id, String player2Id, String startTime, String endTime, String winnerId) {
        this.matchId = new SimpleStringProperty(matchId);
        this.player1Id = new SimpleStringProperty(player1Id);
        this.player2Id = new SimpleStringProperty(player2Id);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.winnerId = new SimpleStringProperty(winnerId);
    }
    
    public String getMatchId() {
        return matchId.get();
    }

    public StringProperty matchIdProperty() {
        return matchId;
    }

    public String getPlayer1Id() {
        return player1Id.get();
    }

    public StringProperty player1IdProperty() {
        return player1Id;
    }

    public String getPlayer2Id() {
        return player2Id.get();
    }

    public StringProperty player2IdProperty() {
        return player2Id;
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public String getWinnerId() {
        return winnerId.get();
    }

    public StringProperty winnerIdProperty() {
        return winnerId;
    }
}
