package assignment_8;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * author : Yagna Parekh
 */
public class PaintApp extends Application {

    Button circleButton, squareButton, drawButton, unDrawButton; // different buttons
    Label locationLabel, sizeLabel, ColorLabel, errorLabel; // appropriate labels
    TextField sizeField, locationXField, locationYField, colorRedField, colorBlueField, colorGreenField;
    private int xLocation, yLocation, size, redColor, greenColor, blueColor;
    private final int CANVAS_WIDTH = 800;
    private final int CANVAS_HEIGHT = 500;
    private String errorMessage;
    private ArrayList<GeometricObject> shapes ;
    private GraphicsContext gc;
    private GeometricObject geoShape;

    public PaintApp() {

        xLocation = 300;
        yLocation = 120;
        size = 5;
        redColor = 255;
        greenColor = 128;
        blueColor = 92;
        circleButton = new Button("Circle");
        squareButton = new Button("Square");
        locationLabel = new Label("Location");
        sizeLabel = new Label("Size");
        locationXField = new TextField(String.valueOf(xLocation));
        locationYField = new TextField(String.valueOf(yLocation));
        sizeField = new TextField(String.valueOf(size));
        ColorLabel = new Label("Color");
        colorRedField = new TextField(String.valueOf(redColor));
        colorBlueField = new TextField(String.valueOf(greenColor));
        colorGreenField = new TextField(String.valueOf(blueColor));
        drawButton = new Button("Draw");
        unDrawButton = new Button("UnDraw");
        errorLabel = null;
        shapes = new ArrayList<>();
        geoShape = new Circle(xLocation, yLocation, getColor(), size);
    }

    public void setErrorLabel(String text) {
        this.errorLabel.setText(text);
    }

    public Color getColor() {
        try {
            return Color.rgb(redColor, greenColor, blueColor, .99);
        }catch (Exception e){
            throw new InputMismatchException("Can not make RGB  " + e.getMessage());
        }
    }

    private void updatePropertiesFromControl(){
        xLocation = Integer.parseInt(locationXField.getText());
        yLocation = Integer.parseInt(locationYField.getText());
        size = Integer.parseInt(sizeField.getText());

        redColor = Integer.parseInt(colorRedField.getText());
        blueColor = Integer.parseInt(colorBlueField.getText());
        greenColor = Integer.parseInt(colorGreenField.getText());
    }

    private void circleButtonHandler(ActionEvent actionEvent) {
        geoShape = new Circle(xLocation, yLocation, getColor(), size);
    }

    private void squareButtonHandler(ActionEvent actionEvent) {
        geoShape = new Square(xLocation, yLocation, getColor(), size);
    }

    private void drawButtonHandler(ActionEvent actionEvent) {
        try {

            System.out.println(shapes.size());

            updatePropertiesFromControl();

            if(geoShape instanceof Circle){
                Circle geoCircle = new Circle(xLocation, yLocation, getColor(), size);
                shapes.add(geoCircle);
                geoCircle.draw(gc);
            }
            if(geoShape instanceof Square){
                Square geoSquare = new Square(xLocation, yLocation, getColor(), size);
                shapes.add(geoSquare);
                geoSquare.draw(gc);
            }
            setErrorLabel("");
        }catch (Exception e){
            setErrorLabel(e.getMessage());
        }
    }

    private void unDrawButtonHandler(ActionEvent actionEvent) {
        try {
            gc.setFill(Color.LIGHTYELLOW);
            gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

            shapes.remove(shapes.size() - 1);

            if(shapes.size() == 0){
                // throws an exception can;t undraw
                throw new Exception("Can not un draw, ArrayList is empty ");
            }

            for (GeometricObject geoShape: shapes) {
                if(geoShape instanceof Circle){
                    Circle geoCircle = (Circle) geoShape;
                    geoCircle.draw(gc);
                }
                if(geoShape instanceof Square){
                    Square geoSquare = (Square) geoShape;
                    geoSquare.draw(gc);
                }
            }

        } catch (Exception e) {
            setErrorLabel(e.getMessage());
        }
    }

    private void pressHandler(MouseEvent event) {
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
    }
    private void dragHandler(MouseEvent event) {

        gc.setStroke(getColor());
        gc.setLineWidth(size);
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        stage.setTitle("FX GUI Template"); // setting the window title here

        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT); // Set canvas Size in Pixels

        gc = canvas.getGraphicsContext2D();

        locationLabel.setPrefWidth(80);
        locationLabel.setPadding(new Insets(5));
        sizeLabel.setPrefWidth(50);
        sizeLabel.setPadding(new Insets(5));
        ColorLabel.setPrefWidth(50);
        ColorLabel.setPadding(new Insets(5));
        locationXField.setMaxWidth(60);
        locationYField.setMaxWidth(60);
        sizeField.setMaxWidth(60);
        colorRedField.setMaxWidth(60);
        colorBlueField.setMaxWidth(60);
        colorGreenField.setMaxWidth(60);
        HBox control = new HBox(circleButton, squareButton, locationLabel, locationXField, locationYField, sizeLabel, sizeField, ColorLabel, colorRedField, colorGreenField, colorBlueField, drawButton, unDrawButton);
        control.setPadding(new Insets(10));
        control.setStyle("-fx-background-color: lightgray;");

        
        errorLabel = new Label();
        errorLabel.relocate(0, 50);
        errorLabel.setPrefWidth(700);
        errorLabel.setPrefHeight(30);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setPadding(new Insets(2, 0, 0, 300));



        circleButton.setOnAction(this::circleButtonHandler);
        squareButton.setOnAction(this::squareButtonHandler);
        drawButton.setOnAction(this::drawButtonHandler);
        unDrawButton.setOnAction(this::unDrawButtonHandler);
        stage.setScene(scene);
        root.getChildren().addAll(canvas, control, errorLabel);

        gc.setFill(Color.LIGHTYELLOW);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //  Add Event Handlers and do final setup
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::pressHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::dragHandler);
        // 6. Show the stage
        stage.show();
    }

    /**
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}
