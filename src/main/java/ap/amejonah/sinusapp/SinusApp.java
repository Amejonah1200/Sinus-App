package ap.amejonah.sinusapp;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class SinusApp extends Application {
  
  public static void launchApp(String[] args) {
    launch(args);
  }
  
  private Stage primaryStage;
  private AnimationController animationController;
  private List<GraphFunction> functions = new ArrayList<>();
  
  @Override
  public void start(Stage stage) throws Exception {
    // Initialization of the functions
    functions.add(new SinusFunction("Stromstärke", 0.75, 1, 0, new Color(1, 0, 1, 1)));
    functions.add(new SinusFunction("Ohmscher Widerstand", 1, 1, 0, new Color(1, 0.5, 0, 1)));
    functions.add(new SinusFunction("Induktiver Widerstand", 1, 1, Math.PI / 2, new Color(1, 0.5, 0, 1)));
    functions.add(new SinusFunction("Kapazitiver Widerstand", 1, 1, Math.PI, new Color(1, 0.5, 0, 1)));
    // ---
    primaryStage = stage;
    AnchorPane pane = new AnchorPane();
    pane.setPrefHeight(600);
    pane.setMinHeight(600);
    pane.setPrefWidth(1000);
    pane.setMinWidth(1000);
    // setting up global values
    int yScale = 125;
    int startX = 450;
    int startY = 300;
    int length = 500;
    // x axis scaling, making that at: xScale * length = 2 PI
    // -> cos(2PI) = cos(0) and sin(2PI) = cos(0)
    double xScale = (2D * Math.PI) / length;
    // ---
    Line funcLine;
    Arc functionArc;
    int id = 0;
    // creating the lines and arcs:
    // length - 1 lines and 1 arc -> 500 children for 1 function
    for (SimpleMathFunction function : functions) {
      for (int x = 0; x < length - 1; x++) {
        funcLine = new Line(startX + x, startY - (function.f(xScale * x)) * yScale, startX + x + 1,
            startY - (function.f(xScale * x + xScale)) * yScale);
        funcLine.strokeWidthProperty().set(2);
        funcLine.setStroke(((GraphFunction) function).getColor());
        funcLine.setId("line:" + id);
        pane.getChildren().add(funcLine);
      }
      double coef = function instanceof GraphFunction ? ((GraphFunction) function).getCoefficientGlobal() : 1;
      functionArc = new Arc();
      functionArc.setRadiusX(yScale * coef);
      functionArc.setRadiusY(yScale * coef);
      functionArc.setStartAngle(0);
      functionArc.setLength(375);
      functionArc.setLayoutX(200);
      functionArc.setLayoutY(startY);
      functionArc.setFill(new Color(0, 0, 0, 0));
      functionArc.setStroke(((GraphFunction) function).getColor());
      functionArc.setId("arc:" + id);
      pane.getChildren().add(functionArc);
      id++;
    }
    // ---
    // setting up the x and y axis for the graph
    Line xAxis = new Line(startX - 20, startY, startX + length - 1, startY);
    xAxis.setStroke(new Color(0.25, 0.25, 0.25, 1));
    pane.getChildren().add(xAxis);
    Line yAxis = new Line(startX, startY - yScale - 20, startX, startY + yScale + 20);
    yAxis.setStroke(new Color(0.25, 0.25, 0.25, 1));
    pane.getChildren().add(yAxis);
    // ---
    // setting up the main arc and the 90° arc
    Arc arc = new Arc();
    arc.setRadiusX(yScale);
    arc.setRadiusY(yScale);
    arc.setStartAngle(0);
    arc.setLength(360);
    arc.setLayoutX(200);
    arc.setLayoutY(startY);
    arc.setFill(new Color(0, 0, 0, 0));
    arc.setStroke(new Color(0, 0, 0, 1));
    arc.strokeWidthProperty().set(2);
    pane.getChildren().add(arc);
    arc = new Arc();
    arc.setRadiusX(10);
    arc.setRadiusY(10);
    arc.setStartAngle(0);
    arc.setLength(90);
    arc.setLayoutX(200);
    arc.setLayoutY(startY);
    arc.setFill(new Color(0, 0, 0, 0));
    arc.setStroke(new Color(0.5, 0.5, 0.5, 1));
    arc.strokeWidthProperty().set(2);
    pane.getChildren().add(arc);
    // ---
    // ---------------------------
    // place for adding other children without affecting the animation
    pane.getChildren().add(createLabel("90°", 205, startY - 25, 1));
    pane.getChildren().add(createLabel("1", 205, startY - yScale - 20, 1.5D));
    pane.getChildren().add(createLabel("-1", 205, startY + yScale + 10, 1.5D));
    pane.getChildren().add(createLabel("1", 200 + yScale + 10, startY, 1.5D));
    pane.getChildren().add(createLabel("-1", 200 - yScale - 15, startY, 1.5D));
    pane.getChildren().add(createLabel("1", startX - 10, startY - yScale - 10, 1.5D));
    pane.getChildren().add(createLabel("0", startX - 10, startY, 1.5D));
    pane.getChildren().add(createLabel("π/2", startX + (length + 10) / 2, startY, 1.5D));
    pane.getChildren().add(createLabel("2π", startX + length + 5, startY, 1.5D));
    pane.getChildren().add(createLabel("-1", startX - 15, startY + yScale - 10, 1.5D));
    pane.getChildren().add(createButton("▶", 30, 955, 15, (event) -> {
      animationController.setRunning(!animationController.isRunning());
      if (animationController.isRunning()) ((Button) event.getSource()).setText("⏸");
      else ((Button) event.getSource()).setText("▶");
    }));
    // ---------------------------
    // setting up the checkboxes for enabling and disabling the lines and arcs
    CheckBox checkBox;
    for (int i1 = 0; i1 < functions.size(); i1++) {
      checkBox = new CheckBox(" -> " + functions.get(i1).getName());
      checkBox.setId("box:" + i1);
      checkBox.setOnAction((
          event) -> updateLines(Integer.parseInt(((CheckBox) event.getSource()).getId().split(":")[1]), ((CheckBox) event.getSource()).isSelected()));
      checkBox.setLayoutX(14);
      checkBox.setSelected(true);
      checkBox.setLayoutY(14 + 30 * i1);
      pane.getChildren().add(checkBox);
    }
    // ---
    // setting up the x and y axis for the main arc
    xAxis = new Line(200 - yScale - 20, startY, 200 + yScale + 20, startY);
    xAxis.setStroke(new Color(0.25, 0.25, 0.25, 1));
    pane.getChildren().add(xAxis);
    yAxis = new Line(200, startY - yScale - 20, 200, startY + yScale + 20);
    yAxis.setStroke(new Color(0.25, 0.25, 0.25, 1));
    pane.getChildren().add(yAxis);
    // ---
    // setting the scene and stage
    Scene scene = new Scene(pane);
    stage.setScene(scene);
    stage.sizeToScene();
    stage.setResizable(false);
    stage.setTitle("Phasenverschiebung im Wechselstromkreis");
    stage.show();
    // ---
    // creating the AnimationController and start it
    animationController = new AnimationController(this, startX, length, yScale, xScale);
    animationController.animate();
    // ---
    // setting that on close it must stop the ScheduledExcutorService
    stage.setOnCloseRequest((event) -> Runtime.getRuntime().exit(0));
    // ---
  }
  
  private Button createButton(String name, int length, int posX, int posY, EventHandler<ActionEvent> action) {
    Button button = new Button(name);
    button.setLayoutX(posX);
    button.setLayoutY(posY);
    button.setPrefSize(length, 25);
    button.setOnAction(action);
    return button;
  }
  
  private Label createLabel(String name, int layoutX, int layoutY, double scale) {
    Label tempLabel = new Label(name);
    tempLabel.setLayoutX(layoutX);
    tempLabel.setLayoutY(layoutY);
    tempLabel.setScaleX(scale);
    tempLabel.setScaleY(scale);
    return tempLabel;
  }
  
  private void updateLines(int id, boolean visible) {
    for (int i = id * 500; i < id * 500 + 500; i++) {
      ((AnchorPane) primaryStage.getScene().getRoot()).getChildren().get(i).setVisible(visible);
    }
    animationController.setVisible(functions.size() - id - 1, visible);
  }
  
  public Stage getPrimaryStage() {
    return primaryStage;
  }
  
  public List<GraphFunction> getFunctions() {
    return functions;
  }
}
