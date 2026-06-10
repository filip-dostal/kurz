import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Scene1 extends BasicScene {
    public static void main(String[] args) {
        setAxis();
        show(new Scene1());
    }

    private final static int MOUNTAINS_Y = 9;
    private final static int BOAR_MAX_X = 10;
    private final static int BOAR_MIN_X = -10;

    private final List<Point> house = new ArrayList<>();
    private List<Point> houseTranslated = new ArrayList<>();
    private double boarX = -5;
    private double boarDirection = 1;
    private float sunAlpha = 0.3f;
    private Timer animationTimer;

    private double boar2X = -8;
    private double boar2Y = -2;
    private double boar2DirX = 1;
    private double boar2DirY = 1;

    private double boar3X = -8;
    private double boar3Y;
    private double boar3Direction = 1;

    public Scene1() {
        house.add(new Point(1, 3));
        house.add(new Point(1, 1));
        house.add(new Point(3, 1));
        house.add(new Point(3, 3));
        house.add(new Point(2, 4));
        house.add(new Point(1, 3));
        house.add(new Point(3, 3));



        startAnimation();
    }

    private void startAnimation() {
        animationTimer = new Timer(50, e -> {
            // Sun
            sunAlpha += 0.01f;
            if (sunAlpha > 1.0f) {
                sunAlpha = 0.3f;
            }
            repaint();

            // Boar 1
            boarX += 0.1 * boarDirection;
            if (boarX > BOAR_MAX_X) boarDirection = -1;
            if (boarX < BOAR_MIN_X) boarDirection = 1;

            // Boar 2
            boar2X += 0.07 * boar2DirX;
            boar2Y += 0.03 * boar2DirY;
            if (boar2X > 8)  boar2DirX = -1;
            if (boar2X < -8) boar2DirX = 1;
            if (boar2Y > 3)  boar2DirY = -1;
            if (boar2Y < -3) boar2DirY = 1;
            repaint();

            // Boar 3
            boar3X += 0.1 * boar3Direction;

            if (boar3X > 8)  boar3Direction = -1;
            if (boar3X < -8) boar3Direction = 1;

            boar3Y = 0.05 * Math.pow(boar3X, 2) - 3;

            repaint();
        });
        animationTimer.start();
    }

    private static List<Integer> imageToColors(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        List<Integer> colors = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                colors.add(image.getRGB(col, row));
            }
        }

        return colors;
    }

    private static List<Point> imageToPoints(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        List<Point> result = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result.add(new Point(col, row));
            }
        }

        return result;
    }

    private static BufferedImage pointsToImage(List<Point> points, List<Integer> colors, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            int col = (int) Math.round(p.getX());
            int row = (int) Math.round(p.getY());
            if (col >= 0 && col < width && row >= 0 && row < height) {
                result.setRGB(col, row, colors.get(i));
            }
        }

        return result;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // must be first

        // Background
        g.drawImage(readImage("e_1_3_wall_3.png"), pointToX(0), pointToY(MOUNTAINS_Y), null);
        g.drawImage(readImage("e_1_3_wall_3.png"), 0, pointToY(MOUNTAINS_Y), null);
        g.drawImage(readImage("e_1_3_ground_7.png"), pointToX(0), pointToY(MOUNTAINS_Y - 4), null);
        g.drawImage(readImage("e_1_3_ground_7.png"), 0, pointToY(MOUNTAINS_Y - 4), null);
        g.drawImage(readImage("e_1_1_ground_grass1.png"), pointToX(0), pointToY(MOUNTAINS_Y - 14), null);
        g.drawImage(readImage("e_1_1_ground_grass1.png"), 0, pointToY(MOUNTAINS_Y - 14), null);
        Graphics2D g2 = (Graphics2D) g;
        Composite originalComposite = g2.getComposite();

        // Mountains
        try {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.drawImage(readImage("clouds-2.png"), 0, pointToY(MOUNTAINS_Y), null);
        } finally {
            g2.setComposite(originalComposite);
        }

        // Sun
        try {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, sunAlpha));
            g2.drawImage(readImage("sky_sun.png"), pointToX(10), pointToY(7), null);
        } finally {
            g2.setComposite(originalComposite);
        }

        // Boars
        drawBoar(g2, boarX, 0, boarDirection);
        drawBoar(g2, boar2X, boar2Y, boar2DirX);
        drawBoar(g2, boar3X, boar3Y, boar3Direction);

        // Wheel
        drawRotated(g2, "E_1_1_object_wheel.png", 0, pointToY(-1), Math.PI);

        // Flower
        drawRotated(g2, "e_1_1_object_flower1.png", pointToX(5), pointToY(-1), Math.PI / 2);
    }

    private void drawBoar(Graphics2D g2, double worldX, double worldY, double directionX) {
        BufferedImage boar = readImage("boar_ER.png");
        if (boar != null) {
            int w = boar.getWidth();
            int h = boar.getHeight();

            Matrix matrix = directionX > 0
                    ? Matrix.scale2D(1, 1)
                    : Matrix.translate2D(w, 0).multiply(Matrix.scale2D(-1, 1));

            List<Point> points = imageToPoints(boar);
            List<Integer> colors = imageToColors(boar);
            List<Point> transformed = transform(matrix, points);
            BufferedImage result = pointsToImage(transformed, colors, w, h);

            g2.drawImage(result, pointToX(worldX), pointToY(worldY), null);
        }
    }

    private void drawRotated(Graphics2D g2, String fileName, int x, int y, double angle) {
        BufferedImage img = readImage(fileName);
        if (img != null) {
            int w = img.getWidth();
            int h = img.getHeight();

            Matrix matrix = Matrix.rotate2D(angle, w / 2.0, h / 2.0);

            List<Point> points = imageToPoints(img);
            List<Integer> colors = imageToColors(img);
            List<Point> transformed = transform(matrix, points);
            BufferedImage result = pointsToImage(transformed, colors, w, h);

            g2.drawImage(result, x, y, null);
        }
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
