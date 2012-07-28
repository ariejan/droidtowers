/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.happydroids.droidtowers.gamestate.GameSaveFactory;
import com.happydroids.utils.BackgroundTask;

import static com.badlogic.gdx.Application.ApplicationType.Android;

public class MigrateExistingGamesTask extends BackgroundTask {

  @Override
  protected void execute() throws Exception {
    FileHandle storageRoot = GameSaveFactory.getStorageRoot();

    if (Gdx.app.getType().equals(Android)) {
      FileHandle noMedia = storageRoot.child(".nomedia");
      if (!noMedia.exists()) {
        noMedia.writeString("", false);
      }
    }

    FileHandle[] localSaveFiles = storageRoot.list();
    upgradeLocalFiles(localSaveFiles);
  }

  private void upgradeLocalFiles(FileHandle[] files) {
    if (files != null && files.length > 0) {
      for (FileHandle file : files) {
        if (file.name().endsWith(".json")) {
          GameSaveFactory.upgradeGameSave(file.read(), file.name());
        }
      }
    }
  }
}
