package com.magz.tetris;

import javafx.scene.shape.Rectangle;

public class Movement {
    public static final int SIZE = Main.SIZE;
    public static int XMAX = Main.XMAX;
    public static int YMAX = Main.YMAX;
    public static int[][] MESH = Main.MESH;

    public static void MoveRight(Form form) {
        if (form.a.getX() + SIZE <= XMAX - SIZE && form.b.getX() + SIZE <= XMAX - SIZE
                && form.c.getX() + SIZE <= XMAX - SIZE && form.d.getX() + SIZE <= XMAX - SIZE && form.e.getX() + SIZE <= XMAX - SIZE) {
            int movea = MESH[((int) form.a.getX() / SIZE) + 1][((int) form.a.getY() / SIZE)];
            int moveb = MESH[((int) form.b.getX() / SIZE) + 1][((int) form.b.getY() / SIZE)];
            int movec = MESH[((int) form.c.getX() / SIZE) + 1][((int) form.c.getY() / SIZE)];
            int moved = MESH[((int) form.d.getX() / SIZE) + 1][((int) form.d.getY() / SIZE)];
            int movee = MESH[((int) form.e.getX() / SIZE) + 1][((int) form.e.getY() / SIZE)];
            if (movea == 0 && movea == moveb && moveb == movec && movec == moved && moved == movee) {
                form.a.setX(form.a.getX() + SIZE);
                form.b.setX(form.b.getX() + SIZE);
                form.c.setX(form.c.getX() + SIZE);
                form.d.setX(form.d.getX() + SIZE);
                form.e.setX(form.e.getX() + SIZE);
            }
        }
    }

    public static void MoveLeft(Form form) {
        if (form.a.getX() - SIZE >= 0 && form.b.getX() - SIZE >= 0 && form.c.getX() - SIZE >= 0
                && form.d.getX() - SIZE >= 0 && form.e.getX() - SIZE >= 0) {
            int movea = MESH[((int) form.a.getX() / SIZE) - 1][((int) form.a.getY() / SIZE)];
            int moveb = MESH[((int) form.b.getX() / SIZE) - 1][((int) form.b.getY() / SIZE)];
            int movec = MESH[((int) form.c.getX() / SIZE) - 1][((int) form.c.getY() / SIZE)];
            int moved = MESH[((int) form.d.getX() / SIZE) - 1][((int) form.d.getY() / SIZE)];
            int movee = MESH[((int) form.e.getX() / SIZE) - 1][((int) form.e.getY() / SIZE)];
            if (movea == 0 && movea == moveb && moveb == movec && movec == moved && moved == movee) {
                form.a.setX(form.a.getX() - SIZE);
                form.b.setX(form.b.getX() - SIZE);
                form.c.setX(form.c.getX() - SIZE);
                form.d.setX(form.d.getX() - SIZE);
                form.e.setX(form.e.getX() - SIZE);
            }
        }
    }

    public static void MoveTurn(Form form) {
        int f = form.form;
        BlockRotator(f, form);
    }
    private static void BlockRotator(int formNumber, Form form) {
        if (form.getName() == "x") {
            return;
        }
        Rectangle[] blocks = {form.a, form.b, form.d, form.e};
        boolean canRotate = true;

        for (Rectangle block : blocks) {
            int[] currentDistance = distanceFinder(form.c, block);
            int[] newCoords = rotatorAssistAssistant(currentDistance, formNumber);

            if (!cB(block, newCoords[0], newCoords[1])) {
                canRotate = false;
                break;
            }
        }

        if (canRotate) {
            for (Rectangle block : blocks) {
                int[] currentDistance = distanceFinder(form.c, block);
                int[] newCoords = rotatorAssistAssistant(currentDistance, formNumber);
                rotatorAssist(block, form.c, newCoords[0], newCoords[1]);
            }
        }
    }
    private static int[] rotatorAssistAssistant(int[] coords, int formNumber){
        int newX = 0, newY = 0;

        switch (formNumber) {
            case 1:
            case 3:
                newX = coords[1];
                newY = coords[0];
                break;
            case 2:
            case 4:
                newX = coords[0];
                newY = coords[1];
                break;
        }
        return new int[]{newX, newY};
    }
    private static void rotatorAssist(Rectangle block, Rectangle centreBlock, int relX, int relY) {
        double newX = centreBlock.getX() + relX * SIZE;
        double newY = centreBlock.getY() + relY * SIZE;

        block.setX(newX);
        block.setY(newY);
    }
    private static int[] distanceFinder(Rectangle centreBlock, Rectangle block){
        int[] relativeToCentre = new int[2];

        relativeToCentre[0] = (int) ((centreBlock.getX() - block.getX()) / -SIZE);
        relativeToCentre[1] = (int) ((centreBlock.getY() - block.getY()) / SIZE);

        return relativeToCentre;
    }
    private static boolean cB (Rectangle rect,int x, int y){
        boolean xb = false;
        boolean yb = false;
        if (x >= 0)
            xb = rect.getX() + x * SIZE <= XMAX - SIZE;
        if (x < 0)
            xb = rect.getX() + x * SIZE >= 0;
        if (y >= 0)
            yb = rect.getY() - y * SIZE > 0;
        if (y < 0)
            yb = rect.getY() + y * SIZE < YMAX;
        try {
            return xb && yb && MESH[(((int) rect.getX() / SIZE) + x)][(((int) rect.getY() / SIZE) - y)] == 0;
        }catch (Exception e){
            return false;
        }
    }
}
