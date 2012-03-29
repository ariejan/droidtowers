package com.unhappyrobot.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.unhappyrobot.TowerConsts;
import com.unhappyrobot.TowerGame;

public class RuntimeExceptionDialog extends Dialog {
  private static final String TAG = RuntimeExceptionDialog.class.getSimpleName();

  public RuntimeExceptionDialog(Throwable error) {
    this(TowerGame.getRootUiStage(), error);
  }

  public RuntimeExceptionDialog(Stage stage, Throwable error) {
    super(stage);

    setTitle("An unexpected error occurred!");

    String message = "Sorry, but something has gone wrong.\nSome anonymous data detailing the error has been sent to happydroids for analysis.\n\n";
    if (TowerConsts.DEBUG) {
      message += error;
    }

    setMessage(message);
    addButton(ResponseType.POSITIVE, "Dismiss", new OnClickCallback() {
      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
      }
    });

    Gdx.app.error(TAG, "Uncaught Exception!", error);
  }
}