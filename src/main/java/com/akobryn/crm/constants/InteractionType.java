package com.akobryn.crm.constants;

import lombok.Getter;

@Getter
public enum InteractionType {
    ADDING("Adding"),
    DELETING("Deleting"),
    UPDATING("Updating");

    private final String displayName;

    InteractionType(String displayName) {
        this.displayName = displayName;
    }

}
