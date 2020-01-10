import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Session {

  private Track track;
  private ArrayList<Team> teamList;
  private int totalLaps;
  private Team winner;
  public Session() {
    winner=null;
  }

  public Session(Track track, ArrayList<Team> teamList, int totalLaps) {
    // Fill this method
    this.track=track;
    this.teamList=teamList;
    this.totalLaps=totalLaps;
    this.winner=null;
  }

  public Track getTrack() {
    return track;
  }

  public void setTrack(Track track) {
    this.track = track;
  }
  public void setWinner(Team win){this.winner=win;}

  public ArrayList<Team> getTeamList() {
    return teamList;
  }

  public void setTeamList(ArrayList<Team> teamList) {
    this.teamList = teamList;
  }

  public int getTotalLaps() {
    return totalLaps;
  }
  public Team getWinner(){
    return winner;
  }

  public void setTotalLaps(int totalLaps) {
    this.totalLaps = totalLaps;
  }

  public void simulate() {
    if(!getTrack().isValidTrack()){
      System.out.print("Track is invalid.Simulation aborted!");
      return;
    }
    double best_time=Double.MAX_VALUE;
    System.out.print("Track is valid.Strating simulation on "+ getTrack().getTrackName()+" for "+getTotalLaps()+" laps.\n");
    for (int h = 0; h < getTeamList().size(); h++) {
      ArrayList<Car> cars = getTeamList().get(h).getCarList();
      double team_time = 0;
      for (int a = 0; a < cars.size(); a++) {
        for (int i = 0; i < getTotalLaps(); i++) {
          for (int j = 0; j < getTrack().getTrackLength(); j++) { // her feature için gir
            TrackFeature feat = getTrack().getNextFeature();
            cars.get(a).tick(feat);
          }
        }
        team_time += cars.get(a).getTotalTime();
      }

      if (team_time < best_time) {
        best_time = team_time;
        setWinner(getTeamList().get(h));
      }
    }
    System.out.print(printWinnerTeam());
    System.out.print(printTimingTable());
  }

  public String printWinnerTeam() {
    if(getWinner()==null){
      simulate();
    }
    String part1= "Team "+getWinner().getName()+ " wins.";
    String part2="";
    String[] colors= getWinner().getTeamColors();
    for(int i=0; i< colors.length;i++){
        if(i==0){
          part2+=colors[i];
        }
        else if(i==colors.length-1){
          part2+=" and "+colors[i];
        }
        else{
          part2+=", "+colors[i];
        }
    }
    return part1+part2+" flags are waving everywhere.\n";
    //"<team color 1> [[, <team color 2>,<team color 3>, ...] and <team color n>] flags are waving everywhere."
  }

  private String printTimingTable() {
    ArrayList<Car> All_cars=new ArrayList<Car>();
    for (int h = 0; h < getTeamList().size(); h++) {
      ArrayList<Car> cars = getTeamList().get(h).getCarList();
      for (int a = 0; a < cars.size(); a++) {
        All_cars.add(cars.get(a));
      }
    }
    getWinner().setCarList(All_cars);
    getWinner().getCarList().sort(new Comparator<Car>() {
      @Override
      public int compare(Car o1, Car o2) {
        if(o1.getTotalTime()>o2.getTotalTime()){
          return 1;
        }
        return -1;
      }
    });
    ArrayList<Car> cars=getWinner().getCarList();
    String out="";
    for(int i=0 ; i<cars.size();i++){
      Car curr=cars.get(i);
      String hours=String.format("%02d", (int)curr.getTotalTime()/3600);
      String mins= String.format("%02d", ((int)curr.getTotalTime()/60)%60);
      String  secs= String.format("%02d", (int)curr.getTotalTime()%60);
      String msecs= String.format("%03d", ((int)(curr.getTotalTime()*1000))%1000);
       out+=curr.getDriverName()+"(" +curr.getCarNo()+"):  "+hours+':'+mins+':'+secs+':'+msecs+'\n';
    }
    return out;
    // Fill this method
  }
}
