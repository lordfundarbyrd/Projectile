
import processing.core.PApplet;
import processing.core.PVector;

public class Projectile extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[]{Projectile.class.getName()});
    }

    static PVector position;
    static PVector velocity;
    static PVector acceleration;
    static PVector drag;
    static PVector force;

    float airResistance;
    float c; // c constant
    float p; // density
    float v; // velocity
    float a; // area
    float r; // radius
    float mag; // velocity magnitude
    float mass;

    float t = .1F;  // time steps
    float time = 0.000F; // timer

    float finalVel = 0; // Final Velocity
    float initHeight; // height ball falls from
    int angle = 0; // angle of launch
    int initVel = 0; // initial velocity
    boolean launch = false; // whether the ball has launched or not

    public void settings() {
        size(900, 630);
    }

    public void setup() {
        background(100);
        frameRate(30 / t); // frame rate based on time steps, speeds up time
        textSize(20);

        position = new PVector(10, 620);  
        velocity = new PVector(0, 0); 
        acceleration = new PVector(0, 9.8F); 
        drag = new PVector(); 
        force = new PVector();
        
        c = .2F; // coefficient
        mass = 4.42F; // kg
        p = 1.15F; // kg/m^3
        r = 0.125F; // radius of object (pumpkin in this case)
        a = (float) (Math.PI * Math.pow(r, 2)); // area of crosssection

        Graph graph = new Graph();
        PApplet.runSketch(new String[]{this.getClass().getSimpleName()}, graph); // open up second window for 
    }

    public void draw() {
        background(175, 238, 238);
        strokeWeight(1);

        //ball
        fill(255);
        ellipse(position.x + 30, position.y - 30, 50, 50);

        //sliders
        fill(180);
        rect(25, 25, 181, 25); // for angle
        rect(25, 75, 621, 25); // for velocity
        rect(25, 125, 621, 25); // for height
        strokeWeight(6);
        line(-angle + 25, 25, -angle + 25, 50); // line on slider for adjusting
        fill(0);
        text(-angle + " Deg.", 216, 50);
        line(initVel + 25, 75, initVel + 25, 100); // line on slider for adjusting
        fill(0);
        text(initVel + " m/s", 656, 100);
        line(initHeight + 25, 125, initHeight + 25, 150); // line on slider for adjusting
        fill(0);
        text(initHeight + " m", 656, 150);

        //lines on ball
        pushMatrix();
        translate(position.x + 30, position.y - 30);
        rotate(radians(angle)); // rotate lines based on angle
        // scale lines based on velocity
        line(0, 0, initVel, 0); 
        line(initVel, 0, initVel - 10, -10);
        line(initVel, 0, initVel - 10, 10);
        popMatrix();

        //what to do when ball launches
        if (launch) {
            //p=(float) (1.15*Math.pow(Math.E,-.000119F)*position.y);
            force.set(1,0,0);
            mag = velocity.mag(); // get magnitude of velcity and direction
            drag.set(0,(float)(Math.pow(mag, 2) * a * c * .5 * p), 0); // calculate resistance
            force.add(drag);  
            acceleration.set(force); // set acceleration to force
            acceleration.div(mass); // divide by mass
            finalVel = velocity.y + acceleration.y * t; // find final velcity
            position.set(position.x + velocity.x * t, position.y + velocity.y * t + .5F * (finalVel - velocity.y) * t); // find position
            velocity.y = finalVel; 
            force.set(1, 0, 0);

            if (position.y < height - 10) {
                time = time + t;
            }
        }
        if (position.y > (height - 10)) {
            position.y = height - 10;
            velocity.set(0, 0);
        }

        textSize(20); // size of text
        fill(0); // color of text
        text(initHeight, 580, 200); // initial drop height
        text("m init", 785, 200); // units for height

        text(position.x - 10, 580, 250); // height of ball
        text("m range", 785, 250); // units for position

        text(velocity.y, 580, 300); // speed of ball
        text("m/s", 785, 300); // units for velocity

        text(time, 580, 350); // length of fall
        text("s", 785, 350); // units for time
    }

    // for sliders, check to see if mouse is within x and y coordinates of rectanges
    public void mouseDragged() {
        if (mouseX > 24 && mouseX < 207  && mouseY > 25 && mouseY < 50 && position.x < 11) { //limited between 0 and 180 because drag wouldn't work right when it was greater than 180
            angle = -mouseX + 24; // change angle based on mouse
        } else if (mouseX > 24 && mouseX < 649 && mouseY > 75 && mouseY < 100 && position.x < 11) {
            initVel = mouseX - 24; // change velocity based on mouse
        } else if (mouseX > 24 && mouseX < 649 && mouseY > 125 && mouseY < 150 && position.x < 11) {
            position.y = mouseX - 24; // change position based on mouse
            initHeight = height-mouseX - 24; // show how far off bottom of screen ball was
        }
    }
    
    // set parameters based on sliders
    public void mouseReleased() {
        if (position.x < 11 && mouseY >= 125) {
            velocity.set(initVel * cos(radians(angle)), initVel * sin(radians(angle))); // set velocity
            initHeight = position.y; // set position
            launch = true; // launch
            time = 0; // set timer
        }
    }

    // reset launch
    public void mousePressed() {
        launch = false;
        position.set(10, 620);
    }

    public class Graph extends PApplet {
        
        float velMag;

        public void settings() {
            size(1500, 700);
        }

        public void setup() {
            background(0);
            velMag = 0; // magnitude of velocity for graphing
        }

        public void draw() {
            stroke(255);
            strokeWeight(1);
            line(0, height / 2, width, height / 2); // x axis
            if (launch) {
                strokeWeight(1);
                point(width/time,velMag); // draw point where velocity is scaled in relation to the width
                velMag = velocity.mag(); // set magnitude to next velocity
            }
        }
    }
}
