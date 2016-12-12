package jchess.gamelogic.views;

public interface IMessageDisplay
{
	public enum Option {
		YES,
		NO,
		CANCEL
	}

	public void showMessage(String key, String arg);
	public Option showConfirmMessage(String key, String arg);
	public String showInputMessage(String key, String arg, String initialValue);
}