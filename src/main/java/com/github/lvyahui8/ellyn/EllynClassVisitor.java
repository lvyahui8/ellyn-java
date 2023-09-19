package com.github.lvyahui8.ellyn;


import com.github.lvyahui8.ellyn.plugin.EllynLocal;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class EllynClassVisitor extends ClassVisitor {
    public EllynClassVisitor(ClassVisitor classVisitor) {
        super(Constants.asmVersion, classVisitor);
    }

    static class EllynMethodVisitor extends MethodVisitor {
        Label label0 = new Label();
        Label label1 = new Label();
        Label label2 = new Label();
        String name;

        public EllynMethodVisitor(String name,MethodVisitor methodVisitor) {
            super(Constants.asmVersion, methodVisitor);
            this.name =name;
        }

        @Override
        public void visitCode() {
            super.visitCode();
//                mv.visitVarInsn("");
            MethodVisitor methodVisitor = mv;
            methodVisitor.visitTryCatchBlock(label0, label1, label2, null);
            methodVisitor.visitLdcInsn(name);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/github/lvyahui8/ellyn/plugin/EllynLocal", "push", "(Ljava/lang/String;)V", false);
            methodVisitor.visitLabel(label0);
        }

        @Override
        public void visitEnd() {
            MethodVisitor methodVisitor = mv;
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLdcInsn(name);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/github/lvyahui8/ellyn/plugin/EllynLocal", "pop", "(Ljava/lang/String;)V", false);
            Label label3 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label3);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
            methodVisitor.visitVarInsn(ASTORE, 1);
            methodVisitor.visitLdcInsn(name);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/github/lvyahui8/ellyn/plugin/EllynLocal", "pop", "(Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitInsn(ATHROW);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 2);
            methodVisitor.visitEnd();
            super.visitEnd();
        }


    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor nextMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new EllynMethodVisitor(name,nextMethodVisitor);
    }
}
