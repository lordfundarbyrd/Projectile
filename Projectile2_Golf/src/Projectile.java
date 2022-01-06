
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import processing.core.PApplet;

public class Projectile extends PApplet {

    public static List<Ball> balls = new ArrayList<>();
    static float size = 100;
    float angle;
    float length;
    float horizSpeed = 120;
    Ball best;

    float beg = 0;
    float end = 90;
    int index;
    int gen = 1;
    float diff = 120;
    float wd = ((float) (5 - (2 * Math.random())));

    boolean last;

    public static void main(String[] args) {
        PApplet.main(new String[]{Projectile.class.getName()});
    }

    public void balls() {
        if (beg != end) {
            for (int i = 0; i < size; i++) {
                balls.add(new Ball(this, 0, 30, (float) ThreadLocalRandom.current().nextDouble(beg, end), wd));
            }
        }
        if (beg == end) {
            last = true;
            for (int i = 0; i < size; i++) {
                balls.add(new Ball(this, 0, 30, (float) beg, wd));
            }
        }
    }

    public void setup() {
        balls();
    }

    public void settings() {
        size(1000, 1000);
    }

    public void reset() {
        if (landed() == true) {
            sort();
            best();
            if (beg != end) {
                setRange();
                balls.clear();
            }
            balls();
        }
    }

    public void sort() {
        for (int i = 0; i < balls.size(); i++) {
            for (int j = balls.size() - 1; j > i; j--) {
                if (balls.get(i).angle > balls.get(j).angle) {
                    Ball tmp = balls.get(i);
                    balls.set(i, balls.get(j));
                    balls.set(j, tmp);
                }
            }
        }
    }

    public void draw() {
        background(201, 233, 246);
        fill(0);
        textSize(30);
        text("Gen: " + gen, width - 400, height / 2 + 270);
        text("Height: " + Ball.h + " m", width - 400, height / 2 + 90);
        text("Velocity: " + Ball.initVel + " m/s", width - 400, height / 2);
        text("Range: " + length + " m", width - 400, height / 2 - 90);
        text("Angle: " + angle + " deg", width - 400, height / 2 - 180);
        text("Wind:" + wd + " m/s", width - 400, height / 2 + 180);

        for (Ball b : balls) {
            b.render();
            b.update();
        }

        if (last == true) {
            if (landed() == true) {
                fill(255, 0, 0);
                textSize(30);
                text("Gen: " + (gen - 1), width - 400, height / 2 + 270);
                text("Height: " + Ball.h + " m", width - 400, height / 2 + 90);
                text("Velocity: " + Ball.initVel + " m/s", width - 400, height / 2);
                text("Range: " + length + " m", width - 400, height / 2 - 90);
                text("Angle: " + angle + " deg", width - 400, height / 2 - 180);
                text("Wind:" + wd + " m/s", width - 400, height / 2 + 180);
                noLoop();
            }
        }
        reset();
    }

    public boolean landed() {
        for (int i = 0; i < balls.size(); i++) {
            if (balls.get(i).landed == false) {
                return false;
            }
        }
        gen++;
        return true;
    }

    public void best() {
        for (Ball b : balls) {
//            if (abs(120 - b.range) < diff) {
//                best = b;
//                diff = abs(120 - b.range);
//                length = b.range;
//                angle = b.angle;
//            }

            if (b.range > length) {
                best = b;
                length = b.range;
                angle = b.angle;
            } 
        }
    }

    public void setRange() {
        index = balls.indexOf(best);
        if (index <= 3) {
            beg = balls.get(index + 3).angle;
        }
        if (index > 3) {
            beg = balls.get(index - 3).angle;
        }
        if (index >= balls.size() - 3) {
            end = balls.get(index - 3).angle;
        }
        if (index < balls.size() - 3) {
            end = balls.get(index + 3).angle;
        }
    }
}
