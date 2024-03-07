package main;

public class Candidate {
    private int id;
    private String name;
    private boolean active;

    public Candidate(String line) {
        String[] parts = line.split(",");
        this.id = Integer.parseInt(parts[0]);
        this.name = parts[1];
        this.active = true;
    }

    public int getId() {
        return this.id;
    }

    public boolean isActive() {
        return this.active;
    }

    public String getName() {
        return this.name;
    }

    public void deactivate() {
        this.active = false;
    }
}

