package io.anuke.codetesting.storysim;

public interface State<T extends Actor>{
	public default void entered(T a){}
	public default void exited(T a){}
	public default void enteredPlace(T a, Place place){}
	public default void exitedPlace(T a, Place place){}
	public default void update(T a){}
	public int ordinal();
}
