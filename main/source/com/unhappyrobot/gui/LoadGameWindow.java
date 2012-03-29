package com.unhappyrobot.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.Scaling;
import com.unhappyrobot.TowerConsts;
import com.unhappyrobot.TowerGame;
import com.unhappyrobot.gamestate.GameSave;
import com.unhappyrobot.scenes.TowerScene;

import java.io.IOException;

public class LoadGameWindow extends TowerWindow {
  public LoadGameWindow(Stage stage, Skin skin) {
    super("Load a saved game", stage, skin);

    defaults().top().left().pad(5);

    FileHandle storage = Gdx.files.external(TowerConsts.GAME_SAVE_DIRECTORY);
    FileHandle[] files = storage.list();


    Table gameFiles = new Table();
    gameFiles.defaults();

    if (files != null && files.length > 0) {
      for (FileHandle file : files) {
        if (file.name().endsWith(".json")) {
          gameFiles.row();
          gameFiles.add(makeGameFileRow(file));
        }
      }
    } else {
      gameFiles.add(LabelStyle.Default.makeLabel("No saved games were found on this device."));
    }

    WheelScrollFlickScrollPane scrollPane = new WheelScrollFlickScrollPane();
    scrollPane.setWidget(gameFiles);
    add(scrollPane).maxWidth(500).maxHeight(300).minWidth(400);
  }

  private Table makeGameFileRow(final FileHandle gameSave) {
    FileHandle imageFile = Gdx.files.external(TowerConsts.GAME_SAVE_DIRECTORY + gameSave.name() + ".png");


    Actor imageActor;
    if (imageFile.exists()) {
      imageActor = new Image(new Texture(imageFile), Scaling.fit, Align.TOP);
    } else {
      imageActor = LabelStyle.Default.makeLabel("No image.");
    }


    Table fileRow = new Table();
    fileRow.defaults().pad(5).width(360);
    fileRow.row().top().left();
    fileRow.add(imageActor).top().left().width(100);
    fileRow.add(makeGameFileInfoBox(gameSave)).top().left().fill();

    return fileRow;
  }

  private Table makeGameFileInfoBox(final FileHandle savedGameFile) {
    TextButton launchButton = new TextButton("Play", skin);
    launchButton.setClickListener(new ClickListener() {
      public void click(Actor actor, float x, float y) {
        dismiss();
        try {
          TowerGame.changeScene(TowerScene.class, GameSave.readFile(savedGameFile));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    Table box = new Table();
    box.defaults().top().left().expand();
    box.row();
    box.add(LabelStyle.Default.makeLabel(savedGameFile.nameWithoutExtension())).top().left();
    box.add(launchButton).top().right();

    return box;
  }
}