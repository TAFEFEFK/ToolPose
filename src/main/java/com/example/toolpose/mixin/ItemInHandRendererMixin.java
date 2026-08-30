package com.example.toolpose.mixin;

import com.example.toolpose.config.ToolPoseConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(method = "applyItemArmTransform", at = @At("TAIL"))
    private void onApplyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float inverseArmHeight, CallbackInfo ci) {
        ToolPoseConfig config = ToolPoseConfig.getInstance();
        if (!config.enabled) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStack stack = (arm == mc.player.getMainArm()) ? mc.player.getMainHandItem() : mc.player.getOffhandItem();
        if (stack.isEmpty()) return;

        boolean isLeft = arm == HumanoidArm.LEFT;

        ToolPoseConfig.Transform categoryTransform = getCategoryTransform(stack, config, isLeft);
        if (categoryTransform == null) return;

        float offsetX, offsetY, offsetZ, rotationX, rotationY, rotationZ, scale;

        if (config.useCategoryTransforms && categoryTransform.enabled) {
            offsetX = categoryTransform.offsetX;
            offsetY = categoryTransform.offsetY;
            offsetZ = categoryTransform.offsetZ;
            rotationX = categoryTransform.rotationX;
            rotationY = categoryTransform.rotationY;
            rotationZ = categoryTransform.rotationZ;
            scale = categoryTransform.scale;
        } else {
            if (isLeft) {
                offsetX = config.offsetXLeft;
                offsetY = config.offsetYLeft;
                offsetZ = config.offsetZLeft;
                rotationX = config.rotationXLeft;
                rotationY = config.rotationYLeft;
                rotationZ = config.rotationZLeft;
                scale = config.scaleLeft;
            } else {
                offsetX = config.offsetX;
                offsetY = config.offsetY;
                offsetZ = config.offsetZ;
                rotationX = config.rotationX;
                rotationY = config.rotationY;
                rotationZ = config.rotationZ;
                scale = config.scale;
            }
        }

        int invert = isLeft ? -1 : 1;
        poseStack.translate(-invert * 0.56F, 0.52F + inverseArmHeight * 0.6F, 0.72F);

        poseStack.translate(offsetX, offsetY, offsetZ);

        // Yaw, pitch, roll order for independent rotations
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));

        // Scaling
        poseStack.scale(scale, scale, scale);
    }

    @Inject(method = "swingArm", at = @At("HEAD"), cancellable = true)
    private void onSwingArm(float attack, PoseStack poseStack, int invert, HumanoidArm arm, CallbackInfo ci) {
        if (shouldCustomAnimate(arm)) {
            ci.cancel();
            applyCustomSwingAnimation(attack, poseStack, invert, arm);
        }
    }

    @Inject(method = "applyItemArmAttackTransform", at = @At("HEAD"), cancellable = true)
    private void onApplyItemArmAttackTransform(PoseStack poseStack, HumanoidArm arm, float attackValue, CallbackInfo ci) {
        if (shouldCustomAnimate(arm)) {
            ci.cancel();
            int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
            applyCustomSwingAnimation(attackValue, poseStack, invert, arm);
        }
    }

    private boolean shouldCustomAnimate(HumanoidArm arm) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        ItemStack stack = (arm == mc.player.getMainArm()) ? mc.player.getMainHandItem() : mc.player.getOffhandItem();
        if (stack.isEmpty()) return false;

        if (stack.is(ItemTags.SWORDS) || stack.is(ItemTags.AXES) || stack.is(ItemTags.PICKAXES)
                || stack.is(ItemTags.SHOVELS) || stack.is(ItemTags.HOES)) {
            return true;
        }

        String className = stack.getItem().getClass().getSimpleName().toLowerCase();
        return className.contains("sword") || className.contains("axe") || className.contains("pickaxe")
                || className.contains("shovel") || className.contains("hoe");
    }

    private void applyCustomSwingAnimation(float attack, PoseStack poseStack, int invert, HumanoidArm arm) {
        ToolPoseConfig config = ToolPoseConfig.getInstance();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStack stack = (arm == mc.player.getMainArm()) ? mc.player.getMainHandItem() : mc.player.getOffhandItem();
        if (stack.isEmpty()) return;

        boolean isLeft = arm == HumanoidArm.LEFT;
        ToolPoseConfig.Transform categoryTransform = getCategoryTransform(stack, config, isLeft);

        float intensity;
        if (config.useCategoryTransforms && categoryTransform != null && categoryTransform.enabled) {
            intensity = categoryTransform.swingIntensity;
        } else {
            intensity = isLeft ? config.swingIntensityLeft : config.swingIntensity;
        }

        if (intensity <= 0.0f) return;

        float ySwingRotation = Mth.sin(attack * attack * (float) Math.PI);
        float xzSwingRotation = Mth.sin(Mth.sqrt(attack) * (float) Math.PI);

        poseStack.mulPose(Axis.YP.rotationDegrees(invert * (45.0F + ySwingRotation * -20.0F) * intensity));
        poseStack.mulPose(Axis.ZP.rotationDegrees(invert * xzSwingRotation * -20.0F * intensity));
        poseStack.mulPose(Axis.XP.rotationDegrees(xzSwingRotation * -80.0F * intensity));
        poseStack.mulPose(Axis.YP.rotationDegrees(invert * -45.0F * intensity));
    }

    private ToolPoseConfig.Transform getCategoryTransform(ItemStack stack, ToolPoseConfig config, boolean isLeft) {
        Item item = stack.getItem();

        if (stack.is(ItemTags.SWORDS)) return isLeft ? config.swordLeft : config.sword;
        if (stack.is(ItemTags.AXES)) return isLeft ? config.axeLeft : config.axe;
        if (stack.is(ItemTags.PICKAXES)) return isLeft ? config.pickaxeLeft : config.pickaxe;
        if (stack.is(ItemTags.SHOVELS)) return isLeft ? config.shovelLeft : config.shovel;
        if (stack.is(ItemTags.HOES)) return isLeft ? config.hoeLeft : config.hoe;

        String className = item.getClass().getSimpleName().toLowerCase();
        if (className.contains("sword")) return isLeft ? config.swordLeft : config.sword;
        if (className.contains("axe")) return isLeft ? config.axeLeft : config.axe;
        if (className.contains("pickaxe")) return isLeft ? config.pickaxeLeft : config.pickaxe;
        if (className.contains("shovel")) return isLeft ? config.shovelLeft : config.shovel;
        if (className.contains("hoe")) return isLeft ? config.hoeLeft : config.hoe;

        if (stack.has(DataComponents.FOOD)) return isLeft ? config.foodLeft : config.food;

        if (className.contains("block")) return isLeft ? config.blockLeft : config.block;

        if (BuiltInRegistries.ITEM.getKey(item).getPath().contains("totem")) return isLeft ? config.totemLeft : config.totem;

        if (className.contains("shield")) return isLeft ? config.shieldLeft : config.shield;

        if (className.contains("crossbow")) return isLeft ? config.crossbowLeft : config.crossbow;
        if (className.contains("bow")) return isLeft ? config.bowLeft : config.bow;

        return null;
    }
}