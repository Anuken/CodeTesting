package io.anuke.codetesting.logic;

public enum Fallacy{
	none {
		@Override
		public boolean occurs(Statement p1, Statement p2, Statement c){
			return false;
		}
	},
	undistributedMiddle{
		@Override
		public boolean occurs(Statement p1, Statement p2, Statement c){
			return !p1.isTermDistributed(Term.M) && !p2.isTermDistributed(Term.M);
		}
	},
	illictMinor {
		@Override
		public boolean occurs(Statement p1, Statement p2, Statement c){
			return c.isTermDistributed(Term.S) && !p1.isTermDistributed(Term.S) && !p2.isTermDistributed(Term.S);
		}
	},
	illicitMajor {
		@Override
		public boolean occurs(Statement p1, Statement p2, Statement c){
			return c.isTermDistributed(Term.P) && !p1.isTermDistributed(Term.P) && !p2.isTermDistributed(Term.P);
		}
	},
	exclusivePremises {
		@Override
		public boolean occurs(Statement p1, Statement p2, Statement c){
			return p1.proposition.negative && p2.proposition.negative;
		}
	},
	affirimitiveConclusionNegativePremise {
		@Override
		public boolean occurs(Statement p1, Statement p2, Statement c){
			return !c.proposition.negative && (p1.proposition.negative || p2.proposition.negative);
		}
	},
	negativeConclusionAffirimitePremise {
		@Override
		public boolean occurs(Statement p1, Statement p2, Statement c){
			return c.proposition.negative && (!p1.proposition.negative && !p2.proposition.negative);
		}
	},
	existential {
		@Override
		public boolean occurs(Statement p1, Statement p2, Statement c){
			return p1.proposition.universal && p2.proposition.universal && !c.proposition.universal;
		}
	};
	
	abstract public boolean occurs(Statement p1, Statement p2, Statement c);
}
