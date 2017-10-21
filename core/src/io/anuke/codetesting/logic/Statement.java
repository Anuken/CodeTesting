package io.anuke.codetesting.logic;

public class Statement{
	Term term1;
	Term term2;
	Proposition proposition;
	
	public Statement(Proposition proposition, Term term1, Term term2){
		this.proposition = proposition;
		this.term1 = term1;
		this.term2 = term2;
	}
	
	public boolean isTermDistributed(Term term){
		return (term == term1 && proposition.subjectDistributed) ||
				(term == term2 && proposition.predicateDistributed);
	}
	
	@Override
	public String toString(){
		return toString(term1.toString(), term2.toString());
	}
	
	public String toString(String first, String second){
		return proposition.quantifier + " " + first + " " + proposition.copula + " " + second;
	}
}
