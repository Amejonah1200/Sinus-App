package ap.amejonah.sinusapp;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class SinusApp extends Application {
  
  public static void main(String[] args) {
    launch(args);
  }
  
  private Stage primaryStage;
  private AnimationController animationController;
  private List<GraphFunction> functions = new ArrayList<>();
  private int startSearchBoxes;
  
  @Override
  public void start(Stage stage) throws Exception {
    // Initialization of the functions
    functions.add(new SinusFunction("sin(x)", 1, 1, 0, new Color(1, 0.5, 0, 1)));
    functions.add(new SinusFunction("0.75*sin(x)", 0.75, 1, 0, new Color(1, 0, 1, 1)));
    functions.add(new SinusFunction("sin(x + PI/2)", 1, 1, Math.PI / 2, new Color(1, 0.5, 0, 1)));
    functions.add(new CosinusFunction("-cos(x)", -1, 1, 0, new Color(1, 0.5, 0, 1)));
    // ---
    primaryStage = stage;
    AnchorPane pane = new AnchorPane();
    pane.setPrefHeight(600);
    pane.setMinHeight(600);
    pane.setPrefWidth(1000);
    pane.setMinWidth(1000);
    // setting up global values
    int scaleGlobal = 125;
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
        funcLine = new Line(startX + x, startY - (function.f(xScale * x)) * scaleGlobal, startX + x + 1,
            startY - (function.f(xScale * x + xScale)) * scaleGlobal);
        funcLine.strokeWidthProperty().set(2);
        funcLine.setStroke(((GraphFunction) function).getColor());
        funcLine.setId("line:" + id);
        pane.getChildren().add(funcLine);
      }
      double coef = function instanceof GraphFunction ? ((GraphFunction) function).getCoefficientGlobal() : 1;
      functionArc = new Arc();
      functionArc.setRadiusX(scaleGlobal * coef);
      functionArc.setRadiusY(scaleGlobal * coef);
      functionArc.setStartAngle(0);
      functionArc.setLength(375);
      functionArc.setLayoutX(200);
      functionArc.setLayoutY(startY);
      functionArc.setFill(new Color(0, 0, 0, 0));
      functionArc.setStroke(((GraphFunction) function).getColor());
      pane.getChildren().add(functionArc);
      id++;
    }
    // ---
    // setting up the x and y axis for the graph
    Line xAxis = new Line(startX - 20, startY, startX + length - 1, startY);
    xAxis.setStroke(new Color(0.25, 0.25, 0.25, 1));
    pane.getChildren().add(xAxis);
    Line yAxis = new Line(startX, startY - scaleGlobal - 20, startX, startY + scaleGlobal + 20);
    yAxis.setStroke(new Color(0.25, 0.25, 0.25, 1));
    pane.getChildren().add(yAxis);
    // ---
    // setting up the main arc and the 90° arc
    Arc arc = new Arc();
    arc.setRadiusX(scaleGlobal);
    arc.setRadiusY(scaleGlobal);
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
    // ---------------------------
    // setting up the checkboxes for enabling and disabling the lines and arcs
    startSearchBoxes = pane.getChildren().size();
    CheckBox checkBox;
    for (int i1 = 0; i1 < functions.size(); i1++) {
      checkBox = new CheckBox(" -> Funktion: " + functions.get(i1).getName());
      checkBox.setId("box:" + i1);
      checkBox.setOnAction((event) -> {
        updateLines();
      });
      checkBox.setLayoutX(14);
      checkBox.setSelected(true);
      checkBox.setLayoutY(14 + 30 * i1);
      pane.getChildren().add(checkBox);
    }
    // ---
    // setting up the x and y axis for the main arc
    xAxis = new Line(200 - scaleGlobal - 20, startY, 200 + scaleGlobal + 20, startY);
    xAxis.setStroke(new Color(0.25, 0.25, 0.25, 1));
    pane.getChildren().add(xAxis);
    yAxis = new Line(200, startY - scaleGlobal - 20, 200, startY + scaleGlobal + 20);
    yAxis.setStroke(new Color(0.25, 0.25, 0.25, 1));
    pane.getChildren().add(yAxis);
    // ---
    // setting the scene and stage
    Scene scene = new Scene(pane);
    stage.setScene(scene);
    stage.sizeToScene();
    stage.setResizable(false);
    stage.setTitle("Zeigerdiagramm App");
    stage.show();
    // ---
    // creating the AnimationController and start it
    animationController = new AnimationController(this, startX, length, scaleGlobal, xScale);
    animationController.animate();
    // ---
    // setting that on close it must stop the ScheduledExcutorService
    stage.setOnCloseRequest((event) -> Runtime.getRuntime().exit(0));
    Runtime.getRuntime().addShutdownHook(new Thread() {
      
      public void run() {
        animationController.terminate();
      }
    });
    // ---
  }
  
  private void updateLines() {
    int lineId;
    boolean visible;
    for (Node node : ((AnchorPane) primaryStage.getScene().getRoot()).getChildren())
      if (node.getId() != null && node.getId().startsWith("line")) {
        lineId = Integer.parseInt(node.getId().split(":")[1]);
        visible = ((CheckBox) ((AnchorPane) primaryStage.getScene().getRoot()).getChildren().get(startSearchBoxes
            + lineId)).isSelected();
        node.setVisible(visible);
        animationController.setVisible(functions.size() - lineId - 1, visible);
      }
  }
  
  public Stage getPrimaryStage() {
    return primaryStage;
  }
  
  public List<GraphFunction> getFunctions() {
    return functions;
  }
}
