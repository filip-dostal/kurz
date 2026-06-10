public class RGBA {
    public final static int CHANNEL_MASK = 0xFF;
    public final static int BITS_PER_CHANNEL = 8;
    public final static int RED_SHIFT = BITS_PER_CHANNEL * 2;
    public final static int GREEN_SHIFT = BITS_PER_CHANNEL;
    public final static int BLUE_SHIFT = 0;
    public final static int ALPHA_SHIFT = BITS_PER_CHANNEL * 3;

    public static void main(String[] args) {
        RGBA rgba = new RGBA(255, 255, 255);
        System.out.println(Integer.toHexString(rgba.getRGB()));
    }
    
    public static RGBA fromRGB(int rgb) {
        int red = (rgb >> RED_SHIFT) & CHANNEL_MASK;
        int green = (rgb >> GREEN_SHIFT) & CHANNEL_MASK;
        int blue = (rgb >> BLUE_SHIFT) & CHANNEL_MASK;
        return new RGBA(red, green, blue);
    }
    
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public RGBA(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public RGBA(int red, int green, int blue) {
        this(red, green, blue, 0);
    }

    public int getRGB() {
        return ((red & CHANNEL_MASK) << RED_SHIFT)
                | ((green & CHANNEL_MASK) << GREEN_SHIFT)
                | ((blue & CHANNEL_MASK) << BLUE_SHIFT);
    }

    public int getRGBA() {
        return ((alpha & CHANNEL_MASK) << ALPHA_SHIFT)
                | ((red & CHANNEL_MASK) << RED_SHIFT)
                | ((green & CHANNEL_MASK) << GREEN_SHIFT)
                | ((blue & CHANNEL_MASK) << BLUE_SHIFT);
    }
}
