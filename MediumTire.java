public class MediumTire extends Tire {

  public MediumTire() {
    this.speed = 310;
    this.degradation = 0;
  }
  public void tick(TrackFeature track){
    this.degradation+=1.1*track.getRoughness()*track.getMultiplier();
    double degrade=75;
    if(degradation<75) {
      degrade=degradation;
    }
    if(speed>=100){
      speed-=degrade*0.25;
    }
  }
  public int getType(){
    return 1;
  }
}
