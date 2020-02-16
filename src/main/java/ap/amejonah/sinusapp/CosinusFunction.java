package ap.amejonah.sinusapp;

import javafx.scene.paint.Color;

public class CosinusFunction extends GraphFunction {
  
  public CosinusFunction(String name, double coefficientGlobal, double coefficientX, double addToX, Color color) {
    super(name, coefficientGlobal, coefficientX, addToX, color);
  }
  
  @Override
  public double f(double x) {
    return getCoefficientGlobal() * Math.cos(getCoefficientX() * x + getAddToX());
  }
}
