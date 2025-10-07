package it.saimao.tmk_typing_tutor.model;

import javafx.scene.image.Image;

public class Hand {
    private Image leftHand;
    private Image rightHand;

    public Hand() {
        this.leftHand = new Image(getClass().getResourceAsStream("/images/left_hand.png"));
        this.rightHand = new Image(getClass().getResourceAsStream("/images/right_hand.png"));
    }

    public Image getLeftHand() {
        return leftHand;
    }

    public void setLeftHand(String leftHandString) {
        this.leftHand = new Image(getClass().getResourceAsStream("/images/" + leftHandString + ".png"));
    }

    public Image getRightHand() {
        return rightHand;
    }

    public void setRightHand(String rightHandString) {
        this.rightHand = new Image(getClass().getResourceAsStream("/images/" + rightHandString + ".png"));
    }
}
