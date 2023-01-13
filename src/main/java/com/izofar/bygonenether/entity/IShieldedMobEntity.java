package com.izofar.bygonenether.entity;

public interface IShieldedMobEntity {
    boolean isShieldDisabled();
    void startUsingShield();
    void stopUsingShield();
}
