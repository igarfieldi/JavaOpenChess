package jchess.gamelogic.models;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import jchess.gamelogic.Clock;

public class GameClockModel
{
	private List<Clock> clocks;
	
	public GameClockModel() {
		this(0);
	}
	
	public GameClockModel(int clockCount) {
		clockCount = Math.max(clockCount, 0);
		
		this.clocks = new ArrayList<Clock>(clockCount);
		for(int i = 0; i < clockCount; i++) {
			this.clocks.add(new Clock());
		}
	}
	
	/**
	 * Returns the clock with the given index.
	 * @param index Index of the clock
	 * @return Clock
	 */
	public Clock getClock(int index) {
		return clocks.get(index);
	}
	
	/**
	 * Returns all clocks of the model.
	 * @return List of clocks of the model
	 */
	public List<Clock> getClocks() {
		return clocks;
	}
	
	/**
	 * Returns the clock next in the list.
	 * Wraps around if the list is at its end.
	 * @param current Clock currently used
	 * @return Clock next in list
	 */
	public Clock getNextClock(Clock current) {
		int index = clocks.indexOf(current) + 1;
		if(index == 0) {
			throw new NoSuchElementException("The current clock does not exist in the model!");
		}
		
		if(index == clocks.size()) {
			index = 0;
		}
		return clocks.get(index);
	}
}
