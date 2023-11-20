package com.magz.tetris;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {
    public static final int SIZE = 40;
    public static int XMAX = SIZE * 5;
    public static int YMAX = SIZE * 15;
    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];
    private static Pane group = new Pane();
    private Pane nextBlockPane = new Pane();
    private static Form nextBlock;
    private static Form object;
    private static Scene scene = new Scene(group, XMAX + 150, YMAX);
    public static int score = 0;
    private static int top = 0;
    private static boolean game = true;
    private static int linesNo = 0;
    private List<Rectangle> ghostBlockRectangles = new ArrayList<>();
    private long lastMoveTime = 0;
    private final long placementDelay = 1000;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setOnCloseRequest(event -> System.exit(0));
        group.setStyle("-fx-background-color: #5A5A5A;");

        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }
        drawGrid();

        Line line = new Line(XMAX, 0, XMAX, YMAX);

        Text scoretext = new Text("Score:");
        scoretext.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 20));
        scoretext.setY(50);
        scoretext.setX(XMAX + 10);
        scoretext.setFill(Color.WHITE);

        Text level = new Text("Lines:");
        level.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 20));
        level.setY(100);
        level.setX(XMAX + 10);
        level.setFill(Color.WHITE);

        Text nextblocktext = new Text("NEXT BLOCK");
        nextblocktext.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 20));
        nextblocktext.setY(275);
        nextblocktext.setX(XMAX + 10);
        nextblocktext.setFill(Color.WHITE);

        group.getChildren().addAll(line, scoretext,nextblocktext, level);

        nextBlock = PentominoBuilder.makeRect();
        nextBlockPane.setLayoutX(XMAX + 60);
        nextBlockPane.setLayoutY(300);
        group.getChildren().add(nextBlockPane);

        updateNextBlockDisplay(nextBlock);

        object = PentominoBuilder.makeRect();
        group.getChildren().addAll(object.a, object.b, object.c, object.d, object.e);

        moveOnKeyPress(object);

        stage.setScene(scene);
        stage.setTitle("T E T R I S");
        stage.show();

        Timer fall = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0
                            || object.d.getY() == 0 || object.e.getY() == 0){
                        top++;
                    }
                    else{
                        top = 0;
                    }

                    if (top == 2) {
                        Text over = new Text("GAME OVER");
                        over.setFill(Color.RED);
                        over.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 70));
                        over.setY(250);
                        over.setX(10);
                        group.getChildren().add(over);
                        game = false;
                    }
                    if (top == 15) {
                        System.exit(0);
                    }

                    if (game) {
                        MoveDown(object);
                        scoretext.setText("Score: " + Integer.toString(linesNo*100));
                        level.setText("Lines: " + Integer.toString(linesNo));

                        removeGhostBlock();
                        displayGhostBlock(object);
                    }
                });
            }
        };
        fall.schedule(task, 0, 500);
    }
    private void drawGrid() {
        for (int i = 0; i < XMAX/SIZE; i++) {
            Line line = new Line(i * SIZE, 0, i * SIZE, YMAX);
            line.setStroke(Color.BLACK);
            group.getChildren().add(line);
        }
        for (int i = 0; i < YMAX/SIZE; i++) {
            Line line = new Line(0, i * SIZE, XMAX, i * SIZE);
            line.setStroke(Color.BLACK);
            group.getChildren().add(line);
        }
    }
    public void updateNextBlockDisplay(Form nextBlock) {
        nextBlockPane.getChildren().clear();

        double minX = Math.min(Math.min(Math.min(Math.min(nextBlock.a.getX(), nextBlock.b.getX()),
                nextBlock.c.getX()), nextBlock.d.getX()), nextBlock.e.getX());
        double minY = Math.min(Math.min(Math.min(Math.min(nextBlock.a.getY(), nextBlock.b.getY()),
                nextBlock.c.getY()), nextBlock.d.getY()), nextBlock.e.getX());

        // Create and position the rectangles based on the block's shape
        Rectangle a = createAndPositionRectangle(nextBlock.a, minX, minY);
        Rectangle b = createAndPositionRectangle(nextBlock.b, minX, minY);
        Rectangle c = createAndPositionRectangle(nextBlock.c, minX, minY);
        Rectangle d = createAndPositionRectangle(nextBlock.d, minX, minY);
        Rectangle e = createAndPositionRectangle(nextBlock.e, minX, minY);
        nextBlockPane.getChildren().addAll(a, b, c, d, e);
    }
    private Rectangle createAndPositionRectangle(Rectangle original, double minX, double minY) {
        Rectangle rect = new Rectangle(SIZE-1, SIZE-1, original.getFill());
        // Position relative to the top-left corner of the block's bounding box
        rect.setX((original.getX() - minX) / SIZE * (SIZE-1) + nextBlockPane.getPrefWidth() / 2 - SIZE);
        rect.setY((original.getY() - minY) / SIZE * (SIZE-1));
        return rect;
    }
    private Form calculateGhostPosition(Form currentBlock) {
        Form ghostBlock = new Form(
                new Rectangle(currentBlock.a.getX(), currentBlock.a.getY(), currentBlock.a.getWidth(), currentBlock.a.getHeight()),
                new Rectangle(currentBlock.b.getX(), currentBlock.b.getY(), currentBlock.b.getWidth(), currentBlock.b.getHeight()),
                new Rectangle(currentBlock.c.getX(), currentBlock.c.getY(), currentBlock.c.getWidth(), currentBlock.c.getHeight()),
                new Rectangle(currentBlock.d.getX(), currentBlock.d.getY(), currentBlock.d.getWidth(), currentBlock.d.getHeight()),
                new Rectangle(currentBlock.e.getX(), currentBlock.e.getY(), currentBlock.e.getWidth(), currentBlock.e.getHeight()),
                currentBlock.getName()
        );

        while (canMoveDown(ghostBlock)) {
            ghostBlock.a.setY(ghostBlock.a.getY() + SIZE);
            ghostBlock.b.setY(ghostBlock.b.getY() + SIZE);
            ghostBlock.c.setY(ghostBlock.c.getY() + SIZE);
            ghostBlock.d.setY(ghostBlock.d.getY() + SIZE);
            ghostBlock.e.setY(ghostBlock.e.getY() + SIZE);
        }

        return ghostBlock;
    }
    private void displayGhostBlock(Form currentBlock) {
        Form ghostBlock = calculateGhostPosition(currentBlock);

        Color ghostColor = currentBlock.getColor().deriveColor(0, 1.0, 1.0, 0.10);
        ghostBlock.a.setFill(ghostColor);
        ghostBlock.b.setFill(ghostColor);
        ghostBlock.c.setFill(ghostColor);
        ghostBlock.d.setFill(ghostColor);
        ghostBlock.e.setFill(ghostColor);

        ghostBlockRectangles.addAll(Arrays.asList(ghostBlock.a, ghostBlock.b, ghostBlock.c, ghostBlock.d, ghostBlock.e));

        group.getChildren().addAll(ghostBlock.a, ghostBlock.b, ghostBlock.c, ghostBlock.d, ghostBlock.e);
    }
    private void removeGhostBlock() {
        if (!ghostBlockRectangles.isEmpty()) {
            group.getChildren().removeAll(ghostBlockRectangles);
            ghostBlockRectangles.clear();
        }
    }
    private void updateGhostBlock(Form currentBlock) {
        removeGhostBlock();
        displayGhostBlock(currentBlock);
    }
    private void moveOnKeyPress(Form form) {
        scene.setOnKeyPressed(event -> {
            boolean moved = false;
            switch (event.getCode()) {
                case D:
                    Movement.MoveRight(form);
                    moved = true;
                    break;
                case S:
                    MoveDown(form);
                    moved = true;
                    break;
                case A:
                    Movement.MoveLeft(form);
                    moved = true;
                    break;
                case W:
                    Movement.MoveTurn(form);
                    moved = true;
                    break;
                case SPACE:
                    DropBlock(form);
                    moved = true;
                    break;

            }
            if (moved) {
                updateGhostBlock(form);
            }
        });
    }
    private void DropBlock(Form form) {
        while (canMoveDown(form)) {
            form.a.setY(form.a.getY() + SIZE);
            form.b.setY(form.b.getY() + SIZE);
            form.c.setY(form.c.getY() + SIZE);
            form.d.setY(form.d.getY() + SIZE);
            form.e.setY(form.e.getY() + SIZE);
        }

        placeBlockInGrid(form);
    }
    private void placeBlockInGrid(Form form) {
        MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
        MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
        MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
        MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
        MESH[(int) form.e.getX() / SIZE][(int) form.e.getY() / SIZE] = 1;
    }

    private void RemoveRows(Pane pane) {
        ArrayList<Node> rects = new ArrayList<Node>();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Node> newrects = new ArrayList<Node>();
        int full = 0;
        for (int i = 0; i < MESH[0].length; i++) {
            for (int j = 0; j < MESH.length; j++) {
                if (MESH[j][i] == 1)
                    full++;
            }
            if (full == MESH.length)
                lines.add(i);
            full = 0;
        }
        if (lines.size() > 0)
            do {
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                score += 50;
                linesNo++;

                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node : newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
                rects.clear();
            } while (lines.size() > 0);
    }
    private void MoveDown(Form form) {
        if (canMoveDown(form)) {

            form.a.setY(form.a.getY() + SIZE);
            form.b.setY(form.b.getY() + SIZE);
            form.c.setY(form.c.getY() + SIZE);
            form.d.setY(form.d.getY() + SIZE);
            form.e.setY(form.e.getY() + SIZE);
            lastMoveTime = System.currentTimeMillis();
        } else {
            if (System.currentTimeMillis() - lastMoveTime >= placementDelay) {
                placeBlock(form);
                lastMoveTime = 0; // Reset the timer after placing the block
            }
        }
    }
    private void placeBlock (Form form){
        MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE)] = 1;
        MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE)] = 1;
        MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE)] = 1;
        MESH[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE)] = 1;
        MESH[(int) form.e.getX() / SIZE][((int) form.e.getY() / SIZE)] = 1;

        RemoveRows(group);
        object = nextBlock;

        nextBlock = PentominoBuilder.makeRect();
        updateNextBlockDisplay(nextBlock);

        group.getChildren().addAll(object.a, object.b, object.c, object.d, object.e);
        moveOnKeyPress(object);

    }
    private boolean canMoveDown (Form form){
        return form.a.getY() + SIZE < YMAX && form.b.getY() + SIZE < YMAX &&
                form.c.getY() + SIZE < YMAX && form.d.getY() + SIZE < YMAX && form.e.getY() + SIZE < YMAX &&
                MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1] == 0 &&
                MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1] == 0 &&
                MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1] == 0 &&
                MESH[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1] == 0 &&
                MESH[(int) form.e.getX() / SIZE][((int) form.e.getY() / SIZE) + 1] == 0;
    }

    private void testing(int[][] test){
        System.out.println("--------");
        for(int[] i : test){
            System.out.println(Arrays.toString(i));
        }
    }
}