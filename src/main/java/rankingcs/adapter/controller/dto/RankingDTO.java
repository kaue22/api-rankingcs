package rankingcs.adapter.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RankingDTO {

    private int standing;
    private int points;
    private String teamName;
    private List<String> roster;
    private LocalDateTime modifiedDate;

    public RankingDTO(int standing, int points, String teamName, List<String> roster, LocalDateTime modifiedDate) {
        this.standing = standing;
        this.points = points;
        this.teamName = teamName;
        this.roster = roster;
        this.modifiedDate = modifiedDate;
    }

    // Getters and setters
    public int getStanding() {
        return standing;
    }

    public void setStanding(int standing) {
        this.standing = standing;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getRoster() {
        return roster;
    }

    public void setRoster(List<String> roster) {
        this.roster = roster;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
