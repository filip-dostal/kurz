public class Point {
    public static void main(String[] args) {
        Point a = new Point(2, 3);
        System.out.println(a);

        System.out.println(a.move(3, 5));

        Point b = new Point(2, 3);
        Vector u = new Vector(3, 5);
        System.out.println(a.move(u));
    }

    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point move(double deltaX, double deltaY) {
        return new Point(this.x + deltaX, this.y + deltaY);
    }

    public Point move(Vector vector) {
        return move(vector.getX(), vector.getY());
    }

    public Point rotate(double angle) {
        double r = Math.sqrt(x * x + y * y);
        double a = Math.acos(x / r);
        a += angle;
        double x = r * Math.cos(a);
        double y = r * Math.sin(a);
        return new Point(x, y);
    }

    public Point rotate(Point origin, double angle) {
        Point p1 = this.move(-origin.getX(), -origin.getY());
        Point p2 = p1.rotate(angle);
        return p2.move(origin.getX(), origin.getY());
    }

    public Point scale(double factor) {
        return new Point(this.x * factor, this.y * factor);
    }

    public Point scale(Point origin, double factor) {
        Point p1 = this.move(-origin.getX(), -origin.getY());
        Point p2 = p1.scale(factor);
        return p2.move(origin.getX(), origin.getY());
    }

    @Override
    public String toString() {
        return String.format("[%.1f; %.1f]", x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
