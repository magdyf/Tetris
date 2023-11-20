package com.magz.tetris;


import javafx.scene.shape.Rectangle;


public class PentominoBuilder {
    public static final int SIZE = Main.SIZE;
    public static int XMAX = Main.XMAX;
    public static int MIDDLE;

    public static Form makeRect() {
        if (((XMAX/SIZE))%2 == 0){
            MIDDLE = XMAX/2;
        }else{
            MIDDLE = XMAX/2 - SIZE/2;
        }
        int block = (int) (Math.random() * 121);
        String name;
        Rectangle a = new Rectangle(SIZE-1, SIZE-1), b = new Rectangle(SIZE-1, SIZE-1), c = new Rectangle(SIZE-1, SIZE-1),
                d = new Rectangle(SIZE-1, SIZE-1), e = new Rectangle(SIZE-1,SIZE-1);
        if (block < 10) {
            a.setX(MIDDLE);
            b.setX(MIDDLE + SIZE);
            c.setX(MIDDLE + SIZE); c.setY(SIZE);
            d.setX(MIDDLE); d.setY(SIZE);
            e.setX(MIDDLE); e.setY(SIZE*2);
            name = "p";
        } else if (block < 20) {
            a.setX(MIDDLE); a.setY(SIZE);
            b.setX(MIDDLE);
            c.setX(MIDDLE - SIZE); c.setY(SIZE);
            d.setX(MIDDLE + SIZE); d.setY(SIZE);
            e.setX(MIDDLE); e.setY(SIZE*2);
            name = "x";
        } else if (block < 30) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE*2);
            c.setX(MIDDLE); c.setY(SIZE);
            d.setX(MIDDLE + SIZE);
            e.setX(MIDDLE - SIZE); e.setY(SIZE);
            name = "f";
        } else if (block < 40) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE);
            c.setX(MIDDLE); c.setY(SIZE*2);
            d.setX(MIDDLE + SIZE); d.setY(SIZE*2);
            e.setX(MIDDLE + SIZE*2); e.setY(SIZE*2);
            name = "v";
        } else if (block < 50) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE);
            c.setX(MIDDLE + SIZE); c.setY(SIZE);
            d.setX(MIDDLE + SIZE); d.setY(SIZE*2);
            e.setX(MIDDLE + SIZE*2); e.setY(SIZE*2);
            name = "w";
        } else if (block < 60) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE);
            c.setX(MIDDLE); c.setY(SIZE*2);
            d.setX(MIDDLE); d.setY(SIZE*3);
            e.setX(MIDDLE - SIZE); e.setY(SIZE);
            name = "y";
        } else if (block < 70) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE);
            c.setX(MIDDLE); c.setY(SIZE*2);
            d.setX(MIDDLE); d.setY(SIZE*3);
            e.setX(MIDDLE); e.setY(SIZE*4);
            name = "i";
        }else if (block < 80) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE*2);
            c.setX(MIDDLE); c.setY(SIZE);
            d.setX(MIDDLE - SIZE);
            e.setX(MIDDLE + SIZE);
            name = "t";
        }else if (block < 90) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE);
            c.setX(MIDDLE); c.setY(SIZE*2);
            d.setX(MIDDLE - SIZE);
            e.setX(MIDDLE + SIZE); e.setY(SIZE*2);
            name = "z";
        }else if (block < 100) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE*2);
            c.setX(MIDDLE); c.setY(SIZE);
            d.setX(MIDDLE + SIZE);
            e.setX(MIDDLE + SIZE); e.setY(SIZE*2);
            name = "u";
        }else if (block < 110) {
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE);
            c.setX(MIDDLE - SIZE); c.setY(SIZE);
            d.setX(MIDDLE - SIZE); d.setY(SIZE*2);
            e.setX(MIDDLE - SIZE); e.setY(SIZE*3);
            name = "n";
        }else{
            a.setX(MIDDLE);
            b.setX(MIDDLE); b.setY(SIZE);
            c.setX(MIDDLE); c.setY(SIZE*2);
            d.setX(MIDDLE); d.setY(SIZE*3);
            e.setX(MIDDLE + SIZE); e.setY(SIZE*3);
            name = "l";
        }
        return new Form(a, b, c, d, e, name);
    }
}