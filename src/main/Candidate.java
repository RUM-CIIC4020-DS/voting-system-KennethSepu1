package main;

public class Candidate {
    
    private int id;
    private String Name;
    private boolean active;
    
    public Candidate(int id, String Name, boolean active) {
        this.id = id;
        this.Name = Name;
        this.active = active;  
    }

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

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return Name;
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }
}