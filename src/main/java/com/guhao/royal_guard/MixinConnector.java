//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.guhao.royal_guard;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {
    public MixinConnector() {
    }

    public void connect() {
        Mixins.addConfiguration("royal_guard.mixins.json");
    }
}
