package br.com.rsdata.roaster;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class Generator {
	
	static final String quebra = ";\n";

	public static JavaInterfaceSource createRepositoryText(Class<?> entidade) {
		String nomeRepo = entidade.getSimpleName() + "Repository";
		
		StringBuffer out = new StringBuffer();
		//package
		out.append("package "+"br.com.rsdata.repo").append(quebra);
		
		//imports
		out.append("import "+entidade.getName()).append(quebra);
		out.append("import org.springframework.beans.factory.annotation.Qualifier").append(quebra);
		
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
		
		String superclass = "DefaultRepository<"+entidade.getSimpleName()+", "+idType+">";
		//define class/interface
		char c[] = nomeRepo.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		String qualifier = new String(c);
		out.append("@Qualifier(\""+qualifier+"\")").append("\n");
		
		out.append("public interface "+nomeRepo+" extends "+superclass+" {").append("\n");
		//body
		
		out.append("}").append("\n");
		
		System.out.println(Roaster.format(out.toString()));
		
		return (JavaInterfaceSource)Roaster.parse(out.toString());
	}
	
	public static JavaInterfaceSource createBusinessText(Class<?> entidade) {
		String nomeRepo = entidade.getSimpleName() + "Business";
		
		StringBuffer out = new StringBuffer();
		//package
		out.append("package "+"br.com.rsdata.business");
		out.append(quebra);
		
		//imports
		out.append("import "+"br.com.rsdata.dataseesmt.vo.paginacao.PaginacaoResultVO").append(quebra);
		out.append("import "+"br.com.rsdata.vo.Lista"+entidade.getSimpleName()+"SearchParamVO").append(quebra);
		out.append("import "+entidade.getName()).append(quebra);
		
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
		
		String superclass = "BaseBusinessPaginado<"+entidade.getSimpleName()+", "+idType+">";
		//define class/interface
		out.append("public interface "+nomeRepo+" extends "+superclass+" {").append("\n");
		//body
		
		
		String metodo = "PaginacaoResultVO<"+entidade.getSimpleName()+"> consulta (Lista"+entidade.getSimpleName()+"SearchParamVO queryVO, boolean paginacao)";
		out.append(metodo).append(quebra);
		
		metodo = "int buscaTotalRegistros (Lista"+entidade.getSimpleName()+"SearchParamVO queryVO)";
		out.append(metodo).append(quebra);
		
		metodo = "void gravar ("+entidade.getSimpleName()+" entidade)";
		out.append(metodo).append(quebra);
		
		out.append("}").append("\n");
		
		System.out.println(Roaster.format(out.toString()));
		
		return (JavaInterfaceSource)Roaster.parse(out.toString());
	}
	
	public static JavaClassSource createBusinessImplText(Class<?> entidade) {
		String nomeClass = entidade.getSimpleName() + "BusinessImpl";
		
		String nomeRepo = entidade.getSimpleName() + "Repository";
		
		StringBuffer out = new StringBuffer();
		//package
		out.append("package "+"br.com.rsdata.business.impl").append(quebra);
		
		//imports
		String[] imports = {"org.springframework.beans.factory.annotation.Autowired" ,
							"org.springframework.beans.factory.annotation.Qualifier" ,
							"org.springframework.stereotype.Service" ,
							"org.springframework.transaction.annotation.Transactional" ,
							"br.com.rsdata.business."+entidade.getSimpleName()+"Business" ,
							"br.com.rsdata.dataseesmt.model."+entidade.getSimpleName()+"" ,
							"br.com.rsdata.dataseesmt.vo.paginacao.PaginacaoResultVO" ,
							"br.com.rsdata.repo.DefaultRepository" ,
							"br.com.rsdata.repo."+entidade.getSimpleName()+"Repository" ,
							"br.com.rsdata.repo.spec.DefaultSpecification" ,
							"br.com.rsdata.vo.Lista"+entidade.getSimpleName()+"SearchParamVO"}; 
		for(String imp : imports) {
			out.append("import "+imp).append(quebra);
		}
		
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
		String superclass = "BaseBusinessPaginadoImpl<"+entidade.getSimpleName()+", "+idType+">";
		String interfaceBusiness = entidade.getSimpleName() + "Business";
		//define class/interface
		out.append("@Service").append("\n");
		out.append("@Transactional").append("\n");
		out.append("public class "+nomeClass+" extends "+superclass+" implements "+interfaceBusiness+" {").append("\n");
		
		//body
		out.append("@Autowired").append("\n");
		out.append("private "+entidade.getSimpleName()+"Repository repository").append(quebra);
		
		char c[] = nomeRepo.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		nomeRepo = new String(c);
		
		out.append("@Autowired").append("\n");
		String metodo = "public "+nomeClass+" (@Qualifier(\""+nomeRepo+"\") DefaultRepository<"+entidade.getSimpleName()+", "+idType+"> defaultRepository) {";
		out.append(metodo).append("\n");
		String corpo = "super(defaultRepository)";
		out.append(corpo).append(quebra);
		out.append("}").append("\n");
		
		metodo = "public PaginacaoResultVO<"+entidade.getSimpleName()+"> consulta (Lista"+entidade.getSimpleName()+"SearchParamVO queryVO, boolean paginacao) {";
		out.append(metodo).append("\n");
		corpo = "return consultaPaginada(queryVO, new DefaultSpecification<"+entidade.getSimpleName()+">(queryVO), paginacao)";
		out.append(corpo).append(quebra);
		out.append("}").append("\n");
		
		metodo = "public int buscaTotalRegistros (Lista"+entidade.getSimpleName()+"SearchParamVO queryVO) {";
		out.append(metodo).append("\n");
		corpo = "return new Long(total(new DefaultSpecification<"+entidade.getSimpleName()+">(queryVO))).intValue()";
		out.append(corpo).append(quebra);
		out.append("}").append("\n");
		
		metodo = "public void gravar ("+entidade.getSimpleName()+" entidade) {";
		out.append(metodo).append("\n");
		corpo = "this.repository.save(entidade)";
		out.append(corpo).append(quebra);
		out.append("}").append("\n");
		
		out.append("}").append("\n");
		
		System.out.println(Roaster.format(out.toString()));
		
		return (JavaClassSource)Roaster.parse(out.toString());
	}
	
	public static JavaClassSource createSearchParamVOText(Class<?> entidade) {
		String nomeClass = "Lista"+ entidade.getSimpleName() + "SearchParamVO";
		
		StringBuffer out = new StringBuffer();
		//package
		out.append("package "+"br.com.rsdata.vo").append(quebra);
		
		//imports
		out.append("import br.com.rsdata.dataseesmt.vo.paginacao.PaginacaoSearchParamVO").append(quebra);
		
		String superclass = "PaginacaoSearchParamVO";
		//define class/interface
		out.append("public class "+nomeClass+" extends "+superclass+" {").append("\n");
		//body
		
		out.append("}").append("\n");
		
//		System.out.println(Roaster.format(out.toString()));
		
		return (JavaClassSource)Roaster.parse(out.toString());
	}
	
}
