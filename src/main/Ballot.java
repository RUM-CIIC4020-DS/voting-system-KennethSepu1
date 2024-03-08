package main;

import java.util.ArrayList;
import java.util.List;

public class Ballot {
    private int ballotNum;
    private List<Integer> votes; 
    private int ballotType;

    public Ballot(String line, interfaces.List<Candidate> candidates) {
    	 	
    		this.ballotType = 0;
            boolean[] checkedRanks = new boolean[candidates.size()];
            String[] parts = line.split(",");
            this.ballotNum = Integer.parseInt(parts[0]);
            this.votes = new ArrayList<>();
            
            for (int i = 1; i <= candidates.size(); i++) {
                this.votes.add(0); 
            }
            for (int i = 1; i < parts.length; i++) {
                String[] pair = parts[i].split(":");
                int candidateID = Integer.parseInt(pair[0]);
                int rank = Integer.parseInt(pair[1]);
                if (rank > candidates.size() || this.votes.get(rank - 1) != 0) {
                    this.ballotType = 2; 
                    break;
                } else {
                    this.votes.set(rank - 1, candidateID);
                }
            }
            if (this.ballotType != 2) {
                boolean zero = true;
                for (int vote : this.votes) {
                    if (vote != 0) {
                        zero = false;
                        break;
                    }
                }
                if (zero) {
                    this.ballotType = 1;
                } else {
                    this.ballotType = 0;
                }
                
                for (int rank : votes) {
                    if (rank > candidates.size() || checkedRanks[rank - 1]) {
                        this.ballotType = 2;
                        break;
                    }
                    checkedRanks[rank - 1] = true;
                }
                for (int i = 0; i < checkedRanks.length; i++) {
                    if (!checkedRanks[i] && i < votes.size()) {
                        this.ballotType = 2; 
                        break;
                }
            }
        }
    }

    public int getBallotNum() {
        return this.ballotNum;
    }

    public int getRankByCandidate(int candidateID) {
        return this.votes.indexOf(candidateID) + 1;
    }
    public int getCandidateByRank(int rank) {
        return this.votes.get(rank - 1); 
    }

    public boolean eliminate(int candidateId) {
        int index = votes.indexOf(candidateId);
        if (index != -1) {
            votes.set(index, -1); 
            for (int i = 0; i < votes.size(); i++) {
                if (votes.get(i) > candidateId) {
                    votes.set(i, votes.get(i) - 1);
                }
            }
            return true;
        }
        return false;
    }

    public int getBallotType() {
        return this.ballotType;
    }
}

