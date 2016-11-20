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

        if (transformedName.equalsIgnoreCase("net.minecraftforge.fml.common.network.FMLOutboundHandler")) {
            ClassNode worldClass = ASMUtils.createClassFromByteArray(classBytes);
            transformWrite(ASMUtils.getMethodFromClass(worldClass, "write", "(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;Lio/netty/channel/ChannelPromise;)V"));
            return ASMUtils.createByteArrayFromClass(worldClass, ClassWriter.COMPUTE_MAXS);
        }
        return classBytes;
    }

    private void transformWrite(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/network/handshake/NetworkDispatcher", "sendProxy", "(Lnet/minecraftforge/fml/common/network/internal/FMLProxyPacket;)V", false));
        needle.add(new LabelNode());
        needle.add(new LineNumberNode(-1, new LabelNode()));

        AbstractInsnNode location = ASMUtils.findLastNodeFromNeedle(method.instructions, needle);

        InsnList var = new InsnList();
        var.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/epoxide/sipo/profiler/ProfilerManager", "PROFILER_NETWORK", "Lxyz/epoxide/sipo/profiler/world/ProfilerNetwork;"));
        var.add(new VarInsnNode(Opcodes.ALOAD, 2));
        var.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraftforge/fml/common/network/internal/FMLProxyPacket"));
        var.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "xyz/epoxide/sipo/profiler/world/ProfilerNetwork", "addPacket", "(Lnet/minecraftforge/fml/common/network/internal/FMLProxyPacket;)V", false));
        var.add(new LabelNode());

        method.instructions.insert(location, var);
    }

    private void transformUpdateEntitiesEntity(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 2));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/Entity", ASMUtils.getAppropriateMapping("onUpdate", "func_70071_h_"), "()V", false));

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

        needle.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/util/ITickable", ASMUtils.getAppropriateMapping("update", "func_73660_a"), "()V", false));

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
