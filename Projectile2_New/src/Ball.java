import processing.core.PApplet;
import processing.core.PVector;

public class Ball {

    float angle;
    static float initVel;
    static float h;
    float range;

    PVector force;
    PVector pos;
    PVector vel;
    PVector acc;
    PVector drag;
    PVector g;
    PVector wind;
    PVector dragVel;
    PVector magnus;
    PVector spin;

    PApplet parent;

    float m;
    float dT;
    float initX;
    float r;
    float c;
    float a;
    float p;
    float s;

    float coeff;
    
    boolean landed;

    public Ball(PApplet pa, int x, int y, float ang) {
        parent = pa;

        initX = x;
        initVel = 64;

        angle = ang;
        h = y;

        pos = new PVector(x, y);
        vel = new PVector(initVel * PApplet.cos(PApplet.radians(angle)), initVel * PApplet.sin(PApplet.radians(angle)));
        acc = new PVector(0, -9.8F, 0);
        g = new PVector(0, -9.8F, 0);
        force = new PVector();
        drag = new PVector();
        wind = new PVector(8,0,0);
        dragVel = new PVector();
        magnus = new PVector();
        spin = new PVector(0,0,150);

        m = .04575F;
        c = 0.2F;
        r = .0213F;
        a = (float) ((float) Math.PI * Math.pow(r, 2));
        p = 1.2F;
        coeff = (float) (-0.5 * c * a * p);
        s = 0.00007F;

        dT = .01F;
    }

    public void render() {
        parent.fill(235, 135, 156);
        parent.ellipse(pos.x+25, parent.height - pos.y, 50, 50);
        parent.fill(0);
        parent.textSize(10);
        parent.text(angle,pos.x, parent.height - pos.y);
    }

    public void update() {
//        dragVel.set(PVector.sub(drag,wind));
//        PVector.div(dragVel, dragVel.mag(), drag);
//        drag.mult((float) (coeff * dragVel.magSq()));
//        PVector.div(vel, vel.mag(), drag);
//        drag.mult((float) (coeff * vel.magSq()));
//        force.set(drag);
//        
//        magnus = spin.cross(vel);
//        magnus.mult(s);
//        force.add(magnus);
//        
//        acc.set(PVector.add(force,PVector.mult(g,m)));
//        acc.div(m);

        vel.add(acc.x * dT, acc.y * dT);
        
        if (pos.y <= 0) {
            range = (float) (pos.x - initX);
            acc.set(0, 0);
            vel.set(0, 0);
            landed = true;
        }
           
        pos.add(vel.x * dT, vel.y * dT);
    }
}
