package ap.amejonah.sinusapp;

import javafx.scene.paint.Color;

public class SinusFunction extends GraphFunction {
  
  public SinusFunction(String name, double coefficientGlobal, double coefficientX, double addToX, Color color) {
    super(name, coefficientGlobal, coefficientX, addToX, color);
  }
  
  @Override
  public double f(double x) {
    return coefficientGlobal * Math.sin(coefficientX * x + addToX);
  }
}
