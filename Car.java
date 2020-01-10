public class Car {

  private int carNo;
  private String driverName;
  private double totalTime;
  private Tire tire;
  //private int pit_stop; // the number of pit stops that is done. Starts with 0
  public Car() {
    // Fill this method
  }

  public Car(String driverName, int carNo, Tire tire) {
    // Fill this method
    this.driverName=driverName;
    this.carNo=carNo;
    this.tire=tire;
    this.totalTime=0;
  }

  public Tire getTire() {
    return tire;
  }

  public void setTire(Tire tire) {
    this.tire = tire;
  }

  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }

  public int getCarNo() {
    return carNo;
  }

  public void setCarNo(int carNo) {
    this.carNo = carNo;
  }

  public double getTotalTime() {
    return totalTime;
  }


  public void tick(TrackFeature feature) {
    double random=Math.random();
    totalTime+=(feature.getDistance()/getTire().getSpeed())+random;
    getTire().tick(feature);
    if(getTire().getDegradation()>70){//pit stop
        if(getTire().getType()==0){
          totalTime+=25;
          setTire(new MediumTire());
        }
        else{
          totalTime+=25;
          setTire(new SoftTire());
        }
    }
    // Fill this method
  }
}
