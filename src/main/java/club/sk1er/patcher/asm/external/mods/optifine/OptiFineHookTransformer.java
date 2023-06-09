package club.sk1er.patcher.asm.external.mods.optifine;

import club.sk1er.patcher.tweaker.ClassTransformer;
import club.sk1er.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class OptiFineHookTransformer implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"club.sk1er.patcher.hooks.OptiFineHook"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        if (ClassTransformer.smoothFontDetected) {
            return;
        }

        for (MethodNode method : classNode.methods) {
            final String methodName = method.name;
            if (methodName.equals("getOptifineBoldOffset")) {
                method.instructions.clear();
                final InsnList insns = new InsnList();
                insns.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insns.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/FontRenderer", "offsetBold", "F"));
                insns.add(new InsnNode(Opcodes.FRETURN));
                method.instructions.add(insns);
            } else if (methodName.equals("getCharWidth")) {
                method.instructions.clear();
                final InsnList insns = new InsnList();
                insns.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insns.add(new VarInsnNode(Opcodes.ILOAD, 2));
                insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/gui/FontRenderer", "getCharWidthFloat", "(C)F", false));
                insns.add(new InsnNode(Opcodes.FRETURN));
                method.instructions.add(insns);
            }
        }
    }
}
