public class Matrix {
    public static void main(String[] args) {
        Matrix m = new Matrix(3, 2, 2, 2 ,2 ,2 );
        System.out.println(m);

        System.out.println("scale3D 2: ");
        System.out.println(scale2D(2));

        System.out.println("translate2D 1, 2: ");
        System.out.println(translate2D(1, 2));

        System.out.println("rotate2D pi / 2: ");
        System.out.println(rotate2D(Math.PI / 2));


    }

    private final int rows;
    private final int cols;
    private final double[][] value;

    public Matrix(int rows, int cols, double... values) {
        this.rows = rows;
        this.cols = cols;
        this.value = new double[rows][cols];
        for (int i = 0; i < values.length; i++) {
            this.value[i / cols][i % cols] = values[i];
        }
    }

    public static Matrix scale2D(double sx, double sy) {
        return new Matrix(3, 3,
                    sx, 0, 0,
                            0, sy, 0,
                            0, 0, 1);
    }

    public static Matrix scale2D(double s) {
        return scale2D(s, s);
    }

    public static Matrix translate2D(double tx, double ty) {
        return new Matrix(3, 3,
                    1, 0, tx,
                            0, 1, ty,
                            0, 0, 1);
    }

    public static Matrix rotate2D(double angle) {
        return new Matrix(3, 3,
                    Math.cos(angle), -Math.sin(angle), 0,
                            Math.sin(angle), Math.cos(angle), 0,
                            0, 0, 1);
    }

    public static Matrix rotate2D(double angle, Point origin) {
        return rotate2D(angle, origin.getX(), origin.getY());
    }

    public static Matrix rotate2D(double angle, double x, double y) {
        return translate2D(x, y).multiply(rotate2D(angle)).multiply(translate2D(-x, -y));
    }

    public Matrix multiply(double scalar) {
        Matrix m = new Matrix(this.rows, this.cols);
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                m.value[row][col] = this.value[row][col] * scalar;
            }
        }
        return m;
    }

    public Matrix multiply(Matrix m) {
        if (this.cols != m.rows) {
            throw new ArithmeticException();
        }

        Matrix n = new Matrix(this.rows, m.cols);
        for (int row = 0; row < n.rows; row++) {
            for (int col = 0; col < n.cols; col++) {
                double sum = 0;
                for (int i = 0; i < this.cols; i++) {
                    sum += this.value[row][i] * m.value[i][col];
                }
                n.value[row][col] = sum;
            }
        }
        return n;
    }

    public Matrix point2D(double x, double y) {
        return new Matrix(3, 1, x, y, 1);
    }

    public Point transform(Point point) {
        Matrix m = multiply(point2D(point.getX(), point.getY()));
        return new Point(m.value[0][0], m.value[1][0]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            if (i > 0) {
                sb.append(System.lineSeparator());
            }
            sb.append('[');
            for (int j = 0; j < cols; j++) {
                if (j > 0) {
                    sb.append(", ");
                }
                sb.append(value[i][j]);
            }
            sb.append(']');
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}
