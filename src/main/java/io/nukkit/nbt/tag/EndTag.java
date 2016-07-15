package io.nukkit.nbt.tag;

import io.nukkit.nbt.stream.NBTInputStream;
import io.nukkit.nbt.stream.NBTOutputStream;

import java.io.IOException;

public class EndTag extends Tag {

    public EndTag() {
        super(null);
    }

    @Override
    void load(NBTInputStream dis) throws IOException {
    }

    @Override
    void write(NBTOutputStream dos) throws IOException {
    }

    @Override
    public byte getId() {
        return TAG_End;
    }

    @Override
    public String toString() {
        return "EndTag";
    }

    @Override
    public Tag copy() {
        return new EndTag();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
