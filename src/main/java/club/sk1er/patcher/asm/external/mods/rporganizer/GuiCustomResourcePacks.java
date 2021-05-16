/*
 * Copyright © 2020 by Sk1er LLC
 *
 * All rights reserved.
 *
 * Sk1er LLC
 * 444 S Fulton Ave
 * Mount Vernon, NY
 * sk1er.club
 */

package club.sk1er.patcher.asm.external.mods.rporganizer;

import club.sk1er.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;

public class GuiCustomResourcePacks implements PatcherTransformer {
    /**
     * The class name that's being transformed
     *
     * @return the class name
     */
    @Override
    public String[] getClassName() {
        return new String[]{
            "chylex.respack.gui.GuiCustomResourcePacks",
            "me.aycy.resourcepackmanager.gui.screens.GuiResourcePacks"
        };
    }

    /**
     * Perform any asm in order to transform code
     *
     * @param classNode the transformed class node
     * @param name      the transformed class name
     */
    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            final String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("actionPerformed") || methodName.equals("func_146284_a")) {
                final ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

                while (iterator.hasNext()) {
                    final AbstractInsnNode next = iterator.next();

                    if (next instanceof MethodInsnNode && next.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        final String methodInsnName = mapMethodNameFromNode(next);
                        if (methodInsnName.equals("saveOptions") || methodInsnName.equals("func_74303_b")) {
                            methodNode.instructions.insertBefore(next.getNext(), new MethodInsnNode(Opcodes.INVOKESTATIC,
                                getHookClass("FallbackResourceManagerHook"),
                                "clearCache",
                                "()V",
                                false));
                            break;
                        }
                    }
                }

                break;
            }
        }
    }
}