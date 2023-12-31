package com.github.lvyahui8.ellyn;


import com.github.lvyahui8.ellyn.plugin.Method;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class EllynClassVisitor extends ClassVisitor {

    ProgramInserter inserter;

    public EllynClassVisitor(ProgramInserter inserter, ClassVisitor classVisitor) {
        super(Constants.asmVersion, classVisitor);
        this.inserter = inserter;
    }

    static class EllynMethodVisitor extends MethodVisitor {
        Label tryStart = new Label();
        Label tryEnd = new Label();
        Label catchBlock = new Label();

        Method method;
        public EllynMethodVisitor(Method method, MethodVisitor methodVisitor) {
            super(Constants.asmVersion, methodVisitor);
            this.method = method;
        }

        @Override
        public void visitCode() {
            super.visitCode();
//                mv.visitVarInsn("");
            MethodVisitor methodVisitor = mv;
            // type 表示拦截的异常类型，为null时，表示拦截类型为所有exception，其实也就是finally
            methodVisitor.visitTryCatchBlock(tryStart, tryEnd, catchBlock, null);
            methodVisitor.visitLdcInsn(method.getId());
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/github/lvyahui8/ellyn/plugin/EllynLocal", "push", "(Ljava/lang/String;)V", false);
            methodVisitor.visitLabel(tryStart);
        }

        @Override
        public void visitEnd() {
            MethodVisitor methodVisitor = mv;
            methodVisitor.visitLabel(tryEnd);
            // 将常量methodName推送到栈顶
            methodVisitor.visitLdcInsn(method.getId());
            // 取栈顶参数调用pop方法
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/github/lvyahui8/ellyn/plugin/EllynLocal", "pop", "(Ljava/lang/String;)V", false);
            Label returnLabel = new Label();
            // 无条件跳转到return
            methodVisitor.visitJumpInsn(GOTO, returnLabel);
            // 定义catch block（label），当有异常发生时，执行流会被跳转到catch block
            methodVisitor.visitLabel(catchBlock);
            // 栈帧快照。F_SAME1
            // F_SAME1: A compressed frame with exactly the same locals as the previous frame and with a single value on the stack.
            // 局部变量表与前一帧完全一直(不新增任何局部变量) and 操作数栈有1个元素。
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
            // 弹出栈顶存入到本地变量1
            methodVisitor.visitVarInsn(ASTORE, 1);
            // 将方法名称压栈
            methodVisitor.visitLdcInsn(method.getId());
            // 调用pop方法
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/github/lvyahui8/ellyn/plugin/EllynLocal", "pop", "(Ljava/lang/String;)V", false);
            // 将本地变量1压入栈顶
            methodVisitor.visitVarInsn(ALOAD, 1);
            // 抛出栈顶
            methodVisitor.visitInsn(ATHROW);
            // 声明return代码块
            methodVisitor.visitLabel(returnLabel);
            // 帧快照
            // F_SAME:  A compressed frame with exactly the same locals as the previous frame and with an empty stack.
            // 局部变量表与前一帧完全一致(不新增任何局部变量) and 操作数栈为空
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(RETURN);
            super.visitEnd();
        }


    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor nextMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        Method method = inserter.newMethod();
        method.setFullName(signature);
        method.setName(name);
        return new EllynMethodVisitor(method,nextMethodVisitor);
    }
}
