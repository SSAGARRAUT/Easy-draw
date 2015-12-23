package application;


import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Draw2 extends Application {
	final static int CANVAS_WIDTH = 800-20 ;
    final static int CANVAS_HEIGHT = 600-230;
    
	 DropShadow shadow = new DropShadow();
    private Path path;
    private Group lineGroup;
    private static final Double DEFAULTSTROKE = 3.0;
    private static final Double MAXSTROKE = 30.0;
    private static final Double MINSTROKE = 1.0;
    
   
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Easy Draw");
        final Group root = new Group();
        
        Scene scene = new Scene(root, 800, 600,Color.ALICEBLUE);

        Button Graph = new Button();
        Graph.setText("Clear");
        Graph.setEffect(shadow);
        Graph.setStyle("-fx-font: 20 arial; -fx-base: #ee2211;");
        
        Graph.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                
            	
            }
        });


        
        
        
        lineGroup = new Group();
    Button Clear = new Button();
        Clear.setText("Clear");
        Clear.setEffect(shadow);
        Clear.setStyle("-fx-font: 20 arial; -fx-base: #ee2211;");
        
        Clear.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                lineGroup.getChildren().removeAll(lineGroup.getChildren());
            }
        });

        
              
        
        
        Slider Slider = new Slider(MINSTROKE, MAXSTROKE, DEFAULTSTROKE);
        Label Stroke = new Label("Stroke Width");
        Stroke.setEffect(shadow);
        Stroke.setStyle("-fx-font: 20 arial; -fx-base: #ee2211;");
     ////
       
        
        final Line sampleLine = new Line(0, 0, 140, 0);
        sampleLine.setSmooth(true);
        sampleLine.setStrokeLineCap(StrokeLineCap.ROUND);
        sampleLine.strokeWidthProperty().bind(Slider.valueProperty());
        sampleLine.setStroke(Color.BLACK);
        StackPane stackpane = new StackPane();
        stackpane.setPrefHeight(MAXSTROKE);
        stackpane.setPrefWidth(sampleLine.getEndX() + MAXSTROKE);
        stackpane.setAlignment(Pos.CENTER);
        stackpane.getChildren().add(sampleLine);

        
        final Label colorLabel = new Label("Black");
        colorLabel.setEffect(shadow);
        colorLabel.setStyle("-fx-font: 20 arial; -fx-base: #ee2211;");
    
        StackPane stackpane2 = new StackPane();
        stackpane2.setPrefHeight(MAXSTROKE);
        stackpane2.setPrefWidth(sampleLine.getEndX() + MAXSTROKE);
        stackpane2.setAlignment(Pos.CENTER_LEFT);
        stackpane2.getChildren().add(colorLabel);

             
        FlowPane flow = new FlowPane();
        Label Select = new Label("Select color");
        Select.setEffect(shadow);
        Select.setStyle("-fx-font: 20 arial; -fx-base: #ee2211;");
         
        flow.setVgap(2);
        flow.setHgap(2);
        flow.setPrefWrapLength(400);
        Field[] colorFields = Color.class.getDeclaredFields();
        for (Field fieldname : colorFields) {
            int mods = fieldname.getModifiers();

            
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)
                    && !(fieldname.getName().equals("TRANSPARENT"))) {
                try {

                    Color c = Color.web(fieldname.getName());
                 
                    final Rectangle r = new Rectangle(15, 15, c);
                    
                    r.setCursor(Cursor.HAND);
                    Tooltip t = new Tooltip(fieldname.getName().toLowerCase());
                    Tooltip.install(r, t);
                    r.setUserData(t.getText());
                    r.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent me) {
                            sampleLine.setStroke(r.getFill());
                            
                            colorLabel.setText(  ((String) r.getUserData()));
                        }
                    });
                    flow.getChildren().add(r);
                } catch (IllegalArgumentException e) {
                 }
            }
        }       
       
        
       // final Rectangle canvas = new Rectangle(scene.getWidth() - 20, scene.getHeight() - 230);
        final Rectangle canvas = new Rectangle(CANVAS_WIDTH,CANVAS_HEIGHT);
        canvas.setCursor(Cursor.CROSSHAIR);
        canvas.setFill(Color.WHITE);
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent me) {

                path = new Path();
                path.setMouseTransparent(true);
                path.setStrokeWidth(sampleLine.getStrokeWidth());
                path.setStroke(sampleLine.getStroke());
                path.setSmooth(true);
                BoxBlur  blur = new BoxBlur(); 
              //  blur.setWidth(3);
              //  blur.setHeight(3);
               //blur.setIterations(1);
               path.setEffect(blur);
             
                
                lineGroup.getChildren().add(path);
                path.getElements().add(new MoveTo(me.getSceneX(), me.getSceneY()));
            }
        });

        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent me) {
                path = null;

            }
        });

        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent me) {

                
                if (canvas.getBoundsInLocal().contains(me.getX(), me.getY())) {
                    path.getElements().add(new LineTo(me.getSceneX(), me.getSceneY()));
                }

            }
        });
        Button buttonSave = new Button("Save");
        buttonSave.setEffect(shadow);
        buttonSave.setStyle("-fx-font: 20 arial; -fx-base: #ee2211;");
       
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                 
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
                fileChooser.getExtensionFilters().add(extFilter);
               
                //Show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);
                 
                if(file != null){
                    try {
                    	
                        WritableImage writableImage = new WritableImage(CANVAS_WIDTH,CANVAS_HEIGHT);
                        canvas.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", file);
                    } catch (IOException ex) {
                        Logger.getLogger(Draw2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
             
        }); 
        
        
        VBox utilBox = new VBox(10);
        utilBox.setAlignment(Pos.TOP_CENTER);
        utilBox.getChildren().addAll(Clear, Stroke,Slider);

        HBox toolBox = new HBox(10);
        toolBox.setAlignment(Pos.TOP_CENTER);
        toolBox.getChildren().addAll(utilBox,Select, flow);
        
        HBox lineBox = new HBox(20);
        
        
        lineBox.getChildren().addAll(buttonSave,stackpane, stackpane2);


          
        VBox vb = new VBox(20);
        vb.setLayoutX(10);
        vb.setLayoutY(20);
        vb.getChildren().addAll(lineBox,canvas, toolBox);
       
       
        root.getChildren().addAll(vb, lineGroup);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
