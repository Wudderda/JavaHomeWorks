public abstract class Tire {

  protected double speed;
  protected double degradation;

  public double getSpeed() {
    return speed;
  }

  public double getDegradation() {
    return degradation;
  }

  abstract public void tick(TrackFeature f);
  abstract public int  getType();//0 = soft  1=medium  2=hardtire i could use enum but idk if it is allowed
}
