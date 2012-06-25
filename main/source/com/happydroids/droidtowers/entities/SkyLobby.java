/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.entities;

import com.happydroids.droidtowers.grid.GameGrid;
import com.happydroids.droidtowers.types.RoomType;

public class SkyLobby extends Room {
  public SkyLobby(RoomType roomType, GameGrid gameGrid) {
    super(roomType, gameGrid);
  }

  @Override
  public void updatePopulation() {
    // do nothing!
  }

  @Override
  public int getCoinsEarned() {
    return 0;
  }

  @Override
  public int getCurrentResidency() {
    return 0;
  }

  @Override
  public float getDesirability() {
    return 0f;
  }

  @Override
  public float getNoiseLevel() {
    return 0f;
  }

  @Override
  public boolean hasResident() {
    return false;
  }
}