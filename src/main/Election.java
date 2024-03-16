package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import data_structures.ArrayList;
import interfaces.List;

/**
 * Represents an election process that manages ballots, candidates, and the voting rounds.
 * The class provides methods to read candidate and ballot files, process ballots, determine the winner,
 * and perform voting rounds including elimination of candidates.
 * 
 * @author Kenneth S. Sepulveda
 * @since 2024-03-15
 */
public class Election {
    
    private ArrayList<Ballot> ballots; // list of all ballots 
    private ArrayList<Candidate> candidates; // list of candidates
    private ArrayList<String> eliminatedCandidates; //list of eliminated candidates
    private String winner = null; // winner of elections
    
    private int totalBallots = 0; // total number of ballots 
    private int totalInvalidBallots = 0; // total number of invalid ballots
    private int totalBlankBallots = 0; // total number of blank ballots
    private int totalValidBallots = 0; // total number of valid ballots

    /**
     * constructor for election that initiates the ballot and candidate list
     * Reads candidate and ballot files and performs voting rounds.
     */
    public Election() {
        try {
            this.ballots = new ArrayList<>();
            this.candidates = new ArrayList<>();
            this.eliminatedCandidates = new ArrayList<>();
            ballotandcandidatefiles("inputFiles/candidates.csv", "inputFiles/ballots.csv");
            VotingRounds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Constructs an Election object with specified candidate and ballot files.
     * Reads candidate and ballot files and performs voting rounds.
     * 
     * @param candidates_filename =  filename of the candidate file
     * @param ballot_filename =  filename of the ballot file
     */
    public Election(String candidates_filename, String ballot_filename) {
        try {
            this.ballots = new ArrayList<>();
            this.candidates = new ArrayList<>();
            this.eliminatedCandidates = new ArrayList<>();
            ballotandcandidatefiles("inputFiles/" + candidates_filename, "inputFiles/" + ballot_filename);
            VotingRounds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read candidate and ballot files and initialize candidates and ballots lists
    private void ballotandcandidatefiles(String candidatesFile, String ballotsFile) throws IOException {
        try (BufferedReader candidateReader = new BufferedReader(new FileReader(candidatesFile));
             BufferedReader ballotReader = new BufferedReader(new FileReader(ballotsFile))) {

            String candidateLine;
            while ((candidateLine = candidateReader.readLine()) != null) {
                addCandidate(new Candidate(candidateLine));
            }

            String ballotLine;
            while ((ballotLine = ballotReader.readLine()) != null) {
                processBallot(new Ballot(ballotLine, candidates));
            }
        }
    }

    // Add candidate to  candidates list
    private void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

    // Process a ballot and update ballot statistics
    private void processBallot(Ballot ballot) {
        switch (ballot.getBallotType()) {
            case 0:
                processValidBallot(ballot);
                break;
            case 1:
                processBlankBallot();
                break;
            default:
                processInvalidBallot();
                break;
        }
        totalBallots++;
    }

    // Process a valid ballot and add it to the ballots list
    private void processValidBallot(Ballot ballot) {
        ballots.add(ballot);
        totalValidBallots++;
    }

    // Process a blank ballot
    private void processBlankBallot() {
        totalBlankBallots++;
    }

    // Process an invalid ballot
    private void processInvalidBallot() {
        totalInvalidBallots++;
    }

    /**
     * Retrieve the winner of the election.
     * 
     * @return The name of the winning candidate, or an empty string if no winner is determined yet
     */
    public String getWinner() {
        return (winner != null) ? winner : "";
    }
    
    // Perform voting rounds until a winner is determined
    public void VotingRounds() {
        while (winner == null) {
            int[] tally = new int[candidates.size()];
            for (Ballot ballot : ballots) {
                int topChoiceId = ballot.getCandidateByRank(1);
                if (topChoiceId != -1) tally[topChoiceId - 1]++;
            }
            checkWinner(tally);
            if (winner == null) {
                eliminateCandidate(tally);
            }
        }
    }

    // Check if a winner is determined based on the tally
    private void checkWinner(int[] tally) {
        for (int i = 0; i < candidates.size(); i++) {
            if (tally[i] > totalValidBallots / 2) {
                winner = candidates.get(i).getName();
                return;
            }
        }
    }

    // Eliminate candidate(s) based on tally
    private void eliminateCandidate(int[] tally) {
        List<Integer> eliminationList = getEliminationList(tally);
        int preference = 2;
        while (eliminationList.size() > 1 && preference <= candidates.size()) {
            eliminationList = getNewEliminationList(eliminationList, preference);
            preference++;
        }
        int eliminationId = getTopIdAmongCandidates(eliminationList);
        eliminatedCandidates.add(candidates.get(eliminationId).getName() + "-" + tally[eliminationId]);
        for (Ballot ballot : ballots) ballot.eliminate(eliminationId + 1);
    }

    // Get the list of candidates to be eliminated based on the tally
    private List<Integer> getEliminationList(int[] tally) {
        int leastVotes = 99999999;
        List<Integer> eliminationList = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            if (tally[i] < leastVotes && !Eliminated(candidates.get(i).getName())) {
                leastVotes = tally[i];
                eliminationList.clear();
                eliminationList.add(i);
            } else if (tally[i] == leastVotes) {
                eliminationList.add(i);
            }
        }
        return eliminationList;
    }

    // Get the list of candidates to be eliminated based on the new preference
    private List<Integer> getNewEliminationList(List<Integer> eliminationList, int preference) {
        int[] preferenceTally = new int[candidates.size()];
        for (Ballot ballot : ballots) {
            for (int id : eliminationList) {
                if (ballot.getRankByCandidate(id + 1) == preference) preferenceTally[id]++;
            }
        }
        int leastVotes = 9999999;
        List<Integer> newEliminationList = new ArrayList<>();
        for (int id : eliminationList) {
            if (preferenceTally[id] < leastVotes) {
                leastVotes = preferenceTally[id];
                newEliminationList.clear();
                newEliminationList.add(id);
            } else if (preferenceTally[id] == leastVotes) {
                newEliminationList.add(id);
            }
        }
        return newEliminationList;
    }

    /**
     * Get total number of ballots.
     * 
     * @return The total number of ballots
     */
    public int getTotalBallots() {
        return totalBallots;
    }

    /**
     * Get total number of invalid ballots.
     * 
     * @return The total number of invalid ballots
     */
    public int getTotalInvalidBallots() {
        return totalInvalidBallots;
    }

    /**
     * Get total number of blank ballots.
     * 
     * @return The total number of blank ballots
     */
    public int getTotalBlankBallots() {
        return totalBlankBallots;
    }

    /**
     * Get total number of valid ballots.
     * 
     * @return The total number of valid ballots
     */
    public int getTotalValidBallots() {
        return totalValidBallots;
    }
    
    // Get the ID of the top candidate among candidates for elimination
    private int getTopIdAmongCandidates(List<Integer> candidatesForElimination) {
        int highestId = -1;
        for (int candidateIndex : candidatesForElimination) {
        	if (highestId == -1) {
        	    highestId = candidateIndex;
        	} else if (candidates.get(candidateIndex).getId() > candidates.get(highestId).getId()) {
        	    highestId = candidateIndex;
        	}
        	return highestId;
        }
        return highestId;
    }
    
    /**
     * Get list of eliminated candidates.
     * 
     * @return The list of eliminated candidates
     */
    public List<String> getEliminatedCandidates() {
        return eliminatedCandidates;
    }
    
    // Check if a candidate is eliminated
    private boolean Eliminated(String candidateName) {
    	int i = 0;
    	while (i < eliminatedCandidates.size()) {
    	    if (eliminatedCandidates.get(i).startsWith(candidateName)) {
    	        return true;
    	    }
    	    i++;
    	}
    	return false;

    }
    
    // Print ballot distribution statistics
    public void printBallotDistribution() {
        System.out.println("Total ballots:" + getTotalBallots());
        System.out.println("Total blank ballots:" + getTotalBlankBallots());
        System.out.println("Total invalid ballots:" + getTotalInvalidBallots());
        System.out.println("Total valid ballots:" + getTotalValidBallots());
        System.out.println(getEliminatedCandidates());

        for (Candidate c : candidates) {
            System.out.print(c.getName().substring(0, c.getName().indexOf(" ")) + "\t");
            for (Ballot b : ballots) {
                int rank = b.getRankByCandidate(c.getId());
                String tableline = "| " + ((rank != -1) ? rank : " ") + " ";
                System.out.print(tableline);
            }
            System.out.println("|");
        }
    }
}
