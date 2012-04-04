/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.unhappyrobot.types;

import com.badlogic.gdx.Gdx;

public class StairTypeFactory extends GridObjectTypeFactory<StairType> {
  private static StairTypeFactory instance;

  private StairTypeFactory() {
    super(StairType.class);

    parseTypesFile(Gdx.files.internal("params/stairs.json"));
  }

  public static StairTypeFactory instance() {
    if (instance == null) {
      instance = new StairTypeFactory();
    }

    return instance;
  }
}
