package com.magz.tetris;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Form {
    Rectangle a;
    Rectangle b;
    Rectangle c;
    Rectangle d;
    Rectangle e;
    Color color;
    private String name;

    public int form = 1;

    public Form(Rectangle a, Rectangle b, Rectangle c, Rectangle d, Rectangle e, String name) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.name = name;

        switch (name) {
            case "p":
                color = Color.RED;
                break;
            case "x":
                color = Color.BLUE;
                break;
            case "f":
                color = Color.ORANGE;
                break;
            case "v":
                color = Color.FORESTGREEN;
                break;
            case "w":
                color = Color.PINK;
                break;
            case "y":
                color = Color.PURPLE;
                break;
            case "i":
                color = Color.INDIANRED;
                break;
            case "t":
                color = Color.YELLOW;
                break;
            case "z":
                color = Color.GREEN;
                break;
            case "u":
                color = Color.LIGHTPINK;
                break;
            case "n":
                color = Color.CYAN;
                break;
            case "l":
                color = Color.YELLOWGREEN;
                break;
            default:
                color = Color.GRAY;

        }
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
        this.e.setFill(color);
    }


    public String getName() {
        return name;
    }
    public Color getColor(){
        return color;
    }


    public void changeForm() {
        if (form != 4) {
            form++;
        } else {
            form = 1;
        }
    }
}