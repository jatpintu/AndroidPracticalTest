package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // Android debug variables
    final static String TAG="DINO-RAINBOWS";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;

    Item item1;
    Item item2;
    Item item3;
    Item item4;
    Bitmap itemImage1;
    Bitmap itemImage2;
    Bitmap itemImage3;
    Bitmap itemImage4;


    Player player;
    Bitmap PlayerImage;
    int playerXpos;
    int playerYpos;

    Bitmap lines;

    ArrayList<Item> items = new ArrayList<Item>();






    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------

    // represent the TOP LEFT CORNER OF THE GRAPHIC

    // ----------------------------
    // ## GAME STATS
    // ----------------------------


    public GameEngine(Context context, int w, int h) {
        super(context);

        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;

        this.printScreenInfo();

        // put the initial starting position of your player and item
        this.player = new Player(getContext(), 1500, 50);
        this.item1 = new Item(getContext(), 50, 50);
        this.items.add(item1);
        this.item2 = new Item(getContext(), 50, 250);
        this.items.add(item2);
        this.item3 = new Item(getContext(), 50, 450);
        this.items.add(item3);
        this.item4 = new Item(getContext(), 50, 650);
        this.items.add(item4);

        this.PlayerImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.dino64);
        this.itemImage1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.candy64);
        this.itemImage2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.poop64);
        this.itemImage3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.rainbow64);
        this.itemImage4 = BitmapFactory.decodeResource(context.getResources(),R.drawable.candy64);



//        this.item1

        this.lines = BitmapFactory.decodeResource(getResources(),R.drawable.alien_laser);
        this.lines = Bitmap.createScaledBitmap(
                this.lines,
                this.screenWidth,
                50,
                false);




    }



    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        //@TODO: Start the player at the left side of screen
    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location

    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void spawnBullet() {



    }


    private void spawnObjects() {
        Random random = new Random();

        if (this.items.size() < 4) {
            Item myItem;
            if (this.items.isEmpty()) {
                myItem = new Item(getContext(), 100, 600);
            } else {
                // prev bullet
                Item prevItem = this.items.get(this.items.size() - 1);
                int newItemXpos = prevItem.getxPosition() + 120;
                myItem = new Item(getContext(), newItemXpos, 600);
            }
            this.items.add(myItem);
        }

        //@TODO: Place the enemies in a random location

    }

    int numLoops = 0;

    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------

    public void updatePositions() {


        if (this.fingerAction == "inputUp") {
            // if mousedown, then move player up
            // Make the UP movement > than down movement - this will
            // make it look like the player is moving up alot
            player.setyPosition(player.getyPosition() - 50);
            if(player.getxPosition() <= screenHeight){
                player.setxPosition(1500);
                player.setyPosition(50);
            }
            player.updateHitbox();
        }
        if (this.fingerAction == "inputDown") {
            // if mouseup, then move player down
            player.setyPosition(player.getyPosition() + 50);
            if(player.getxPosition() <= screenHeight){
                player.setyPosition(50);
                player.setxPosition(1500);
            }
            player.updateHitbox();


        }
        if (numLoops % 5  == 0) {
            spawnObjects();
        }

        item1.setxPosition(item1.getxPosition() + 10);
        item2.setxPosition(item2.getxPosition() + 40);
        item3.setxPosition(item3.getxPosition() + 5);
        item4.setxPosition(item4.getxPosition() + 50);
        



    }


    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);


            canvas.drawBitmap(this.PlayerImage,player.getxPosition(),player.getyPosition(),null);
            canvas.drawBitmap(this.itemImage1,item1.getxPosition(),item1.getyPosition(),null);
            canvas.drawBitmap(lines,50,200,null);
            canvas.drawBitmap(this.itemImage2,item2.getxPosition(),item2.getyPosition(),null);
            canvas.drawBitmap(lines,50,400,null);
            canvas.drawBitmap(this.itemImage3,item3.getxPosition(),item3.getyPosition(),null);
            canvas.drawBitmap(lines,50,600,null);
            canvas.drawBitmap(this.itemImage4,item4.getxPosition(),item4.getyPosition(),null);
            canvas.drawBitmap(lines,50,800,null);



            // DRAW THE PLAYER HITBOX
            // ------------------------
            // 1. change the paintbrush settings so we can see the hitbox
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);
            paintbrush.setTextSize(60);
            canvas.drawText("LIVES : " + " ",
                    1000,
                    50,
                    paintbrush
            );


            canvas.drawText("SCORE: " + " ",
                    1400,
                    50,
                    paintbrush
            );

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(120);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    String fingerAction = "";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {

            fingerAction = "inputUp";
        }
        else if (userAction == MotionEvent.ACTION_UP) {

            fingerAction= "inputDown";
        }

        return true;
    }
}
