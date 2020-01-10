import java.util.ArrayList;

public class Track {

  private String trackName;
  private ArrayList<TrackFeature> featureList;
  private boolean isClockwise;
  private int current_feature; // default value of -1

  public Track() {
    current_feature=-1;
  }

  public Track(String trackName, ArrayList<TrackFeature> featureList, boolean isClockwise) {
    this.trackName=trackName;
    this.featureList=featureList;
    this.isClockwise=isClockwise;
    this.current_feature=-1;
  }

  public String getTrackName() {
    return trackName;
  }

  public void setTrackName(String trackName) {
    this.trackName = trackName;
  }

  public ArrayList<TrackFeature> getFeatureList() {
    return featureList;
  }

  public void setFeatureList(ArrayList<TrackFeature> featureList) {
    this.featureList = featureList;
  }

  public boolean isClockwise() {
    return isClockwise;
  }

  public void setClockwise(boolean clockwise) {
    isClockwise = clockwise;
  }

  public int getTrackLength() {
    return featureList.size();
    // Fill this method
  }

  public TrackFeature getNextFeature() {
    // Fill this method
    int size= featureList.size();
    current_feature=(current_feature+1)%size;
    return featureList.get(current_feature);

  }

  public void addFeature(TrackFeature feature) {
    featureList.add(feature);
  }
  public int turnValue(TurnDirection turn){ // values sums up to 4 it is clockvise
    if(turn==TurnDirection.STRAIGHT){
      return 0;
    }
    if(turn==TurnDirection.LEFT){
      return -1;
    }
    return 1; // right turn
  }

  public boolean isValidTrack() {
    if(featureList.get(0).getTurnDirection()!=TurnDirection.STRAIGHT || featureList.get(getTrackLength()-1).getTurnDirection()!=TurnDirection.STRAIGHT){
      return false;
    }
    int sum=0;
    for(int i=0; i<getTrackLength();i++){
        sum+=turnValue(featureList.get(i).turnDirection);
    }
    if(sum==4 && isClockwise){
      return true;
    }
    if(sum==-4 && !isClockwise){
      return true;
    }
    return false;
    // Fill this method
  }
}
