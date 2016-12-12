package jchess.gamelogic.models;

import java.util.ArrayList;
import java.util.List;

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
	
	public Clock getClock(int index) {
		return clocks.get(index);
	}
	
	public List<Clock> getClocks() {
		return clocks;
	}
}
