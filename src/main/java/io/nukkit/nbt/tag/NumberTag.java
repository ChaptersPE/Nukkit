package io.nukkit.nbt.tag;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class NumberTag<T extends Number> extends Tag {
    protected NumberTag(String name) {
        super(name);
    }

    public abstract T getNumber();

    public abstract void getNumber(T data);
}
