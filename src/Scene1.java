import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene1 extends BasicScene {
    public static void main(String[] args) {
        setAxis();
        show(new Scene1());
    }

    private final static int MOUNTAINS_Y = 9;
    private final List<Point> house = new ArrayList<>();
    private List<Point> houseTranslated = new ArrayList<>();

    public Scene1() {
        house.add(new Point(1, 3));
        house.add(new Point(1, 1));
        house.add(new Point(3, 1));
        house.add(new Point(3, 3));
        house.add(new Point(2, 4));
        house.add(new Point(1, 3));
        house.add(new Point(3, 3));

        Matrix transform = Matrix.rotate2D(Math.PI / 4, 2, 2).multiply(Matrix.scale2D(2));

        houseTranslated = transform(transform, house);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(readImage("e_1_3_wall_3.png"), pointToX(0), pointToY(MOUNTAINS_Y), null);
        g.drawImage(readImage("e_1_3_wall_3.png"), 0, pointToY(MOUNTAINS_Y), null);
        g.drawImage(readImage("e_1_3_ground_7.png"), pointToX(0), pointToY(MOUNTAINS_Y - 4), null);
        g.drawImage(readImage("e_1_3_ground_7.png"), 0, pointToY(MOUNTAINS_Y - 4), null);
        g.drawImage(readImage("e_1_1_ground_grass1.png"), pointToX(0), pointToY(MOUNTAINS_Y - 14), null);
        g.drawImage(readImage("e_1_1_ground_grass1.png"), 0, pointToY(MOUNTAINS_Y - 14), null);
        Graphics2D g2 = (Graphics2D) g;
        Color originalColor = g2.getColor();
        Composite originalComposite = g2.getComposite();
        try {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
            g2.drawImage(readImage("clouds-2.png"), 0, pointToY(MOUNTAINS_Y), null);
        } finally {
            g2.setComposite(originalComposite);
            g2.setColor(originalColor);
        }
        paintHouse(g);
        paintHouseTranslated(g);
    }
    private void paintLeft(Graphics g) {
        g.drawImage(readImage("boar_ER.png"), pointToX(0), pointToY(0), null);
    }

    private void paintRights(Graphics g) {
        g.drawImage(readImage("insect_RE.png"), 200, 300, null);
    }

    private List<Point> transform(Matrix transform, List<Point> points) {
        List<Point> result = new ArrayList<>(points.size());
        for (Point p : points) {
            result.add(transform.transform(p));
        }
        return result;
    }

    private void paintPollyLine(Graphics g, List<Point> points) {
        for (int i = 1; i < points.size(); i++) {
            Point from = points.get(i - 1);
            Point to = points.get(i);
            int x1 = pointToX(from.getX());
            int y1 = pointToY(from.getY());
            int x2 =  pointToX(to.getX());
            int y2 =  pointToY(to.getY());
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private void paintHouse(Graphics g) {
        paintPollyLine(g, house);
    }

    private void paintHouseTranslated(Graphics g) {
        Color originalColor = g.getColor();
        g.setColor(Color.RED);
        try {
            paintPollyLine(g, houseTranslated);
        } finally {
            g.setColor(originalColor);
        }
    }
}
