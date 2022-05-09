package cz.vutbr.fit.dip.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;

public class UML2Services extends UML2ServicesCommon {

	private String getType(Type arg) {
		String type = generateType(arg, null);
		return type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
	}

	@SuppressWarnings("unchecked")
	public String generateArgs(Element e) {
		String result = "";
		List<DynamicEObjectImpl> arguments = null;
		List<Stereotype> stereotypes = e.getAppliedStereotypes();
		for (Stereotype stereotype : stereotypes) {
			if (stereotype.getName().equals(S_SPARKAPP)) {
				arguments = (List<DynamicEObjectImpl>) e.getValue(stereotype, "arguments");
			}
		}
		for (int i = 0; i < arguments.size(); i++) {
			result += "var ";
			result += arguments.get(i).dynamicGet(0);
			result += " = if (args.length > ";
			result += Integer.toString(i);
			result += ") args(";
			result += Integer.toString(i);
			result += ").to";
			PrimitiveType type = (PrimitiveType) arguments.get(i).dynamicGet(1);
			String t = getType(type);
			result += t;
			result += " else ";
			String defaultValue = arguments.get(i).dynamicGet(2).toString();
			result += t.equals("String") ? "\"" + defaultValue + "\"" : defaultValue;
			result += "\n";
		}
		return result;
	}

	public String generateOptionConf(Element e, String stereotypeName, Boolean isConf) {
		String taggedValue = isConf ? "conf" : "options";
		String func = isConf ? "set" : "option";
		String result = "";
		List<Option> opts = getOptionsFromElement(e, stereotypeName, taggedValue);

		for (Option conf : opts) {
			result += "\n						";
			result += "." + func + "(\"";
			result += conf.getKey();
			result += "\", ";
			result += conf.getValue();
			result += ")";
		}
		return result;
	}

	public String formatClassName(String var) {
		String res = var.replaceAll("\\s", "");
		return res.substring(0, 1).toUpperCase() + res.substring(1);
	}

	public String formatString(Object in) {
		if (Objects.isNull(in)) {
			return "";
		}
		if (in instanceof Collection) {
			Collection<?> c = (Collection<?>) in;
			List<String> s = new ArrayList<>();
			for (Object o : c) {
				s.add(formatString(o));
			}
			return "[" + String.join(", ", s) + "]";

		}
		String var = in.toString();
		String res = var.replaceAll("\"", "\\\\\"");
		return res;
	}

	public String replaceVariableInTaggedValue(String taggedValue, String toReplace, String replaceBy) {
		return taggedValue.replaceAll(toReplace, replaceBy);
	}

	public String generateFunctionDefinitionArguments(Class node, Model model, Boolean isFirst) {
		String result = "";
		List<String> args = new ArrayList<>();
		List<Relationship> associations = node.getRelationships();
		for (Relationship a : associations) {
			String argument = "";
			if (!isStereotype(a, "Argument")) {
				continue;
			}
			List<Element> ends = a.getRelatedElements();
			for (Element p : ends) {
				Class end = (Class) p;
				if (end != node) {
					argument += end.getName();
					argument += ": ";
					String type = generateType(getArgumentDataType(end), model);
					argument += isVariableRdd(end) ? "RDD[" + type + "]" : type;
				}
			}
			args.add(argument);
		}
		if (args.size() > 0 && !isFirst) {
			result += ", ";
		}
		return result + String.join(", ", args);
	}
	
	
	public boolean containsDataTypes(Model model) {
		for (Element e : model.getOwnedElements()) {
			if ((e instanceof org.eclipse.uml2.uml.DataType)) {
				if (!isStereotype(e, S_TUPLE)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isInArguments(Class node, Model model) {
		String variableName = node.getName();
		List<DynamicEObjectImpl> arguments = null;
		List<String> argumentsNames = new ArrayList<>();
		List<Stereotype> stereotypes = model.getAppliedStereotypes();
		for (Stereotype stereotype : stereotypes) {
			if (stereotype.getName().equals(S_SPARKAPP)) {
				arguments = (List<DynamicEObjectImpl>) model.getValue(stereotype, "arguments");
			}
		}
		for (DynamicEObjectImpl arg : arguments) {
			argumentsNames.add((String) arg.dynamicGet(0));
		}
		return argumentsNames.contains(variableName);
	}
	
	public String generateVariableType(Class node, Model model) {
		DataType dt = getArgumentDataType(node);
		String dtString = generateType(dt, model);
		return isVariableRdd(node) ? "RDD[" + dtString + "]" : dtString;
	}

	private DataType getArgumentDataType(Class end) {
		List<Stereotype> stereotypes = end.getAppliedStereotypes();
		for (Stereotype stereotype : stereotypes) {
			if (stereotype.getName().equals(S_VARIABLE)) {
				DataType dt = (DataType) end.getValue(stereotype, "dataType");
				return dt;
			}
		}
		return null;
	}

	private boolean isVariableRdd(Class end) {
		List<Stereotype> stereotypes = end.getAppliedStereotypes();
		for (Stereotype stereotype : stereotypes) {
			if (stereotype.getName().equals("Variable")) {
				Boolean isrdd = (Boolean) end.getValue(stereotype, "isRDD");
				return isrdd;
			}
		}
		return false;
	}

	
	
	@SuppressWarnings("unchecked")
	private List<Option> getOptionsFromElement(Element e, String stereotypeName, String taggedValue) {
		List<Option> opts = new ArrayList<>();
		List<Stereotype> flowStereotypes = e.getAppliedStereotypes();
		List<DynamicEObjectImpl> options = null;
		for (Stereotype stereotype : flowStereotypes) {
			if (stereotype.getName().equals(stereotypeName)) {
				options = (List<DynamicEObjectImpl>) e.getValue(stereotype, taggedValue);
			}
		}

		for (DynamicEObjectImpl opt : options) {
			String key = (String) opt.dynamicGet(0);
			String value = (String) opt.dynamicGet(1);
			opts.add(new Option(key, value));
		}
		return opts;
	}
}