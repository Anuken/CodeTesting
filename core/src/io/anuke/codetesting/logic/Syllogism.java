package io.anuke.codetesting.logic;

public class Syllogism{
	Proposition premise1Prop, premise2Prop, conclusionProp;
	Figure figure;
	
	Statement premise1, premise2, conclusion;
	
	public Syllogism(Proposition premise1Prop, Proposition premise2Prop, Proposition conclusionProp, Figure figure){
		this.premise1Prop = premise1Prop;
		this.premise2Prop = premise2Prop;
		this.conclusionProp = conclusionProp;
		this.figure = figure;
		
		premise1 = new Statement(premise1Prop, figure.terms[0][0], figure.terms[0][1]);
		premise2 = new Statement(premise2Prop, figure.terms[1][0], figure.terms[1][1]);
		conclusion = new Statement(conclusionProp, figure.terms[2][0], figure.terms[2][1]);
	}
	
	public Fallacy getFallacy(){
		for(int i = 0; i < Fallacy.values().length; i ++){
			Fallacy fallacy = Fallacy.values()[i];
			if(fallacy.occurs(premise1, premise2, conclusion)){
				return fallacy;
			}
		}
		
		return Fallacy.none;
	}
	
	@Override
	public String toString(){
		return premise1.toString() + "\n" + premise2.toString() + "\ntherefore, " + conclusion.toString();
	}
	
	public static Syllogism fromString(String text){
		String[] split = text.split("-");
		Figure figure = Figure.valueOf("f" + split[1]);
		Proposition p1 = Proposition.valueOf(split[0].toString().charAt(0) + "");
		Proposition p2 = Proposition.valueOf(split[0].toString().charAt(1) + "");
		Proposition p3 = Proposition.valueOf(split[0].toString().charAt(2) + "");
		
		return new Syllogism(p1, p2, p3, figure);
	}
}
