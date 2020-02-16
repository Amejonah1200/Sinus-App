package ap.amejonah.sinusapp;

import javafx.scene.paint.Color;

public abstract class GraphFunction implements SimpleMathFunction {
  
  private String name;
  double coefficientGlobal;
  double coefficientX;
  double addToX;
  private Color color;
  
  public GraphFunction(String name, double coefficientGlobal, double coefficientX, double addToX, Color color) {
    this.name = name;
    this.coefficientGlobal = coefficientGlobal;
    this.coefficientX = coefficientX;
    this.addToX = addToX;
    this.color = color;
  }
  
  public String getName() {
    return name;
  }
  
  public double getCoefficientGlobal() {
    return coefficientGlobal;
  }
  
  public double getCoefficientX() {
    return coefficientX;
  }
  
  public double getAddToX() {
    return addToX;
  }
  
  public Color getColor() {
    return color;
  }
}
