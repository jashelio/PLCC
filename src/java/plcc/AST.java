package plcc;

import java.util.List;
import java.util.ArrayList;

import plcc.Token;

public class AST {

	private List<AST> nodes = null;;
	private Token value = null; // TODO maybe change to Value interface

	public AST(List<AST> nodes) {
		this.nodes = List.copyOf(nodes);
	}

	public AST(Token token) {
		value = token;
	}
}
