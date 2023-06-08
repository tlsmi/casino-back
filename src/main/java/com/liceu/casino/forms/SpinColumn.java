package com.liceu.casino.forms;

public class SpinColumn {
    private int column;
    private String emoji;
    private int apuesta;

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public int getApuesta() {
        return apuesta;
    }

    public void setApuesta(int apuesta) {
        this.apuesta = apuesta;
    }

    @Override
    public String toString() {
        return "SpinColumn{" +
                "column=" + column +
                ", emoji='" + emoji + '\'' +
                ", apuesta=" + apuesta +
                '}';
    }
}
