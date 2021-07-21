package entities;

public class Travel {
    private int id;
    private String start;
    private String end;
    private String date;
    private int numberPassenger;
    private IUser user;
    private boolean isDriver = false;

    Travel(int id, IUser user, String start, String end, String date) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.date = date;
        this.user = user;
        this.numberPassenger = 0;
    }

    Travel(int id, IUser user, String start, String end, String date, int numberPassenger) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.date = date;
        this.user = user;
        this.numberPassenger = numberPassenger;
        this.isDriver = true;
    }

    public int getID() {
        return this.id;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    public String getDate() {
        return this.date;
    }

    public IUser getUser() {
        return this.user;
    }

    public boolean getIsDriver() {
        return this.isDriver;
    }

    public int getNumberPassenger() {
        return this.numberPassenger;
    }

    public boolean compare(Travel travel) {
        return (travel.getStart().equals(this.start) && travel.getEnd().equals(this.end)
                && travel.getDate().equals(this.date));
    }
}