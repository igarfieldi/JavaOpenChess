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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import jchess.JChessApp;
import jchess.gui.secondary.setup.NewGameWindow;
import jchess.gui.secondary.themechooser.ThemeImageLoader;

/**
 * Class handling the opening and closing of game tabs
 */
public class JChessTabbedPane extends JTabbedPane implements MouseListener, ImageObserver
{
	private static final long serialVersionUID = -7046648284513652282L;
	private static Logger log = Logger.getLogger(JChessTabbedPane.class.getName());
	
	private Image addIcon;
	private Rectangle addIconRectangle = null;
	
	private static final int EMPTY = 0;
	
	public JChessTabbedPane()
	{
		super();
		this.addIcon = ThemeImageLoader.getInstance().loadThemeImage("add-tab-icon.png");
		
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
		final int MINIMUM_INDEX = 0;
		
		int tabIndex = getUI().tabForCoordinate(this, event.getX(), event.getY());
		if(tabIndex >= MINIMUM_INDEX)
		{
			closeGameTab(event, tabIndex);
			if(this.getTabCount() == EMPTY)
				this.showNewGameWindow();
		}
		else if(this.addIconRectangle != null && this.addIconRectangle.contains(event.getX(), event.getY()))
		{
			log.log(Level.FINE, "newGame by + button");
			this.showNewGameWindow();
		}
	}

	/**
	 * Closes a tab when its close icon is klicked.
	 * 
	 * @param event
	 * 				The event of the mouse being clicked.
	 * @param tabIndex
	 * 				The index of the tab that is closed
	 */
	private void closeGameTab(MouseEvent event, int tabIndex)
	{
		Rectangle closeIconRectangle = ((CloseIcon) getIconAt(tabIndex)).getBounds();
		if(closeIconRectangle.contains(event.getX(), event.getY()))
		{
			log.log(Level.FINE, "Removing tab with " + tabIndex + " number!...");
			JChessApp.getApplication().view.removeGamefromListAt(tabIndex);
			this.removeTabAt(tabIndex);
			this.updateAddIconRectangle();
		}
	}

	/**
	 * Updates the position of the add icon whenever the number of tabs changes.
	 */
	private void updateAddIconRectangle()
	{
		if(this.getTabCount() > EMPTY)
		{
			final int RIGHT_TAB = this.getTabCount() - 1;
			final int RECTANGLE_WIDTH = 5;
			
			Rectangle newRectangleBounds = this.getBoundsAt(RIGHT_TAB);
			this.addIconRectangle = new Rectangle(newRectangleBounds.x + newRectangleBounds.width + RECTANGLE_WIDTH,
			        newRectangleBounds.y, this.addIcon.getWidth(this), this.addIcon.getHeight(this));
		}
		else
			this.addIconRectangle = null;
	}

	/**
	 * Displays the dialog to start a new game
	 */
	private void showNewGameWindow()
	{
		JChessApp.getApplication().show(new NewGameWindow(JChessApp.view.getFrame()));
	}
	
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}
	
	@Override
	public void addTab(String title, Component component)
	{
		super.addTab(title, new CloseIcon(null), component);
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
	
	/**
	 * This class handles the creation of a close icon taken from an image
	 */
	private class CloseIcon implements Icon
	{
		private int xPosition;
		private int yPosition;
		
		private Image iconImage = ThemeImageLoader.getInstance().loadThemeImage("close-tab-icon.png");
		private Icon fileIcon;
		
		public CloseIcon(Icon fileIcon)
		{
			this.fileIcon = new ImageIcon(iconImage);
		}
		
		@Override
		public void paintIcon(Component component, Graphics graphics, int x, int y)
		{
			this.xPosition = x;
			this.yPosition = y;
			
			if(fileIcon != null)
				fileIcon.paintIcon(component, graphics, x, y);
		}
		
		public int getIconWidth()
		{
			return fileIcon.getIconWidth();
		}
		
		public int getIconHeight()
		{
			return fileIcon.getIconHeight();
		}
		
		public Rectangle getBounds()
		{
			return new Rectangle(xPosition, yPosition, fileIcon.getIconWidth(), fileIcon.getIconHeight());
		}
	}
}