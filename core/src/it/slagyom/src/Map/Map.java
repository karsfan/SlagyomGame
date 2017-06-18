package it.slagyom.src.Map;

import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import it.slagyom.src.World.Tile;

public class Map {
	private LinkedList<Tile> listTile;
	private LinkedList<Item> listItems;
	private String nameVillage;
	public String mapPath;
	public boolean current;

	public Map(String path, boolean bool, String nameVillage) {
		this.nameVillage = nameVillage;
		current = bool;
		listTile = new LinkedList<Tile>();
		listItems = new LinkedList<Item>();
		readMap(path);
		setMapPath(path);
	}

	public String getNameVillage() {
		return nameVillage;
	}

	public void setNameVillage(String nameVillage) {
		this.nameVillage = nameVillage;
	}

	public Map() {
		listTile = new LinkedList<Tile>();
		listItems = new LinkedList<Item>();
	}

	public LinkedList<Item> getListItems() {
		return listItems;
	}

	public void readMap(String path) {
		openFile(path);
	}

	@SuppressWarnings("resource")
	public void openFile(String fileName) {
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
			try {
				Scanner input = new Scanner(System.in);
				input = new Scanner(fr);
				while (input.hasNextLine()) {
					String line = input.nextLine();
					String[] splittata = line.split(" ");

					for (int i = 0; i < splittata.length - 1; i++)
						addTile(splittata[i], getXY(splittata[i + 1]));
				}
				input.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			fr.close();
		} catch (

		IOException e) {
			e.printStackTrace();
		}
		System.out.println("read");
	}

	public static Point getXY(String line) {
		int x = 0;
		int i = 1;
		for (; line.charAt(i) != 'Y'; i++) {
			x = x * 10 + java.lang.Character.getNumericValue(line.charAt(i));
		}

		int y = 0;
		for (i = i + 1; line.charAt(i) != ';'; i++) {
			y = y * 10 + java.lang.Character.getNumericValue(line.charAt(i));
		}
		y = Math.abs(y - 29);
		return new Point(x, y);
	}

	public void addTile(String element, Point point) {
		Tile tile = new Tile(element, point);
		tile.setPoint(
				new Point((int) tile.shape.getX(), (int) Math.abs(tile.shape.getY()-tile.shape.height/32 +1)));
		listTile.add(tile);
	}

	public LinkedList<Tile> getListTile() {
		return listTile;
	}

	public String getMapPath() {
		return mapPath;
	}

	public void setMapPath(String mapPath) {
		this.mapPath = mapPath;
	}

	public boolean current() {
		return current;
	}

	public void set(String path, boolean bool) {
		current = bool;
		listTile = new LinkedList<Tile>();
		listItems = new LinkedList<Item>();
		readMap(path);
		setMapPath(path);
	}

	public void setCurrent(boolean bool) {
		current = bool;
	}
}
