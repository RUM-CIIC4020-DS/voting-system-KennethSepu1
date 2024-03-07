package main;

public class Election {
    private Candidate[] candidates;
    private int totalBallots;
    private int totalInvalidBallots;
    private int totalBlankBallots;
    private int totalValidBallots;

    public Election() {
      
    }

    public Election(String candidates_filename, String ballot_filename) {
    }

    public String getWinner() {
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
        return new String[0];
    }

	public Candidate[] getCandidates() {
		return candidates;
	}

	public void setCandidates(Candidate[] candidates) {
		this.candidates = candidates;
	}
}

