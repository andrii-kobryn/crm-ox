package com.akobryn.crm.constants;

import lombok.Getter;

@Getter
public class InteractionMessages {
    public static final String CLIENT_WAS_CREATED = "Клієнта з id: %s було створено";
    public static final String CONTACT_WAS_CREATED = "Контакт з id: %s було створено в клієнті з id: %s";
    public static final String CLIENT_WAS_UPDATED = "Клієнта з id: %s було оновлено";
    public static final String CONTACT_WAS_UPDATED = "Контакт з id: %s було оновлено в клієнті з id: %s";
    public static final String CONTACT_WAS_DELETED = "Контакт з id: %s було видалено в клієнті з id: %s";
}
