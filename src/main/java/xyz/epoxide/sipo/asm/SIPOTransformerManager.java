package xyz.epoxide.sipo.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class SIPOTransformerManager implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        if (transformedName.equalsIgnoreCase("net.minecraft.world.World")) {
            ClassNode worldClass = ASMUtils.createClassFromByteArray(classBytes);
            transformUpdateEntitiesEntity(ASMUtils.getMethodFromClass(worldClass, ASMUtils.getAppropriateMapping("updateEntities", "func_72939_s"), "()V"));
            transformUpdateEntitiesTileEntity(ASMUtils.getMethodFromClass(worldClass, ASMUtils.getAppropriateMapping("updateEntities", "func_72939_s"), "()V"));
            return ASMUtils.createByteArrayFromClass(worldClass, ClassWriter.COMPUTE_MAXS);
        }
        return classBytes;
    }

    private void transformUpdateEntitiesEntity(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 2));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/Entity", "onUpdate", "()V", false));

        AbstractInsnNode location = ASMUtils.findFirstNodeFromNeedle(method.instructions, needle);

        InsnList var = new InsnList();
        var.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/epoxide/sipo/profiler/ProfilerManager", "PROFILER_ENTITY", "Lxyz/epoxide/sipo/profiler/world/ProfilerEntity;"));
        var.add(new VarInsnNode(Opcodes.ALOAD, 2));
        var.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "xyz/epoxide/sipo/profiler/world/ProfilerEntity", "initial", "(Lnet/minecraft/entity/Entity;)V", false));
        var.add(new LabelNode());

        method.instructions.insertBefore(location, var);
        location = ASMUtils.findLastNodeFromNeedle(method.instructions, needle);

        var.clear();
        var.add(new LabelNode());
        var.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/epoxide/sipo/profiler/ProfilerManager", "PROFILER_ENTITY", "Lxyz/epoxide/sipo/profiler/world/ProfilerEntity;"));
        var.add(new VarInsnNode(Opcodes.ALOAD, 2));
        var.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "xyz/epoxide/sipo/profiler/world/ProfilerEntity", "end", "(Lnet/minecraft/entity/Entity;)V", false));
        method.instructions.insert(location, var);
    }

    private void transformUpdateEntitiesTileEntity(MethodNode method) {
        InsnList needle = new InsnList();

        needle.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/util/ITickable", "update", "()V", false));

        AbstractInsnNode location = ASMUtils.findFirstNodeFromNeedle(method.instructions, needle);

        InsnList var = new InsnList();
        var.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/epoxide/sipo/profiler/ProfilerManager", "PROFILER_TILE_ENTITY", "Lxyz/epoxide/sipo/profiler/world/ProfilerTileEntity;"));
        var.add(new VarInsnNode(Opcodes.ALOAD, 2));
        var.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "xyz/epoxide/sipo/profiler/world/ProfilerTileEntity", "initial", "(Lnet/minecraft/tileentity/TileEntity;)V", false));
        var.add(new LabelNode());

        method.instructions.insertBefore(location, var);
        location = ASMUtils.findLastNodeFromNeedle(method.instructions, needle);

        var.clear();
        var.add(new LabelNode());
        var.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/epoxide/sipo/profiler/ProfilerManager", "PROFILER_TILE_ENTITY", "Lxyz/epoxide/sipo/profiler/world/ProfilerTileEntity;"));
        var.add(new VarInsnNode(Opcodes.ALOAD, 2));
        var.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "xyz/epoxide/sipo/profiler/world/ProfilerTileEntity", "end", "(Lnet/minecraft/tileentity/TileEntity;)V", false));
        method.instructions.insert(location, var);
    }
}
