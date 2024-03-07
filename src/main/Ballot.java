package main;

import java.util.*;

public class Ballot {
    private int ballotNum;
    private Map<Integer, Integer> votes; 
    private int ballotType; 

    public Ballot(String line, List<Candidate> candidates) {
    }

    public int getBallotNum() {
        return this.ballotNum;
    }

    public int getRankByCandidate(int candidateID) {
        return this.votes.getOrDefault(candidateID, -1);
    }

    public int getCandidateByRank(int rank) {
        for (Map.Entry<Integer, Integer> entry : this.votes.entrySet()) {
            if (entry.getValue() == rank) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public boolean eliminate(int candidateId) {
        return this.votes.remove(candidateId) != null;
    }

    public int getBallotType() {
        return this.ballotType;
    }
}
