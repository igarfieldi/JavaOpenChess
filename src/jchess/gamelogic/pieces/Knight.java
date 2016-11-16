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
package jchess.gamelogic.pieces;

import java.util.ArrayList;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Chessboard;
import jchess.gamelogic.field.Square;
import jchess.gui.GUI;

import java.awt.Image;

/**
 * Class to represent a chess pawn knight
 * Knight's movements:
 *  

|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|2|_|3|_|_|_|5
|_|1|_|_|_|4|_|_|4
|_|_|_|K|_|_|_|_|3
|_|8|_|_|_|5|_|_|2
|_|_|7|_|6|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 */
public class Knight extends Piece
{

    public static short value = 3;
    protected static final Image imageWhite = GUI.loadThemeImage("Knight-W.png");
    protected static final Image imageBlack = GUI.loadThemeImage("Knight-B.png");

    public Knight(Chessboard chessboard, Player player)
    {
        super(chessboard, player);//call initialiser of super type: Piece
        //this.setImages("Knight-W.png", "Knight-B.png");
        this.symbol = "N";
        this.setImage();
    }

    @Override
    void setImage()
    {
        if (this.player.getColor() == Player.Color.BLACK)
        {
            image = imageBlack;
        }
        else
        {
            image = imageWhite;
        }
        orgImage = image;
    }

    /**
     *  Annotation to superclass Piece changing pawns location
     * @return  ArrayList with new position of pawn
     */
    @Override
    public ArrayList<Square> allMoves()
    {
        ArrayList<Square> list = new ArrayList<Square>();
        int newX, newY;
        //1st move from the grid 
        newX = this.square.getPosX() - 2;
        newY = this.square.getPosY() + 1;

        if (!isout(newX, newY) && checkPiece(newX, newY))
        {
            if (this.player.getColor() == Player.Color.WHITE) //white
            {
                if (this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
            else //or black
            {
                if (this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
        }

        //2nd move from the grid
        newX = this.square.getPosX() - 1;
        newY = this.square.getPosY() + 2;

        if (!isout(newX, newY) && checkPiece(newX, newY))
        {
            if (this.player.getColor() == Player.Color.WHITE) //white
            {
                if (this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
            else //or black
            {
                if (this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
        }

        //3rd move from the grid
        newX = this.square.getPosX() + 1;
        newY = this.square.getPosY() + 2;

        if (!isout(newX, newY) && checkPiece(newX, newY))
        {
            if (this.player.getColor() == Player.Color.WHITE) //white
            {
                if (this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
            else //or black
            {
                if (this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
        }

        //4th move from the grid
        newX = this.square.getPosX() + 2;
        newY = this.square.getPosY() + 1;

        if (!isout(newX, newY) && checkPiece(newX, newY))
        {
            if (this.player.getColor() == Player.Color.WHITE) //white
            {
                if (this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
            else //or black
            {
                if (this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
        }

        //5th move from the grid
        newX = this.square.getPosX() + 2;
        newY = this.square.getPosY() - 1;

        if (!isout(newX, newY) && checkPiece(newX, newY))
        {
            if (this.player.getColor() == Player.Color.WHITE) //white
            {
                if (this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
            else //or black
            {
                if (this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
        }

        //6th move from the grid
        newX = this.square.getPosX() + 1;
        newY = this.square.getPosY() - 2;

        if (!isout(newX, newY) && checkPiece(newX, newY))
        {
            if (this.player.getColor() == Player.Color.WHITE) //white
            {
                if (this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
            else //or black
            {
                if (this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
        }

        //7th move from the grid
        newX = this.square.getPosX() - 1;
        newY = this.square.getPosY() - 2;

        if (!isout(newX, newY) && checkPiece(newX, newY))
        {
            if (this.player.getColor() == Player.Color.WHITE) //white
            {
                if (this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
            else //or black
            {
                if (this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
        }

        //8th move from the grid
        newX = this.square.getPosX() - 2;
        newY = this.square.getPosY() - 1;

        if (!isout(newX, newY) && checkPiece(newX, newY))
        {
            if (this.player.getColor() == Player.Color.WHITE) //white
            {
                if (this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
            else //or black
            {
                if (this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square, chessboard.squares[newX][newY]))
                {
                    list.add(chessboard.squares[newX][newY]);
                }
            }
        }

        return list;
    }
}
