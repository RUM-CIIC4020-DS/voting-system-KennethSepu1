package main;

/**
 * Candidate with ID,name and status
 * class used to create a candidate
 * 
 * @author Kenneth S. Sepulveda
 * @since 2024-03-15
 */
public class Candidate {
    
    private int id; // identifier for the candidate
    private String Name; //  name of the candidate
    private boolean active; // check status to see if candidate is active or not
    
    /**
     * Constructor for Candidate object with ID, name, and the active status.
     * 
     * @param id =  identifies the candidate
     * @param Name =  name of the candidate
     * @param active = candidates status
     */
    public Candidate(int id, String Name, boolean active) {
        this.id = id;
        this.Name = Name;
        this.active = active;  
    }

    /**
     * Constructs the Candidate object by using a string line with ID and name.
     * 
     * @param line =  A string containing the id and name
     * @throws IllegalArgumentException if its incorrect
     */
    public Candidate(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 2) {
            this.id = Integer.parseInt(parts[0].trim());
            this.Name = parts[1].trim();
            this.active = true; 
        } else {
            throw new IllegalArgumentException("Input format incorrect for Candidate: " + line);
        }
    }

    /**
     * 
     * @return id of candidate
     */
    public int getId() {
        return id;
    }
    
    /**
     * 
     * @param set the id of the candidate.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return check if candidate is active, false if not
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * 
     * 
     * @param check status where is active or not
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return gets the name of the candidate
     */
    public String getName() {
        return Name;
    }
    
    /**
     * 
     * @param sets the name of the candidate
     */
    public void setName(String Name) {
        this.Name = Name;
    }
}
