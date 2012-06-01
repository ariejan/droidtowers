/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.gui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.collect.Lists;
import com.happydroids.droidtowers.TowerAssetManager;
import com.happydroids.droidtowers.gamestate.server.*;
import com.happydroids.droidtowers.gui.friends.NonPlayerFriendItem;
import com.happydroids.droidtowers.gui.friends.PlayerFriendItem;
import com.happydroids.server.HappyDroidServiceCollection;
import com.happydroids.utils.BackgroundTask;

import java.util.List;

import static com.happydroids.droidtowers.platform.Display.scale;

public class FriendsListWindow extends ScrollableTowerWindow {

  private final TextureAtlas.AtlasRegion facebookIcon;
  private List<PlayerFriendItem> playerFriendRows;
  private List<PlayerFriendItem> nonPlayerFriendRows;
  private boolean playerFriendsFetched;
  private boolean nonPlayerFriendsFetched;
  private final CloudGameSave playerGameSave;

  public FriendsListWindow(Stage stage, CloudGameSave playerGameSave) {
    super("My Friends", stage);
    this.playerGameSave = playerGameSave;

    facebookIcon = TowerAssetManager.textureFromAtlas("facebook-logo", "hud/menus.txt");
    playerFriendRows = Lists.newArrayList();
    nonPlayerFriendRows = Lists.newArrayList();

    defaults().left().space(scale(6));

    add(FontManager.Roboto32.makeLabel("making friends :]"));

    NonPlayerFriendSearchBox friendSearchBox = new NonPlayerFriendSearchBox(this);
    setStaticHeader(friendSearchBox);

    new BackgroundTask() {
      private PlayerFriendCollection friendCollection;

      @Override
      protected void execute() {
        friendCollection = new PlayerFriendCollection();
        friendCollection.fetch();
      }

      @Override
      public synchronized void afterExecute() {
        processPlayerFriends(friendCollection);
      }
    }.run();

    new BackgroundTask() {
      private NonPlayerFriendCollection friendCollection;

      @Override
      protected void execute() {
        friendCollection = new NonPlayerFriendCollection();
        friendCollection.fetch();
      }

      @Override
      public synchronized void afterExecute() {
        processNonPlayerFriends(friendCollection);
      }
    }.run();
  }

  private void processNonPlayerFriends(HappyDroidServiceCollection<NonPlayerFriend> collection) {
    if (collection != null && !collection.isEmpty()) {
      for (NonPlayerFriend profile : collection.getObjects()) {
        PlayerFriendItem playerFriendItem = new NonPlayerFriendItem(profile, playerGameSave);
        playerFriendItem.createChildren(facebookIcon);
        nonPlayerFriendRows.add(playerFriendItem);
      }
    }

    nonPlayerFriendsFetched = true;
    updateViewWhenFinished();
  }

  private void processPlayerFriends(HappyDroidServiceCollection<PlayerProfile> collection) {
    playerFriendsFetched = true;

    if (collection != null && !collection.isEmpty()) {
      for (PlayerProfile profile : collection.getObjects()) {
        PlayerFriendItem playerFriendItem = new PlayerFriendItem(profile, playerGameSave);
        playerFriendItem.createChildren(facebookIcon);
        playerFriendRows.add(playerFriendItem);
      }
    }

    updateViewWhenFinished();
  }

  private void updateViewWhenFinished() {
    if (!playerFriendsFetched || !nonPlayerFriendsFetched) {
      return;
    }

    clear();

    row().fillX();
    add(FontManager.Roboto24.makeLabel("Friends playing Droid Towers")).expandX();
    row().fillX();
    add(new HorizontalRule()).expandX();


    if (!playerFriendRows.isEmpty()) {
      for (PlayerFriendItem friendRow : playerFriendRows) {
        row().fillX();
        add(friendRow).expandX();
      }
    } else {
      row().fillX();
      add(FontManager.Roboto18.makeLabel("You should invite some of your friends to play with.")).expandX();
    }

    row().fillX().padTop(scale(32));
    add(FontManager.Roboto24.makeLabel("Friends on Facebook")).expandX();
    row().fillX();
    add(new HorizontalRule()).expandX();
    if (!nonPlayerFriendRows.isEmpty()) {
      for (PlayerFriendItem friendRow : nonPlayerFriendRows) {
        row().fillX();
        add(friendRow).expandX();
      }
    } else {
      row().fillX();
      if (playerFriendRows.isEmpty()) {
        add(FontManager.Roboto18.makeLabel("Wow, terribly sorry to tell you this..\n\nBut you appear to have no friends.\n\n")).expandX();
      } else {
        add(FontManager.Roboto18.makeLabel("You have already invited everyone, thanks!")).expandX();
      }
    }

    shoveContentUp();
    content.invalidateHierarchy();
  }

  public List<PlayerFriendItem> getNonPlayerFriendRows() {
    return nonPlayerFriendRows;
  }

  public List<PlayerFriendItem> getPlayerFriendRows() {
    return playerFriendRows;
  }
}
