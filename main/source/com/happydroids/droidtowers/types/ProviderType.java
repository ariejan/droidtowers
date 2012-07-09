/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.types;

import java.util.Set;

public enum ProviderType {
  NONE,
  LOBBY,
  SKY_LOBBY,
  FOOD,
  ENTERTAINMENT,
  HOTEL_ROOMS,
  OFFICE_SERVICES,
  JANITORS,
  MAIDS,
  SECURITY,
  APARTMENT,
  CONDO,
  PENTHOUSE,
  STAIRS,
  ELEVATOR,
  SERVICE_ELEVATOR,
  RESTROOM,

  HOUSING(APARTMENT, CONDO, PENTHOUSE),
  COMMERCIAL(ENTERTAINMENT, FOOD, OFFICE_SERVICES, HOTEL_ROOMS),
  TRANSPORT(STAIRS, ELEVATOR),
  SERVICE(MAIDS, JANITORS, SECURITY, RESTROOM);

  private final ProviderType[] subTypes;

  ProviderType(ProviderType... subTypes) {
    this.subTypes = subTypes;
  }

  ProviderType() {
    subTypes = null;
  }

  public boolean hasSubTypes() {
    return subTypes != null;
  }

  public boolean matchesSubType(Set<ProviderType> typeSet) {
    if (subTypes != null) {
      for (ProviderType subType : subTypes) {
        if (typeSet.contains(subType)) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean matches(Set<ProviderType> types) {
    return types.contains(this) || matchesSubType(types);
  }
}
