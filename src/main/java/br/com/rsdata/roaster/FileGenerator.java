package br.com.rsdata.roaster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class FileGenerator {

	public static JavaInterfaceSource createRepositoryText(Class<?> entidade) throws Exception {
		JavaInterfaceSource generated = Generator.createRepositoryText(entidade);
		gravarinterface(generated);
		return generated;
	}
	
	public static JavaInterfaceSource createBusinessText(Class<?> entidade) throws Exception {
		JavaInterfaceSource generated = Generator.createBusinessText(entidade);
		gravarinterface(generated);
		return generated;
	}
	
	public static JavaClassSource createBusinessImplText(Class<?> entidade) throws Exception {
		JavaClassSource generated = Generator.createBusinessImplText(entidade);
		gravarClass(generated);
		return generated;
	}
	
	public static JavaClassSource createSearchParamVOText(Class<?> entidade) throws Exception {
		JavaClassSource generated = Generator.createSearchParamVOText(entidade);
		gravarClass(generated);
		return generated;
	}
	
	public static void gravarinterface(JavaInterfaceSource javaClass) throws Exception {
        File file = new File("src/main/java/" + javaClass.getPackage().replace(".", "/") + "/" + javaClass.getName() + ".java");
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(javaClass.toString());
        bw.close();
	}
	
	public static void gravarClass(JavaClassSource javaClass) throws Exception {
        File file = new File("src/main/java/" + javaClass.getPackage().replace(".", "/") + "/" + javaClass.getName() + ".java");
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(javaClass.toString());
        bw.close();
	}
}
