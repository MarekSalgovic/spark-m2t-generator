package cz.vutbr.fit.dip.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.Stereotype;

public class GenerateComputation extends UML2ServicesCommon {

	public String generateVarDefsInLoop(Model model) {
		String result = "";
		List<Class> sources = getSources(model);
		for (Class s : sources) {
			result += generateOutputDefinitionInsideLoop(s, model, "");
		}
		return result;
	}

	public String generateComputation(Model model) {
		String result = "";
		List<Model> loops = getLoops(model);
		List<Model> activeLoops = new ArrayList<>();
		List<Class> sources = getSources(model);
		for (Class s : sources) {
			result += generateNode(s, "", model, loops, activeLoops, "");
		}
		return result;
	}

	private boolean isInsideLoop(Class node, Model model) {
		return !node.getModel().equals(model);
	}

	private String generateOutputDefinitionInsideLoop(Class node, Model model, String tag) {
		String result = "";
		List<OutputNode> outputs = getSortedOutputs(node, tag);
		result += generateOutputDefinitionInsideLoopString(node, tag, model);
		for (OutputNode o : outputs) {
			String newTag = o.getTag();
			if (!tag.equals("") && newTag.equals("")) {
				newTag = tag;
			}
			result += generateOutputDefinitionInsideLoop(o.getNode(), model, newTag);
		}
		return result;
	}

	private String generateOutputDefinitionInsideLoopString(Class node, String tag, Model model) {
		String myName = generateMyName(node, tag);
		String myType = getOutputType(node, !isAction(node), model, tag);
		if (myType.length() <= 0) {
			return "";
		}
		if (!isAction(node) && !isTransformation(node) && !isSource(node)) {
			return "";
		}
		if (!isInsideLoop(node, model)) {
			return "";
		}
		return "var " + myName + ": " + myType + " = null\n";
	}

	private String getOutputType(Class node, Boolean isRDD, Model model, String tag) {
		String result = "";
		List<DirectedRelationship> links = node.getSourceDirectedRelationships();
		for (DirectedRelationship link : links) {
			if (!(link instanceof InformationFlow)) {
				continue;
			}
			InformationFlow info = (InformationFlow) link;
			String infoTag = getTagForRelationship(info);
			if (info.getConveyeds().size() > 0 && infoTag.equals(tag)) {
				result += generateType(info.getConveyeds().get(0), model);
				break;
			}
		}
		if (result.length() == 0) {
			return "";
		}
		return isRDD ? "RDD[" + result + "]" : result;
	}

	
	
	private String generateNode(Class node, String inputNodeName, Model model, List<Model> loops,
			List<Model> activeLoops, String tag) {
		String result = "";
		result += generateStartLoop(node.getModel(), model, loops, activeLoops);
		result += generateEndLoop(node.getModel(), model, loops, activeLoops);
		if (isStereotype(node, S_CODEBLOCK)) {
			return result + generateCodeBlock(node, inputNodeName);
		}
		if (isStereotype(node, S_VARIABLE)) {
			return result + generateVariableDefinition(node, inputNodeName);
		}
		List<OutputNode> outputs = getSortedOutputs(node, tag);
		String generate = generateNodeString(node, inputNodeName, tag, model);
		String myName = generateMyName(node, tag);
		result += generate;
		for (OutputNode o : outputs) {
			String newTag = o.getTag();
			if (!tag.equals("") && newTag.equals("")) {
				newTag = tag;
			}
			result += generateNode(o.getNode(), myName, model, loops, activeLoops, newTag);
		}
		return result;
	}

	private List<Model> getLoops(Model model) {
		List<Model> loops = new ArrayList<>();
		for (Element e : model.getOwnedElements()) {
			if (e.getAppliedStereotypes().stream()
					.anyMatch(x -> (x.getName().equals(S_CONDITIONALLOOP) || (x.getName().equals(S_ITERATIONLOOP))))) {
				if (e instanceof org.eclipse.uml2.uml.Model) {
					loops.add((org.eclipse.uml2.uml.Model) e);
					loops.addAll(getLoops((org.eclipse.uml2.uml.Model) e));
				}
			}
		}
		return loops;
	}

