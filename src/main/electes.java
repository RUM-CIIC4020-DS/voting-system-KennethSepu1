package main;

/**
 * Class that contains the main method that runs elections
 * used to test the code
 */
public class electes {
    /**
     * Main method to run the election simulation.
     * @param arg =  arguments
     */
    public static void main(String[] args) {
        //  run the first election
        Election election = new Election();
        System.out.println("First Election:");
        election.printBallotDistribution();
        System.out.println("Winner: " + election.getWinner());

        //  run the second election
        Election election2 = new Election("candidates.csv", "ballots.csv");
        System.out.println("\nSecond Election:");
        election2.printBallotDistribution();
        System.out.println("Winner: " + election2.getWinner());
    }
}

