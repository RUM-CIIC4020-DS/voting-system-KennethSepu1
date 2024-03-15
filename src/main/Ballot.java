package main;

import data_structures.SinglyLinkedList;
import interfaces.List;

public class Ballot {
    private int ballotNum;
    private List<Integer> candidateIDs;
    private List<Integer> rankings;
    private int ballotType;

    public Ballot(String line, List<Candidate> candidates) {
        String[] partsplit = line.split(",");
        this.ballotNum = Integer.parseInt(partsplit[0]);
        this.candidateIDs = new SinglyLinkedList<>();
        this.rankings = new SinglyLinkedList<>();
        int maxRank = 0;

        if (partsplit.length == 1) {
            this.ballotType = 1;
            return;
        }

        int i = 1;
        while (i < partsplit.length) {
            String[] pair = partsplit[i].split(":");
            int candidateID = Integer.parseInt(pair[0]);
            int rank = Integer.parseInt(pair[1]);

            if (rank < 1) {
                this.ballotType = 2;
                return;
            }

            if (rank > candidates.size()) {
                this.ballotType = 2;
                return;
            }

            if (rankings.contains(rank)) {
                this.ballotType = 2;
                return;
            }

            if (candidateIDs.contains(candidateID)) {
                this.ballotType = 2;
                return;
            }

            candidateIDs.add(candidateID);
            rankings.add(rank);

            if (rank > maxRank) {
                maxRank = rank;
            }

            i++;
        }

        if (maxRank != rankings.size()) {
            this.ballotType = 2;
            return;
        }

        if (this.candidateIDs.isEmpty()) {
            this.ballotType = 1;
        } else {
            this.ballotType = 0;
        }
    }

    private int getIndexOf(List<Integer> list, int value) {
        int index = 0;
        while (index < list.size()) {
            if (list.get(index) == value) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public int getBallotNum() {
        return this.ballotNum;
    }

    public int getRankByCandidate(int candidateID) {
        for (int i = 0; i < candidateIDs.size(); i++) {
            if (candidateIDs.get(i) == candidateID) {
                if (i < rankings.size()) { // Check if index is within bounds
                    return rankings.get(i);
                } else {
                    // Handle the case where the index is out of bounds
                    return -1; // or throw an exception or handle the case appropriately
                }
            }
        }
        return -1;
    }

    public int getCandidateByRank(int rank) {
        if (rank > 0 && rank <= rankings.size()) {
            if (rank - 1 < candidateIDs.size()) { // Check if index is within bounds
                return candidateIDs.get(rank - 1);
            } else {
                // Handle the case where the index is out of bounds
                return -1; // or throw an exception or handle the case appropriately
            }
        }
        return -1;
    }
    
    public boolean eliminate(int eliminatedCandidateId) {
        int index = getIndexOf(candidateIDs, eliminatedCandidateId);
        if (index != -1) {
            if (index < rankings.size()) {
                rankings.remove(index);
            }
            candidateIDs.remove(index);
            return true;
        }
        return false;
    }


    public int getBallotType() {
        return this.ballotType;
    }
}