	private String generateStartLoop(Model current, Model model, List<Model> loops, List<Model> activeLoops) {
		String result = "";
		if (current == model) {
			return "";
		}
		if (isLoop(current) && !activeLoops.contains(current)) {
			activeLoops.add(current);
			result += generateLoopHeader(current);
			result += " {\n";
			result = generateStartLoop((Model) current.getOwner(), model, loops, activeLoops) + result;
		}
		return result;
	}

	private String generateLoopHeader(Model loop) {
		List<Stereotype> stereotypes = loop.getAppliedStereotypes();
		for (Stereotype stereotype : stereotypes) {
			if (stereotype.getName().equals(S_CONDITIONALLOOP)) {
				Object cond = loop.getValue(stereotype, "condition");
				String condition = Objects.isNull(cond) ? "" : cond.toString();
				return "while(" + condition + ")";
			}
			if (stereotype.getName().equals(S_ITERATIONLOOP)) {
				Object iter = loop.getValue(stereotype, "iterations");
				String iterations = Objects.isNull(iter) ? "" : iter.toString();
				return "for(" + formatVariable(loop.getName()) + " <- 1 to " + iterations + ".toInt)";
			}
		}
		return "";
	}

	private String generateEndLoop(Model current, Model model, List<Model> loops, List<Model> activeLoops) {
		String result = "";
		List<Model> myLoops = getMyLoops(current, model);
		int diff = activeLoops.size() - myLoops.size();
		for (int i = 0; i < diff; i++) {
			result += "}\n";
		}
		activeLoops.clear();
		activeLoops.addAll(myLoops);
		return result;
	}

	private List<Model> getMyLoops(Model current, Model model) {
		List<Model> myLoops = new ArrayList<>();
		if (isLoop(current)) {
			myLoops.add(current);
			myLoops.addAll(getMyLoops((Model) current.getOwner(), model));
		}
		return myLoops;
	}

	private String generateVariableDefinition(Class node, String inputNodeName) {
		String block = "";
		block += formatVariable(node.getName());
		block += " = ";
		block += inputNodeName;
		return block + "\n";
	}

	private String generateCodeBlock(Class node, String inputNodeName) {
		String block = "";
		List<Stereotype> stereotypes = node.getAppliedStereotypes();
		for (Stereotype stereotype : stereotypes) {
			if (stereotype.getName().equals(S_CODEBLOCK)) {
				block = (String) node.getValue(stereotype, "code");
			}
		}
		block = block.replaceAll("\\$out", inputNodeName);
		return block + "\n";
	}

	private String generateMyName(Class node, String tag) {
		String result = formatVariable(node.getName());
		result += tag.equals("") ? "" : "_" + tag;
		if (isSource(node)) {
			return "s_" + result;
		} else if (isTransformation(node)) {
			return "t_" + result;
		} else if (isAction(node)) {
			return "a_" + result;
		}
		return "";
	}

	private String generateNodeString(Class node, String inputNodeName, String tag, Model model) {
		boolean isSource = false;
		boolean isTransformation = false;
		boolean isAction = false;
		String varPrefix = "";
		String classPrefix = "";
		if (isSource(node)) {
			varPrefix = "s_";
			classPrefix = "S_";
			isSource = true;
		} else if (isTransformation(node)) {
			varPrefix = "t_";
			classPrefix = "T_";
			isTransformation = true;
		} else if (isAction(node)) {
			varPrefix = "a_";
			classPrefix = "A_";
			isAction = true;
		}
		String newName = varPrefix + formatVariable(node.getName());
		newName += tag.equals("") ? "" : "_" + tag;
		String result = isInsideLoop(node, model) ? "" : "val ";
		result += newName + " = " + classPrefix + formatVariable(node.getName());
		String args = generateFunctionCallArguments(node);
		result += isSource ? ".source(" + args + ")" : "";
		if (args.length() > 0) {
			args = ", " + args;
		}
		result += isTransformation ? ".transform(" + inputNodeName + args + ")" : "";
		result += isAction ? ".action(" + inputNodeName + args + ")" : "";
		String outputType =  getOutputType(node, true, model, tag);
		result += outputType.length() != 0 ? !isAction ? ".asInstanceOf[" + outputType + "]" : "" : "";
		return result + ";\n";
	}

