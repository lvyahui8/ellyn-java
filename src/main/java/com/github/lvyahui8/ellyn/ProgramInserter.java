package com.github.lvyahui8.ellyn;

import com.github.lvyahui8.ellyn.plugin.Method;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgramInserter {
    private static final String DEFAULT_INCLUDES = "**";
    private static final String DEFAULT_EXCLUDES = "";

    final AtomicInteger methodId = new AtomicInteger(0);
    Map<Integer, Method> methodMap = new ConcurrentHashMap<>();

    String [] classpathList;

    public ProgramInserter(String ... classpathList) {
        this.classpathList = classpathList;
    }

    public Method newMethod() {
        Method method = new Method();
        method.setId(methodId.incrementAndGet());
        methodMap.put(method.getId(),method);
        return method;
    }

    /**
     * 遍历classpath，对文件进行插桩
     * 参考： org.jacoco.maven.InstrumentMojo#executeMojo
     */
    public void analyse() {
        ConsoleLogger.info("start analyse");
        for (String classpath : this.classpathList) {
            List<String> fileNames;
            try {
                fileNames = FileUtils.getFileNames(new File(classpath), DEFAULT_INCLUDES, DEFAULT_EXCLUDES,false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (String fileName : fileNames) {
                try {
                    InputStream inputStream = Files.newInputStream(Paths.get(fileName));
                    OutputStream outputStream = Files.newOutputStream(Paths.get(fileName));
                    enhanceClass(inputStream,outputStream);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public void enhanceClass(String className) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        EllynClassVisitor visitor = new EllynClassVisitor(this,writer);
        ClassReader reader = new ClassReader(className);
        reader.accept(visitor,0);
        Class<?> clazz = Class.forName(className);
        String dir = clazz.getResource("").getPath();
        IOUtils.write(writer.toByteArray(),
                org.apache.commons.io.FileUtils.newOutputStream(
                        new File(dir + File.separator + clazz.getSimpleName() + ".class"),
                        false));
    }

    public void enhanceClass(InputStream classInputStream, OutputStream outputStream) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        EllynClassVisitor visitor = new EllynClassVisitor(this,writer);
        ClassReader reader = new ClassReader(classInputStream);
        reader.accept(visitor,0);
        IOUtils.write(writer.toByteArray(), outputStream);
    }

    public void storeMethods() {

    }
}
