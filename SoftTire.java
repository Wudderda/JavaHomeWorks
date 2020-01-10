public class SoftTire extends Tire { // extends means inherits from tire

  public SoftTire() {
    this.speed = 350;
    this.degradation = 0;
  }
  public void tick(TrackFeature track){
    this.degradation+=1.2*track.getRoughness()*track.getMultiplier();
    double degrade=75.0;
    if(degradation<75) {
     degrade=degradation;
    }
    if(speed>=100){
      speed-=degrade*0.25;
    }
  }
  public int getType(){
    return 0;
  }
}
