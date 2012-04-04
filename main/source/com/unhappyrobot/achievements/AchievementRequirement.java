/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.unhappyrobot.achievements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Predicate;
import com.sun.istack.internal.Nullable;
import com.unhappyrobot.entities.*;
import com.unhappyrobot.grid.GameGrid;
import com.unhappyrobot.types.CommercialType;
import com.unhappyrobot.types.ProviderType;
import com.unhappyrobot.types.RoomType;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class AchievementRequirement {
  private RequirementType type;
  private AchievementThing thing;
  private double amount;

  public boolean isCompleted() {
    switch (type) {
      case POPULATION:
        return Player.instance().getTotalPopulation() >= amount;

      case BUILD:
        return handleBuildRequirement();

      default:
        assert false;
        break;
    }

    return false;
  }

  private boolean handleBuildRequirement() {
    if (thing == null) {
      throw new RuntimeException(String.format("AchievementRequirement %s does not contain 'thing' parameter.", type));
    }

    if (!AchievementEngine.instance().hasGameGrid()) {
      return false;
    }
    GameGrid gameGrid = AchievementEngine.instance().getGameGrid();

    GuavaSet<GridObject> gridObjects = null;
    Predicate<GridObject> gridObjectPredicate = null;

    switch (thing) {
      case HOTEL_ROOM:
        gridObjects = gameGrid.getInstancesOf(Room.class, CommercialSpace.class);
        gridObjectPredicate = new Predicate<GridObject>() {
          public boolean apply(@Nullable GridObject gridObject) {
            return gridObject instanceof Room && ((RoomType) gridObject.getGridObjectType()).provides() == ProviderType.HOTEL_ROOMS;
          }
        };
        break;

      case COMMERCIAL_SPACE:
        gridObjects = gameGrid.getInstancesOf(CommercialSpace.class);
        gridObjectPredicate = new Predicate<GridObject>() {
          public boolean apply(@Nullable GridObject gridObject) {
            ProviderType providerType = ((CommercialType) gridObject.getGridObjectType()).provides();
            return gridObject instanceof CommercialSpace && (providerType == ProviderType.OFFICE_SERVICES || providerType == ProviderType.FOOD);
          }
        };
        break;
    }

    return gridObjects != null && gridObjectPredicate != null && gridObjects.filterBy(gridObjectPredicate).size() >= amount;
  }

  @Override
  public String toString() {
    return "AchievementRequirement{" +
                   "amount=" + amount +
                   ", type=" + type +
                   ", thing=" + thing +
                   '}';
  }
}
