package ap.amejonah.sinusapp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class AnimationController {
  
  private SinusApp app;
  private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
  private final int START_X;
  private int xPos = -1, length, scale;
  private double xScale;
  
  public AnimationController(SinusApp app, int START_X, int length, int scale, double xScale) {
    this.app = app;
    this.START_X = START_X;
    this.length = length;
    this.scale = scale;
    this.xScale = xScale;
  }
  
  public void animate() {
    ObservableList<Node> children = ((AnchorPane) app.getPrimaryStage().getScene().getRoot()).getChildren();
    for (SimpleMathFunction function : app.getFunctions())
      addHelpLines(children, ((GraphFunction) function).getColor());
    executorService.scheduleAtFixedRate(() -> {
      Platform.runLater(() -> {
        if (xPos >= length - 1) xPos = 0;
        else xPos++;
        for (int i = 0; i < app.getFunctions().size(); i++)
          updateFuctionHelpLines(xScale * xPos, i * 3
              + 1, children, app.getFunctions().get(app.getFunctions().size() - i - 1));
      });
    }, 25, 25, TimeUnit.MILLISECONDS);
  }
  
  private void addHelpLines(ObservableList<Node> children, Color color) {
    Line line = new Line(200 + scale, 300, START_X, 300);
    line.setStroke(color);
    children.add(line);
    line = new Line(200, 300, 200 + scale, 300);
    line.setStroke(color);
    children.add(line);
    line = new Line(START_X, 300, START_X, 300);
    line.setStroke(color);
    children.add(line);
  }
  
  private void updateFuctionHelpLines(double functionX, int startIndex, ObservableList<Node> children,
      SimpleMathFunction function) {
    double tempScale = 1;
    double x = functionX;
    if (function instanceof GraphFunction) {
      tempScale = ((GraphFunction) function).getCoefficientGlobal();
      x += ((GraphFunction) function).getAddToX();
    }
    Line tempLine = (Line) children.get(children.size() - startIndex);
    tempLine.setStartX(START_X + xPos);
    tempLine.setEndX(START_X + xPos);
    tempLine.setEndY(300 - function.f(functionX) * scale);
    children.set(children.size() - startIndex, tempLine);
    tempLine = (Line) children.get(children.size() - (startIndex + 1));
    tempLine.setEndX(200 + (Math.cos(x)) * scale * tempScale);
    tempLine.setEndY(300 - Math.sin(x) * scale * tempScale);
    children.set(children.size() - (startIndex + 1), tempLine);
    tempLine = (Line) children.get(children.size() - (startIndex + 2));
    tempLine.setStartX(200 + (Math.cos(x)) * scale * tempScale);
    tempLine.setStartY(300 - Math.sin(x) * scale * tempScale);
    tempLine.setEndX(START_X + xPos);
    tempLine.setEndY(300 - function.f(functionX) * scale);
    children.set(children.size() - (startIndex + 2), tempLine);
  }
  
  public void terminate() {
    // Shutdown the executorservice for program stop.
    executorService.shutdown();
  }
  
  public void setVisible(int index, boolean visible) {
    ObservableList<Node> children = ((AnchorPane) app.getPrimaryStage().getScene().getRoot()).getChildren();
    Line tempLine = (Line) children.get(children.size() - (index * 3 + 1));
    tempLine.setVisible(visible);
    children.set(children.size() - (index * 3 + 1), tempLine);
    tempLine = (Line) children.get(children.size() - (index * 3 + 2));
    tempLine.setVisible(visible);
    children.set(children.size() - (index * 3 + 2), tempLine);
    tempLine = (Line) children.get(children.size() - (index * 3 + 3));
    tempLine.setVisible(visible);
    children.set(children.size() - (index * 3 + 3), tempLine);
  }
}
