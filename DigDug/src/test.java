import java.awt.*;
import java.util.HashSet;

public class test {
    public static void main(String[] args) {
        HashSet<Rectangle> set = new HashSet<Rectangle>();
        Rectangle rectangle1 = new Rectangle(300, 300, 100, 50);
        Rectangle rectangle2 = new Rectangle(400, 400, 100, 50);
        Rectangle rectangle3 = new Rectangle(500, 500, 100, 50);
        set.add(rectangle1);
        set.add(rectangle2);
        set.add(rectangle3);
        System.out.println(set.contains(new Rectangle(400, 400, 100, 50)));
    }


}
