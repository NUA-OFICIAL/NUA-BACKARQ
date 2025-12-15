package com.nua.core.base.entities.enums;

public enum Roles {
    USER(1), CREATOR(2), REVISOR(3), ADMIN(4);

    private final int level;

    Roles(int level) { this.level = level; }

    public int level() { return level; }
}
