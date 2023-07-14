package it.saimao.tmk_typing_tutor.node;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class HightlightTextField extends TextField {
    private TextFlow textFlow;

    public HightlightTextField() {
        textFlow = new TextFlow();
    }

    public void setText(String str, int index) {
        textFlow.getChildren().clear();
        for (int i = 0; i < str.length(); i ++) {
            char c = str.charAt(i);
            Text character = new Text(String.valueOf(c));
            if (i == index) {
                character.setFill(Color.RED);
            }
            textFlow.getChildren().add(character);
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (!getChildren().contains(textFlow)) {
            getChildren().add(textFlow);
        }
    }
}
