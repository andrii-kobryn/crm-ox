package com.akobryn.crm.constants;

import lombok.Getter;

@Getter
public enum TaskStatus {
    OPEN("Open"),
    IN_PROGRESS("In progress"),
    COMPLETED("Completed");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

}
