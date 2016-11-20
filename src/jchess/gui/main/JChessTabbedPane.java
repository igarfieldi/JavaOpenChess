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
#    along with this program.  If not, see <http://www.gnu.org/licenses/>..
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.gui.main;

import javax.swing.*;

import jchess.JChessApp;
import jchess.gui.ThemeLoader;
import jchess.gui.setup.NewGameWindow;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JChessTabbedPane extends JTabbedPane implements MouseListener, ImageObserver
{
	private static final long serialVersionUID = -7046648284513652282L;
	private static Logger log = Logger.getLogger(JChessTabbedPane.class.getName());
	
	private TabbedPaneIcon closeIcon;
	private Image addIcon;
	private Image unclickedAddIcon;
	private Rectangle addIconRectangle = null;
	
	public JChessTabbedPane()
	{
		super();
		this.closeIcon = new TabbedPaneIcon(this.closeIcon);
		this.unclickedAddIcon = ThemeLoader.loadThemeImage("add-tab-icon.png");
		this.addIcon = this.unclickedAddIcon;
		
		this.setDoubleBuffered(true);
		super.addMouseListener(this);
	}
	
	public void mouseReleased(MouseEvent event)
	{
	}
	
	public void mousePressed(MouseEvent event)
	{
	}
	
	@Override
	public void mouseClicked(MouseEvent event)
	{
		int tabNumber = getUI().tabForCoordinate(this, event.getX(), event.getY());
		if(tabNumber >= 0)
		{
			closeGameTab(event, tabNumber);
			if(this.getTabCount() == 0)
				this.showNewGameWindow();
		}
		else if(this.addIconRectangle != null && this.addIconRectangle.contains(event.getX(), event.getY()))
		{
			log.log(Level.FINE, "newGame by + button");
			this.showNewGameWindow();
		}
	}

	private void closeGameTab(MouseEvent event, int tabNumber)
	{
		Rectangle closeIconRectangle = ((TabbedPaneIcon) getIconAt(tabNumber)).getBounds();
		if(closeIconRectangle.contains(event.getX(), event.getY()))
		{
			log.log(Level.FINE, "Removing tab with " + tabNumber + " number!...");
			this.removeTabAt(tabNumber);
			this.updateAddIconRectangle();
		}
	}

	private void updateAddIconRectangle()
	{
		if(this.getTabCount() > 0)
		{
			Rectangle newRectangleBounds = this.getBoundsAt(this.getTabCount() - 1);
			this.addIconRectangle = new Rectangle(newRectangleBounds.x + newRectangleBounds.width + 5,
			        newRectangleBounds.y, this.addIcon.getWidth(this), this.addIcon.getHeight(this));
		}
		else
			this.addIconRectangle = null;
	}

	private void showNewGameWindow()
	{
		if(JChessApp.jcv.newGameFrame == null)
			JChessApp.jcv.newGameFrame = new NewGameWindow();
		
		JChessApp.getApplication().show(JChessApp.jcv.newGameFrame);
	}
	
	public void mouseEntered(MouseEvent event)
	{
	}
	
	public void mouseExited(MouseEvent event)
	{
	}
	
	@Override
	public void addTab(String title, Component component)
	{
		super.addTab(title, new TabbedPaneIcon(null), component);
		log.info("Present number of tabs: " + this.getTabCount());
		this.updateAddIconRectangle();
	}

	@Override
	public boolean imageUpdate(Image image, int infoflags, int x, int y, int width, int height)
	{
		super.imageUpdate(image, infoflags, x, y, width, height);
		this.updateAddIconRectangle();
		return true;
	}
	
	@Override
	public void paint(Graphics graphics)
	{
		super.paint(graphics);
		if(addIconRectangle != null)
			graphics.drawImage(this.addIcon, addIconRectangle.x, addIconRectangle.y, null);
	}
	
	@Override
	public void update(Graphics graphics)
	{
		this.repaint();
	}
	
	private class TabbedPaneIcon implements Icon
	{
		private int xPosition;
		private int yPosition;
		private int width;
		private int height;
		private Icon fileIcon;
		
		public TabbedPaneIcon(Icon fileIcon)
		{
			this.fileIcon = fileIcon;
			this.width = 16;
			this.height = 16;
		}
		
		@Override
		public void paintIcon(Component component, Graphics graphics, int x, int y)
		{
			this.xPosition = x;
			this.yPosition = y;
			
			Color iconColor = graphics.getColor();
			graphics.setColor(Color.black);
			
			int yOffset = y + 2;
			drawIcon(graphics, x, yOffset);
			
			graphics.setColor(iconColor);
			if(fileIcon != null)
				fileIcon.paintIcon(component, graphics, x + width, yOffset);
		}

		private void drawIcon(Graphics graphics, int x, int line_y)
		{
			graphics.drawLine(x + 3, line_y + 3, x + 10, line_y + 10);
			graphics.drawLine(x + 3, line_y + 4, x + 9, line_y + 10);
			graphics.drawLine(x + 4, line_y + 3, x + 10, line_y + 9);
			graphics.drawLine(x + 10, line_y + 3, x + 3, line_y + 10);
			graphics.drawLine(x + 10, line_y + 4, x + 4, line_y + 10);
			graphics.drawLine(x + 9, line_y + 3, x + 3, line_y + 9);
		}
		
		public int getIconWidth()
		{
			return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);
		}
		
		public int getIconHeight()
		{
			return height;
		}
		
		public Rectangle getBounds()
		{
			return new Rectangle(xPosition, yPosition, width, height);
		}
	}
}