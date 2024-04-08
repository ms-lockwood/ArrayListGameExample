//Array List Spawning Example
//Lockwood Version 2023-24
// Learning goals:
//THIS VERSION: should have an array list of rocks that when the astro intersects with the rock,
//the rocks spawn into two rocks.
/**To add arraylists:
 * 1) declare an array list
 * 2) construct the array list in GameLand()
 * 3) fill the array list using a loop that goes up to an integer
 * 4) USE THE ARRAY LIST
 *          to use the arraylist:
 *          tell it to move (using a loop going up to rocks.size() )
 *          tell it to check for collisions and add a new rock each time (using a loop going up to rocks.size()
 *          tell it to render to the screen (using a loop going up to rocks.size()
 *          etc.... */

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section

    public class GameLand implements Runnable, KeyListener {

        //Variable Declaration Section
        //Declare the variables used in the program
        //You can set their initial values here if you want

        //Sets the width and height of the program window
        final int WIDTH = 1000;
        final int HEIGHT = 700;

        //Declare the variables needed for the graphics
        public JFrame frame;
        public Canvas canvas;
        public JPanel panel;

        public BufferStrategy bufferStrategy;

        //Declare the objects used in the program below
        public Hero astro;

        /**STEP 1 declare an array list:
        //USE AUTOFILL when typing this our to import ArrayList package e.g. import java.util.ArrayList;*/
        public ArrayList<Meteor> rocks;
        public ArrayList<Meteor> pebbles;


        public Image astroPic;
        public Image backgroundPic;
        public Image rockPic;




        // Main method definition: PSVM
        // This is the code that runs first and automatically
        public static void main(String[] args) {
            GameLand ex = new GameLand();   //creates a new instance of the game and tells GameLand() method to run
            new Thread(ex).start();       //creates a thread & starts up the code in the run( ) method
        }

        // Constructor Method
        // This has no return type and has the same name as the class
        // This section is the setup portion of the program
        // Initialize your variables and construct your program objects here.
        public GameLand() {
            setUpGraphics(); //this calls the setUpGraphics() method

            //create (construct) the objects needed for the game below
            //for each object that has a picture, load in images as well

            astro = new Hero(400,500, 2, 0);

            /**STEP 2 //construct my array list of meteors **/
            rocks = new ArrayList<>();
            pebbles = new ArrayList<>();
            /**STEP 3 //fill my array list of meteors going up to an actuall nunber (not rocks.size() )**/

            for(int i=0;i< 10;i=i+1){ //we must use an integer for i<10 and not rocks.size()
                int randX = (int)(Math.random()*1000);
                int randY = (int)(Math.random()*700);
                //here we use the method .add() that comes with the array list package
                rocks.add(new Meteor(randX,randY,2,3, 80, 80));
            }
            /**NOTE: i'm opting NOT to create pebbles at the start of the game. INSTEAD
             //I WILL FILL MY PEBBLES ARRAY LIST IN MY COLLISIONS METHOD*/


            /**STEP 3.5 load in the image for your objects (including a rock image for array list) **/
            astroPic= Toolkit.getDefaultToolkit().getImage("astroPic.png");
            rockPic = Toolkit.getDefaultToolkit().getImage("rock.png");
            backgroundPic = Toolkit.getDefaultToolkit().getImage( "spacePic.jpeg");



        }// GameLand()

//*******************************************************************************
//User Method Section
//
// put your code to do things here.

        // main thread
        // this is the code that plays the game after you set things up
        public void run() {
            //for the moment we will loop things forever using a while loop
            while (true) {
                moveThings();  //move all the game objects
                collisions();
                render();  // paint the graphics
                pause(20); // sleep for 20 ms
            }
        }

        //paints things on the screen using bufferStrategy
        private void render() {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.clearRect(0, 0, WIDTH, HEIGHT);
//draw background
            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

            //draw the image of your objects below:

            g.drawImage(astroPic, astro.xpos, astro.ypos, astro.width,astro.height, null);

            /**STEP 4ish use your array list, tell it render to the screen */
            for(int i=0;i< rocks.size();i=i+1) {
                g.drawImage(rockPic, rocks.get(i).xpos, rocks.get(i).ypos, rocks.get(i).width,rocks.get(i).height, null);
            }
            //because pebbles starts out with size 0, we need this if statement to avoid a null pointer exception:
           if(!pebbles.isEmpty()) {
               for (int i = 0; i < pebbles.size(); i = i + 1) {
                   g.drawImage(rockPic, pebbles.get(i).xpos, pebbles.get(i).ypos, pebbles.get(i).width, pebbles.get(i).height, null);
               }
           }

            //dispose the images each time(this allows for the illusion of movement).
            g.dispose();

            bufferStrategy.show();
        }

        public void moveThings() {
            //call the move() method code from your object class
            astro.move();
            /**STEP 4 use your array list, tell it to move */
            for(int i=0;i< rocks.size();i=i+1) {
               rocks.get(i).bouncingMove();
            }
            /** Also, tell my pebbles array list to move, but it starts empty so it needs the if statement*/
            if (!pebbles.isEmpty()) { //! means not so !pebbles.isEmpty() means "pebbles is not Empty"
                for (int i = 0; i < pebbles.size(); i = i + 1) {
                    pebbles.get(i).bouncingMove();
                }
            }
        }

        public void collisions(){
            /**STEP 4 use your array list, check collisions and create new rocks */
            for(int i=0;i< rocks.size();i=i+1) {
                if(rocks.get(i).rec.intersects(astro.rec) && rocks.get(i).isAlive==true) {
                    //I HAVE OPTIONS HERE to create new rocks in my rocks list OR create new
                    //pebbles in a second different array list. I'm chosing to fill my pebbles array list here
                    //i'll use the current location of the rock, but random speeds in rand directions
                    int randDirection=1;
                     if(Math.random()>0.5) {
                        randDirection= -1;
                    }
                    int randdX = (int)((Math.random() * 3)+1)*randDirection;
                    int randdY = (int)((Math.random() * 3)+1)*randDirection;
                    //add one pebble
                    pebbles.add(new Meteor(rocks.get(i).xpos, rocks.get(i).ypos, randdX, randdY, 30, 30));
                    //add a second pebble in opposite direction
                    pebbles.add(new Meteor(rocks.get(i).xpos, rocks.get(i).ypos, (-1*randdX), (-1*randdY), 30, 30));

                    rocks.get(i).isAlive = false; //kill old big rock
                    rocks.get(i).dx = 0;
                    rocks.get(i).dy = 0;
                    rocks.get(i).ypos = 2000;

                }


            }

        }




        //Pauses or sleeps the computer for the amount specified in milliseconds
        public void pause(int time) {
            //sleep
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {

            }
        }

        //Graphics setup method
        private void setUpGraphics() {
            frame = new JFrame("Game Land");   //Create the program window or frame.  Names it.

            panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
            panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
            panel.setLayout(null);   //set the layout

            // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
            // and trap input events (Mouse and Keyboard events)
            canvas = new Canvas();
            canvas.setBounds(0, 0, WIDTH, HEIGHT);
            canvas.setIgnoreRepaint(true);
            canvas.addKeyListener(this);
            panel.add(canvas);  // adds the canvas to the panel.

            // frame operations
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
            frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
            frame.setResizable(false);   //makes it so the frame cannot be resized
            frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

            // sets up things so the screen displays images nicely.
            canvas.createBufferStrategy(2);
            bufferStrategy = canvas.getBufferStrategy();
            canvas.requestFocus();
            System.out.println("DONE graphic setup");
        }


        @Override
        public void keyTyped(KeyEvent e) {
            //probably will stay empty
        }

        @Override
        public void keyPressed(KeyEvent e) {
            char key =e.getKeyChar();
            int keyCode=e.getKeyCode();
            System.out.println("Key: "+ key+ ", KeyCode: "+ keyCode);
            if(keyCode==68){// d is 68 // right movement
                astro.rightPressed=true;
            }
            if (keyCode==65){//a is 65
                astro.leftPressed=true;
            }
            if(keyCode==87){ //w is 87
                astro.upPressed=true;
            }
            if(keyCode==83){ //s is 83
                astro.downPressed=true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            char key =e.getKeyChar();
            int keyCode=e.getKeyCode();
            if(keyCode==68){// d is 68 // right movement
                astro.rightPressed=false;
            }
            if (keyCode==65){
                astro.leftPressed=false;
            }
            if(keyCode==87){
                astro.upPressed=false;
            }
            if(keyCode==83){
                astro.downPressed=false;
            }

        }
    }

