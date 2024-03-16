package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import data_structures.ArrayList;
import interfaces.List;

public class Election {
	
    private ArrayList<Ballot> ballots;
    private ArrayList<Candidate> candidates;
    private ArrayList<String> eliminatedCandidates;

    private String winner = null;
    
    private int totalBallots = 0;
    private int totalInvalidBallots = 0;
    private int totalBlankBallots = 0;
    private int totalValidBallots = 0;

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

    private void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

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

    private void processValidBallot(Ballot ballot) {
        ballots.add(ballot);
        totalValidBallots++;
    }

    private void processBlankBallot() {
        totalBlankBallots++;
    }

    private void processInvalidBallot() {
        totalInvalidBallots++;
    }

    public String getWinner() {
                if (winner != null) {
            return winner;
        }
                return "";
    }
    
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

    private void checkWinner(int[] tally) {
        for (int i = 0; i < candidates.size(); i++) {
            if (tally[i] > totalValidBallots / 2) {
                winner = candidates.get(i).getName();
                return;
            }
        }
    }

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

    public int getTotalBallots() {
        return totalBallots;
    }

    public int getTotalInvalidBallots() {
        return totalInvalidBallots;
    }

    public int getTotalBlankBallots() {
        return totalBlankBallots;
    }

    public int getTotalValidBallots() {
        return totalValidBallots;
    }
    
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
    
    public List<String> getEliminatedCandidates() {
                return eliminatedCandidates;
    }
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