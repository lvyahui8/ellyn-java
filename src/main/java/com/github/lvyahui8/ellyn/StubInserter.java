package com.github.lvyahui8.ellyn;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;

public class StubInserter {
    public void enhanceClass(String className) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        EllynClassVisitor visitor = new EllynClassVisitor(writer);
        ClassReader reader = new ClassReader(className);
        reader.accept(visitor,0);
        Class<?> clazz = Class.forName(className);
        String dir = clazz.getResource("").getPath();
        IOUtils.write(writer.toByteArray(),
                FileUtils.newOutputStream(
                        new File(dir + File.separator + clazz.getSimpleName() + ".class"),
                        false));
    }
}
