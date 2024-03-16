package main;

import data_structures.ArrayList;
import data_structures.SinglyLinkedList;
import interfaces.List;

/**
 * This class provides methods to construct a Ballot object, retrieve ballot information, and perform operations on the ballot.
 * Ballot class that has the information from the number , id , rank and ballot type
 *  this is a constructor for the ballot class that receives the information and outputs the operation. 
 * @author Kenneth S. Sepulveda
 * @since 2024-03-15
 */
public class Ballot {
    private int ballotNum; // The number assigned to the ballot
    private List<Integer> candidateIDs; //list of candidate id in ballot
    private List<Integer> rankings; // ranking list for the candidates 
    private List<List<Ballot>> listoflist; // A list of lists of ballots (we use it in the future)
    private int ballotType; // The type of the ballot (0 for valid, 1 for empty, 2 for invalid)

    /**
     * Constructs a Ballot object by passing a string line and a list of candidates.
     *
     *
     * @param line = A string containing comma-separated ballot info
     * @param candidates =  A list of Candidate 
     */
    public Ballot(String line, List<Candidate> candidates) {
        String[] partsplit = line.split(",");
        this.ballotNum = Integer.parseInt(partsplit[0]);
        this.listoflist = new ArrayList<>(candidates.size());
        this.candidateIDs = new SinglyLinkedList<>();
        this.rankings = new SinglyLinkedList<>();
        int maxRank = 0;
        
        int x = 0;
        while (x < candidates.size()) {
            this.listoflist.add(new ArrayList<>());
            x++;
        }

        if (partsplit.length == 1) {
            this.ballotType = 1;
            return;
        }

        int i = 1;
        while (i < partsplit.length) {
            String[] pair = partsplit[i].split(":");
            int candidateID = Integer.parseInt(pair[0]);
            int rank = Integer.parseInt(pair[1]);

            if (rank < 1 || rank > candidates.size() || rankings.contains(rank) || candidateIDs.contains(candidateID)) {
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

        this.ballotType = candidateIDs.isEmpty() ? 1 : 0;
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

    /**
     * Retrieves the number assigned to the ballot.
     * 
     * @return The ballot number
     */
    public int getBallotNum() {
        return this.ballotNum;
    }

    /**
     * Retrieves the rank of a candidate identified by their ID in the ballot.
     * 
     * @param candidateID = ID of the candidate
     * @return  rank of the candidate in the ballot, -1 if not found
     */
    public int getRankByCandidate(int candidateID) {
        for (int i = 0; i < candidateIDs.size(); i++) {
            if (candidateIDs.get(i) == candidateID) {
                return (i < rankings.size()) ? rankings.get(i) : -1;
            }
        }
        return -1;
    }

    /**
     * Retrieves the ID of the candidate ranked at the specified position in the ballot.
     * 
     * @param rank = The position of the candidate
     * @return The ID of the candidate, -1 if not found
     */
    public int getCandidateByRank(int rank) {
        return (rank > 0 && rank <= rankings.size()) ? ((rank - 1 < candidateIDs.size()) ? candidateIDs.get(rank - 1) : -1) : -1;
    }
    
    /**
     * Eliminates a candidate from the ballot by their ID.
     * 
     * @param eliminatedCandidateId The ID of the candidate to be eliminated
     * @return true if the candidate is successfully eliminated, false otherwise
     */
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

    /**
     * Retrieves the ballot type.
     * 
     * @return The type of the ballot (0 for valid, 1 for empty, 2 for invalid)
     */
    public int getBallotType() {
        return this.ballotType;
    }
}
