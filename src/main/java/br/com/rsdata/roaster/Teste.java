package br.com.rsdata.roaster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Id;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class Teste {
	
	static final String quebra = ";\n";
	

	public static void main(String[] args) {
		final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
		javaClass.setPackage("com.company.example").setName("Person");

		javaClass.addInterface(Serializable.class);
		javaClass.addField()
		  .setName("serialVersionUID")
		  .setType("long")
		  .setLiteralInitializer("1L")
		  .setPrivate()
		  .setStatic(true)
		  .setFinal(true);

		javaClass.addProperty(Integer.class.getSimpleName(), "id").setMutable(false);
		javaClass.addProperty(String.class.getSimpleName(), "firstName");
		javaClass.addProperty("String", "lastName");

		javaClass.addMethod()
		  .setConstructor(true)
		  .setPublic()
		  .setBody("this.id = id;")
		  .addParameter(Integer.class, "id");
		
		System.out.println(javaClass);
//		
//		//repo
//		try {
////			final JavaInterfaceSource javaClass = (JavaInterfaceSource)Roaster.parse(createRepositoryText(ES1060AmbienteTrabalho.class));
//			final JavaInterfaceSource javaClass = Generator.createRepositoryText(ES1060AmbienteTrabalho.class);
//	        File file = new File("src/main/java/" + javaClass.getPackage().replace(".", "/") + "/" + javaClass.getName() + ".java");
//	        file.getParentFile().mkdirs();
//	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//	        BufferedWriter bw = new BufferedWriter(fw);
//	        bw.write(javaClass.toString());
//	        bw.close();
//	    } catch (IOException e) {
//	    	System.out.println("repo problem:");
//	    	e.printStackTrace();
//	    }
//		//business interface
//		try {
////			final JavaInterfaceSource javaClass = (JavaInterfaceSource)Roaster.parse(createBusinessText(ES1060AmbienteTrabalho.class));
//			final JavaInterfaceSource javaClass = Generator.createBusinessText(ES1060AmbienteTrabalho.class);
//	        File file = new File("src/main/java/" + javaClass.getPackage().replace(".", "/") + "/" + javaClass.getName() + ".java");
//	        file.getParentFile().mkdirs();
//	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//	        BufferedWriter bw = new BufferedWriter(fw);
//	        bw.write(javaClass.toString());
//	        bw.close();
//	    } catch (IOException e) {
//	    	System.out.println("business problem:");
//	    	e.printStackTrace();
//	    }
//		//business impl
//		try {
////			final JavaClassSource javaClass = (JavaClassSource)Roaster.parse(createBusinessImplText(ES1060AmbienteTrabalho.class));
//			final JavaClassSource javaClass = Generator.createBusinessImplText(ES1060AmbienteTrabalho.class);
//	        File file = new File("src/main/java/" + javaClass.getPackage().replace(".", "/") + "/" + javaClass.getName() + ".java");
//	        file.getParentFile().mkdirs();
//	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//	        BufferedWriter bw = new BufferedWriter(fw);
//	        bw.write(javaClass.toString());
//	        bw.close();
//	    } catch (IOException e) {
//	    	System.out.println("business impl problem:");
//	    	e.printStackTrace();
//	    }
		
	}
	
	static String createRepository(Class<?> entidade) {
		
		final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
		String nomeRepo = entidade.getSimpleName() + "Repository";
		javaClass.setPackage("br.com.rsdata.repo").setName(nomeRepo);
		
		javaClass.addImport(entidade);
		
		String idType = "Long";
		try {
			for(Field id : entidade.getDeclaredFields()){
//				System.out.println(id.getName()+">"+id.isAnnotationPresent(Id.class));
				if (id.isAnnotationPresent(Id.class)) {
					idType = id.getType().getSimpleName();
					break;
				}
			}
			
		} catch (Exception e) {
			System.out.println("erro");
		}
		
		javaClass.setSuperType("DefaultRepository<"+entidade.getSimpleName()+", "+idType+">");

		System.out.println(javaClass);
		
		return javaClass.toString();
	}
	
	
	
	
	static String createBusiness(Class<?> entidade) {
		final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
		String nomeRepo = entidade.getSimpleName() + "Business";
		javaClass.setPackage("br.com.rsdata.business").setName(nomeRepo);
		javaClass.setAbstract(true);
		
		javaClass.addImport(entidade);
		
		String resultVO = "br.com.rsdata.dataseesmt.vo.paginacao.PaginacaoResultVO";
		javaClass.addImport(resultVO);
		String listaVO = "br.com.rsdata.vo.Lista"+entidade.getSimpleName()+"SearchParamVO";
		javaClass.addImport(listaVO);
		
		String idType = "Long";
		try {
			for(Field id : entidade.getDeclaredFields()){
//				System.out.println(id.getName()+">"+id.isAnnotationPresent(Id.class));
				if (id.isAnnotationPresent(Id.class)) {
					idType = id.getType().getSimpleName();
					break;
				}
			}
			
		} catch (Exception e) {
			System.out.println("erro");
		}
		
		javaClass.setSuperType("BaseBusinessPaginado<"+entidade.getSimpleName()+", "+idType+">");
		
		String metodo = "PaginacaoResultVO<"+entidade.getSimpleName()+"> consulta (Lista"+entidade.getSimpleName()+"SearchParamVO queryVO, boolean paginacao)";
		javaClass.addMethod(metodo);
		
		metodo = "int buscaTotalRegistros (Lista"+entidade.getSimpleName()+"SearchParamVO queryVO)";
		javaClass.addMethod(metodo);
		
		javaClass.addMethod()
		  .setName("gravar")
		  .setConstructor(false)
		  .addParameter(entidade, "entidade");

		System.out.println(javaClass);
		
		return javaClass.toString();
	}
	
	
	
	

}
