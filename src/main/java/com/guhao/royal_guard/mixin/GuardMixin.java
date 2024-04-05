//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.guhao.royal_guard.mixin;

import com.guhao.royal_guard.Royal_Guard;
import com.guhao.royal_guard.Sounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

import javax.annotation.Nullable;

@Mixin(
        value = {ParryingSkill.class},
        priority = 10000
)
public abstract class GuardMixin extends GuardSkill {
    @Unique
    int rg;
//    @Final
//    @Shadow
//    private static SkillDataManager.SkillDataKey<Integer> LAST_ACTIVE;

    public GuardMixin(GuardSkill.Builder builder) {
        super(builder);
        this.rg = Royal_Guard.rg.get();
    }

    @Shadow(remap = false)
    @Nullable
    protected abstract StaticAnimation getGuardMotion(PlayerPatch<?> var1, CapabilityItem var2, GuardSkill.BlockType var3);

    @Inject(
            at = {@At("HEAD")},
            method = {"guard"},
            cancellable = true, remap = false
    )
    public void mixin_guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        ci.cancel();
        if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
            DamageSource damageSource = event.getDamageSource();
            if (this.isBlockableSource(damageSource, true)) {
                ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
                boolean successParrying = playerentity.tickCount - container.getDataManager().getDataValue(SkillDataKeys.LAST_ACTIVE.get()) < this.rg;
                float penalty;
                event.getPlayerPatch().playSound(Sounds.RG.get(), 0.0F, 0.1F);
                EpicFightParticles.BLADE_RUSH_SKILL.get().spawnParticleWithArgument((ServerLevel) playerentity.level(), HitParticleType.MIDDLE_OF_ENTITIES, HitParticleType.MIDDLE_OF_ENTITIES, playerentity, damageSource.getDirectEntity());
                if (successParrying) {
                    event.setParried(true);
                    penalty = 0.0F;
                    knockback *= 0.4F;
                } else {
                    event.setParried(true);
                    penalty = 0.0F;
                    knockback *= 0.4F;
                }

                Entity var12 = damageSource.getDirectEntity();
                if (var12 instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity) var12;
                    knockback += (float) EnchantmentHelper.getKnockbackBonus(livingentity) * 0.1F;
                }

                event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                float consumeAmount = penalty * impact;
                event.getPlayerPatch().consumeStaminaAlways(consumeAmount);
                GuardSkill.BlockType blockType = successParrying ? BlockType.ADVANCED_GUARD : (event.getPlayerPatch().hasStamina(0.0F) ? BlockType.GUARD : BlockType.GUARD_BREAK);
                StaticAnimation animation = this.getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
                if (animation != null) {
                    event.getPlayerPatch().playAnimationSynchronized(animation, 0.0F);
                }

                if (blockType == BlockType.GUARD_BREAK) {
                    event.getPlayerPatch().playSound(EpicFightSounds.NEUTRALIZE_MOBS.get(), 3.0F, 0.0F, 0.1F);
                }

                this.dealEvent(event.getPlayerPatch(), event, advanced);
                return;
            }
        }

        super.guard(container, itemCapability, event, knockback, impact, false);
    }
}
