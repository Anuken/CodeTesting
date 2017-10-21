package io.anuke.codetesting.logic;

public enum Figure{
	f1(new Term[][]{
		{Term.M, Term.P},
		{Term.S, Term.M},
		{Term.S, Term.P}
	}), 
	f2(new Term[][]{
		{Term.P, Term.M},
		{Term.S, Term.M},
		{Term.S, Term.P}
	}), 
	f3(new Term[][]{
		{Term.M, Term.P},
		{Term.M, Term.S},
		{Term.S, Term.P}
	}), 
	f4(new Term[][]{
		{Term.P, Term.M},
		{Term.M, Term.S},
		{Term.S, Term.P}
	});
	
	public final Term[][] terms;
	
	private Figure(Term[][] terms){
		this.terms = terms;
	}
}
