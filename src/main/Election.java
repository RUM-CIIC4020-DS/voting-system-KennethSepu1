package main;

public class Election {
    private Candidate[] candidates;
    private int totalBallots;
    private int totalInvalidBallots;
    private int totalBlankBallots;
    private int totalValidBallots;

    public Election() {
        // Implement logic to read candidates.csv and ballots.csv[^5^][5]
        // Initialize candidates array
        // Count totalBallots, totalInvalidBallots, totalBlankBallots, totalValidBallots
    }

    public Election(String candidates_filename, String ballot_filename) {
        // Implement logic to read specified candidate and ballot files[^5^][5]
        // Initialize candidates array
        // Count totalBallots, totalInvalidBallots, totalBlankBallots, totalValidBallots
    }

    public String getWinner() {
        // Implement logic to determine the winner
        return "";
    }

    public int getTotalBallots() {
        return this.totalBallots;
    }

    public int getTotalInvalidBallots() {
        return this.totalInvalidBallots;
    }

    public int getTotalBlankBallots() {
        return this.totalBlankBallots;
    }

    public int getTotalValidBallots() {
        return this.totalValidBallots;
    }

    public String[] getEliminatedCandidates() {
        // Implement logic to get list of eliminated candidates
        return new String[0];
    }

	public Candidate[] getCandidates() {
		return candidates;
	}

	public void setCandidates(Candidate[] candidates) {
		this.candidates = candidates;
	}
}

