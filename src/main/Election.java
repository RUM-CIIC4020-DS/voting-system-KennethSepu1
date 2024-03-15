package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import data_structures.ArrayList;
import data_structures.DoublyLinkedList;
import interfaces.List;

public class Election {
    private List<Ballot> ballots;
    private List<Candidate> candidates;
    private int totalBallots;
    private int totalInvalidBallots;
    private int totalBlankBallots;
    private int totalValidBallots;
    private DoublyLinkedList<String> eliminatedCandidates;

    public Election() {
    	this.eliminatedCandidates = new DoublyLinkedList<>();
    	this.ballots = new DoublyLinkedList<>();
        this.candidates = new DoublyLinkedList<>();
        this.totalBallots = 0;
        this.totalInvalidBallots = 0;
        this.totalBlankBallots = 0;
        this.totalValidBallots = 0;

         try (BufferedReader candidatesReader = new BufferedReader(new FileReader("inputFiles/candidates.csv"));
                 BufferedReader ballotReader = new BufferedReader(new FileReader("inputFiles/ballots.csv"))) {
             String line;

             while ((line = candidatesReader.readLine()) != null) {
                 this.candidates.add(new Candidate(line));
             }

             while ((line = ballotReader.readLine()) != null) {
                 Ballot ballot = new Ballot(line, this.candidates);
                 this.ballots.add(ballot);
                 this.totalBallots++;
                 switch (ballot.getBallotType()) {
                     case 0:
                         this.totalValidBallots++;
                         break;
                     case 1:
                         this.totalBlankBallots++;
                         break;
                     case 2:
                         this.totalInvalidBallots++;
                         break;
                     default:
                         break;
                 }
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

    public Election(String candidatesFilename, String ballotFilename) {
    	this.eliminatedCandidates = new DoublyLinkedList<>();
    	this.ballots = new DoublyLinkedList<>();
        this.candidates = new DoublyLinkedList<>();
        this.totalBallots = 0;
        this.totalInvalidBallots = 0;
        this.totalBlankBallots = 0;
        this.totalValidBallots = 0;


        try (BufferedReader candidatesReader = new BufferedReader(new FileReader("inputFiles/" + candidatesFilename));
                BufferedReader ballotReader = new BufferedReader(new FileReader("inputFiles/" + ballotFilename))) {
            String line;

            while ((line = candidatesReader.readLine()) != null) {
                this.candidates.add(new Candidate(line));
            }

            while ((line = ballotReader.readLine()) != null) {
                Ballot ballot = new Ballot(line, this.candidates);
                this.ballots.add(ballot);
                this.totalBallots++;
                switch (ballot.getBallotType()) {
                    case 0:
                        this.totalValidBallots++;
                        break;
                    case 1:
                        this.totalBlankBallots++;
                        break;
                    case 2:
                        this.totalInvalidBallots++;
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWinner() {
        int[] voteCount = new int[this.candidates.size() + 1];

        for (Ballot ballot : this.ballots) {
            if (ballot.getBallotType() == 0) {
                int candidateID = ballot.getCandidateByRank(1);
                if (candidateID != -1) {
                    voteCount[candidateID]++;
                }
            }
        }

        int maxVotes = -1;
        int winnerID = -1;
        for (int i = 1; i < voteCount.length; i++) {
            if (voteCount[i] > maxVotes) {
                maxVotes = voteCount[i];
                winnerID = i;
            }
        }

        String winnerName = "";
        for (Candidate candidate : this.candidates) {
            if (candidate.getId() == winnerID) {
                winnerName = candidate.getName();
                break;
            }
        }

        return winnerName;
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
    
    public DoublyLinkedList<String> getEliminatedCandidates() {
        return eliminatedCandidates;
    }
    public void printBallotDistribution() {
        System.out.println("Total ballots: " + this.totalBallots);
        System.out.println("Total blank ballots: " + this.totalBlankBallots);
        System.out.println("Total invalid ballots: " + this.totalInvalidBallots);
        System.out.println("Total valid ballots: " + this.totalValidBallots);

        for (Candidate c : this.candidates) {
            System.out.print(c.getName().substring(0, c.getName().indexOf(" ")) + "\t");
            for (Ballot b : this.ballots) {
                int rank = b.getRankByCandidate(c.getId());
                String tableline = "| " + ((rank != -1) ? rank : " ") + " ";
                System.out.print(tableline);
            }
            System.out.println("|");
        }
    }
}
