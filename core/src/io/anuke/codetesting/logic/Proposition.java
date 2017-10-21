package io.anuke.codetesting.logic;

public enum Proposition{
	A(true, false, "all", "are", true, false), 
	E(true, true, "no", "are", true, true), 
	I(false, false, "some", "are", false, false), 
	O(false, true, "some", "are not", false, true);
	
	public final boolean subjectDistributed;
	public final boolean predicateDistributed;
	
	public final String copula;
	public final String quantifier;
	public final boolean universal;
	public final boolean negative;
	
	private Proposition(boolean universal, boolean negative, String quantifier, String copula, boolean subjectDistributed, boolean predicateDistributed){
		this.universal = universal;
		this.negative = negative;
		this.quantifier = quantifier;
		this.copula = copula;
		this.subjectDistributed = subjectDistributed;
		this.predicateDistributed = predicateDistributed;
	}
	
	public boolean distributed(Term term){
		if(term == Term.M) return false;
		
		return term == Term.P ? predicateDistributed : subjectDistributed;
	}
}
