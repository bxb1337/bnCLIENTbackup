package net.AzureWare.utils.handler;

import org.lwjgl.input.Keyboard;

public class KeyInputHandler {
    public boolean clicked;
    private int key;

    public KeyInputHandler(int key) {
        this.key = key;
    }

    public boolean canExcecute() {
        if (Keyboard.isKeyDown((int)this.key)) {
            if (!this.clicked) {
                this.clicked = true;
                return true;
            }
        } else {
            this.clicked = false;
        }
        return false;
    }
}

