package com.github.lvyahui8.ellyn;

import com.github.lvyahui8.ellyn.plugin.EllynLocal;
import com.github.lvyahui8.ellyn.proj.TestApp;
import junit.framework.TestCase;
import org.objectweb.asm.util.ASMifier;

public class ProgramInserterTest extends TestCase {


    public void testEnhanceClass() throws Exception {
        ProgramInserter inserter = new ProgramInserter();
        inserter.enhanceClass(TestApp.class.getName());
    }

    public static class TestClass {
        public static void main(String[] args) {
            EllynLocal.push(1);
            try {
                System.out.println("hello world");
            } finally {
                EllynLocal.pop(1);
            }
        }
    }

    public void testASMifierEllynLocal() throws Exception {
//        ASMifier.main(new String[]{TestClass.class.getName()});
        ASMifier.main(new String[]{"-nodebug",TestClass.class.getName()});
    }
}