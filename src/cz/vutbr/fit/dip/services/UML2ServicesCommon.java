package cz.vutbr.fit.dip.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class UML2ServicesCommon {

	protected static final String S_SPARKAPP = "Spark Application";
	protected static final String S_CODEBLOCK = "Code Block";
	protected static final String S_FILESOURCE = "File Source";
	protected static final String S_SQL = "SQL";
	protected static final String S_PARALLELIZE = "Parallelize";
	protected static final String S_JDBC = "JDBC";
	protected static final String S_RDDTRANSFORMATION = "RDD Transformation";
	protected static final String S_DATASETTRANSFORMATION = "Dataset Transformation";
	protected static final String S_RDDACTION = "RDD Action";
	protected static final String S_DATASETACTION = "Dataset Action";
	protected static final String S_FLOW = "Flow";
	protected static final String S_TUPLE = "Tuple";
	protected static final String S_VARIABLE = "Variable";
	protected static final String S_ITERATIONLOOP = "Iteration Loop";
	protected static final String S_CONDITIONALLOOP = "Conditional Loop";
	protected static final String S_ARGUMENT = "Argument";
	
	protected boolean isStereotype(Element e, String stereotypeName) {
		return e.getAppliedStereotypes().stream().anyMatch(x -> (x.getName().equals(stereotypeName)));
	}

	public String formatVariable(String var) {
		String res = var.replaceAll("\\s", "");
		return res.substring(0, 1).toLowerCase() + res.substring(1);
	}

	protected boolean isSource(Class node) {
		return node.getAppliedStereotypes().stream().anyMatch(x -> (x.getName().equals(S_PARALLELIZE)
				|| (x.getName().equals(S_FILESOURCE)) || (x.getName().equals(S_SQL)) 
				|| (x.getName().equals(S_JDBC))));
	}

	protected boolean isTransformation(Class node) {
		return node.getAppliedStereotypes().stream().anyMatch(
				x -> (x.getName().equals(S_RDDTRANSFORMATION) || (x.getName().equals(S_DATASETTRANSFORMATION))));
	}

	protected boolean isAction(Class node) {
		return node.getAppliedStereotypes().stream()
				.anyMatch(x -> (x.getName().equals(S_RDDACTION) || (x.getName().equals(S_DATASETACTION))));
	}

	public String generateType(Type type, Model model) {
		if (isStereotype(type, S_TUPLE)) {
			return generateTuple(type, model);
		}
		
		String packageName = "";
		if (!Objects.isNull(model) && !Objects.isNull(type.getPackage())) {
			if (type.getPackage().getName().equals(model.getName())
					|| type.getPackage().getName().equals("PrimitiveTypes")) {
				packageName = "";
			}
		}
		String name = "";
		if (Objects.isNull(type.getName())) {
			name = getTypeNameFromEstorage(type);
		} else {
			name = type.getName();
		}
		switch (name) {
		case "Integer":
			return packageName + "Long";
		case "Real":
			return packageName + "Double";
		}

		return packageName + name;
	}

	private String getTypeNameFromEstorage(Type t) {
		List<Field> fields = getFields(t);
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals("eStorage")) {
				try {
					String deep = Arrays.deepToString((Object[]) field.get(t));
					return deep.split("#")[1].substring(0, deep.split("#")[1].length() - 1);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return "null";
	}

	private <T> List<Field> getFields(T t) {
		List<Field> fields = new ArrayList<>();
		java.lang.Class<? extends Object> clazz = t.getClass();
		while (clazz != Object.class) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	private String generateTuple(Type type, Model model) {
		String result = "(";
		List<String> fields = new ArrayList<>();
		for (Object o : type.eContents()) {
			Property p = (Property) o;
			String typeStr = generateType(p.getType(), model);
			if (p.isMultivalued()) {
				typeStr = "Iterable[" + typeStr + "]";
			}
			fields.add(typeStr);
		}
		result += String.join(", ", fields);
		return result + ")";
	}
}
