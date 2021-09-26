package assignment_8;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Yagn Parekh
 */
public class Circle extends GeometricObject {

    private double radius;

    public double getRadius() {
        return radius;
    }

    /**
     * @param x
     * @param y
     * @param fillColor
     * @param radius
     */
    public Circle(double x, double y, Color fillColor, double radius) {
        super(x, y, fillColor);
        this.radius = radius;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(getFillColor());
        gc.fillOval(getX() - radius, getY() - radius, radius * 2, radius * 2);
    }
}
