package com.github.lvyahui8.ellyn.plugin;

public class EllynLocal {
    public static void push(int methodId) {
        CallContext context = getContext();
        if (! context.stack.isEmpty()) {
            Integer top = context.stack.peek();
            context.graph.add(top,methodId);
        }
        context.stack.push(methodId);
    }

    public static void pop(int methodId) {
        CallContext context = getContext();
        Integer top = context.stack.peek();
        if (top != methodId) {
            return;
        }
        context.stack.pop();
    }

    public static final ThreadLocal<CallContext> callCtx = new ThreadLocal<>();

    public static CallContext getContext() {
        CallContext context = callCtx.get();
        if (context != null) {
            return context;
        }
        context = new CallContext();
        callCtx.set(context);
        return context;
    }
}