	private List<Class> getSources(Model model) {
		List<SourcePriority> sources = new ArrayList<>();
		for (Element e : model.getOwnedElements()) {
			if (!(e instanceof org.eclipse.uml2.uml.Class)) {
				continue;
			}
			Class source = (Class) e;
			List<Stereotype> stereotypes = source.getAppliedStereotypes();
			for (Stereotype stereotype : stereotypes) {
				if (isSource(source)) {
					Integer priority = Integer.parseInt(e.getValue(stereotype, "priority").toString());
					SourcePriority sp = new SourcePriority(source, priority);
					sources.add(sp);
				}
			}
		}
		return sortSources(sources);
	}

	private List<Class> sortSources(List<SourcePriority> sp) {
		Collections.sort(sp, (o1, o2) -> o2.getPriority().compareTo(o1.getPriority()));
		return sp.stream().map(x -> x.getSource()).toList();
	}

	private int getPriorityForRelationship(DirectedRelationship rs) {
		if (isStereotype(rs, "Flow")) {
			List<Stereotype> stereotypes = rs.getAppliedStereotypes();
			for (Stereotype stereotype : stereotypes) {
				if (stereotype.getName().equals(S_FLOW)) {
					Object priority = rs.getValue(stereotype, "priority");
					return !Objects.isNull(priority) ? Integer.parseInt(priority.toString()) : Integer.MAX_VALUE;
				}
			}
		}
		return Integer.MIN_VALUE;
	}

	private String getTagForRelationship(DirectedRelationship rs) {
		if (isStereotype(rs, "Flow")) {
			List<Stereotype> stereotypes = rs.getAppliedStereotypes();
			for (Stereotype stereotype : stereotypes) {
				if (stereotype.getName().equals(S_FLOW)) {
					Object tag = rs.getValue(stereotype, "tag");
					return !Objects.isNull(tag) ? tag.toString() : "";
				}
			}
		}
		return "";
	}

	private List<OutputNode> sortOutputs(List<OutputNode> priorities) {
		Collections.sort(priorities, (o1, o2) -> o2.getPriority().compareTo(o1.getPriority()));
		return priorities;
	}

	private List<OutputNode> getSortedOutputs(Class input, String inputTag) {
		List<OutputNode> priorities = new ArrayList<>();
		List<DirectedRelationship> links = input.getSourceDirectedRelationships();
		for (DirectedRelationship link : links) {
			int priority = getPriorityForRelationship(link);
			String tag = getTagForRelationship(link);
			List<Element> targets = link.getTargets();
			for (Element t : targets) {
				if (t instanceof org.eclipse.uml2.uml.Class) {
					if (tag.equals(inputTag) || inputTag.equals("") || tag.equals("")) {
						priorities.add(new OutputNode((Class) t, priority, tag));
					}
				}
			}
		}
		return sortOutputs(priorities);
	}

	private String generateFunctionCallArguments(Class node) {
		List<String> args = new ArrayList<>();
		List<Relationship> associations = node.getRelationships();
		for (Relationship a : associations) {
			String argument = "";
			if (!isStereotype(a, S_ARGUMENT)) {
				continue;
			}
			List<Element> ends = a.getRelatedElements();
			for (Element p : ends) {
				Class end = (Class) p;
				if (end != node) {
					argument += end.getName();
				}
			}
			args.add(argument);
		}
		return String.join(", ", args);
	}

	private boolean isLoop(Model loop) {
		return loop.getAppliedStereotypes().stream()
				.anyMatch(x -> (x.getName().equals(S_CONDITIONALLOOP) || x.getName().equals(S_ITERATIONLOOP)));
	}

}
