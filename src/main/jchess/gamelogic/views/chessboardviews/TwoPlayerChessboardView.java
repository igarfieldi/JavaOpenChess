/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.gamelogic.views.chessboardviews;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;

import jchess.gui.ThemeImageLoader;

/**
 * Class to represent chessboard. Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class TwoPlayerChessboardView extends SquareChessboardView
{
	private static final long serialVersionUID = 3713107400653656480L;
	
	private static final String[] FIELD_LETTERS = {
			"a", "b", "c", "d", "e", "f", "g", "h",
		};
	private static final String[] FIELD_NUMBERS = {
			"1", "2", "3", "4", "5", "6", "7", "8"
		};
	
	private final int CHESSBOARD_SQUARES = 8;
	private final ThemeImageLoader themeLoader;

	public TwoPlayerChessboardView(boolean renderLabels, boolean invertedBoard)
	{
		super(renderLabels, invertedBoard);
		themeLoader = ThemeImageLoader.getInstance();
	}
	
	@Override
	protected Image getChessboardImage() {
		return themeLoader.loadThemeImage("chessboard.png");
	}

	@Override
	protected Image getSelectedFieldImage()
	{
		return themeLoader.loadThemeImage("sel_square.png");
	}

	@Override
	protected Image getPossibleFieldImage()
	{
		return themeLoader.loadThemeImage("able_square.png");
	}
	
	@Override
	protected int getSquareCount() {
		return CHESSBOARD_SQUARES;
	}

	@Override
	protected List<String> getLabelLetters() {
		return Arrays.asList(FIELD_LETTERS);
	}

	@Override
	protected List<String> getLabelNumbers() {
		return Arrays.asList(FIELD_NUMBERS);
	}
}
