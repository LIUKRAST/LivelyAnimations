package net.frozenblock.liukrast.livelyanimations.data;

public class PlayerData {

    private float movY, sneakX, limbSwingOld, walkTime, swimTime, elytraTime;
    private PlayerData(float movY, float sneakX, float limbSwingOld, float walkTime, float swimTime, float elytraTime) {
        this.movY = movY;
        this.sneakX = sneakX;
        this.limbSwingOld = limbSwingOld;
        this.walkTime = walkTime;
        this.swimTime = swimTime;
        this.elytraTime = elytraTime;
    }

    public float getMovY() {
        return movY;
    }
    public void setMovY(float movY) {
        this.movY = movY;
    }

    public float getSneakX() {
        return sneakX;
    }

    public void setSneakX(float sneakX) {
        this.sneakX = sneakX;
    }

    public void setLimbSwingOld(float limbSwingOld) {
        this.limbSwingOld = limbSwingOld;
    }

    public float getLimbSwingOld() {
        return limbSwingOld;
    }

    public float getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(float walkTime) {
        this.walkTime = walkTime;
    }

    public float getSwimTime() {
        return swimTime;
    }

    public void setSwimTime(float swimTime) {
        this.swimTime = swimTime;
    }

    public float getElytraTime() {
        return elytraTime;
    }

    public void setElytraTime(float elytraTime) {
        this.elytraTime = elytraTime;
    }

    public static PlayerData generate() {
        return new PlayerData(0, 0, 0, 0, 0, 0);
    }
}
