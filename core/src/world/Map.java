package world;

import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;


import battle.Enemy.Level;
import staticObjects.EnemyHome;
import staticObjects.BossHome;
import staticObjects.Item;
import staticObjects.PreEnemyHouse;
import staticObjects.Shop;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;

public class Map {
	private LinkedList<StaticObject> listStaticObjects;
	private LinkedList<Item> listItems;
	private String nameVillage;
	public String mapPath;
	public boolean current;
	public boolean online;
	public Map(String path, boolean villageCurrent, String nameVillage, boolean online) {
		this.nameVillage = nameVillage;
		current = villageCurrent;
		this.online = online;
		listStaticObjects = new LinkedList<StaticObject>();
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
		listStaticObjects = new LinkedList<StaticObject>();
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
				String line = input.nextLine();
				String[] split = line.split(" ");
				GameConfig.WIDTH = Integer.parseInt(split[0]);
				GameConfig.HEIGHT = Integer.parseInt(split[1]);
				while (input.hasNextLine()) {
					line = input.nextLine();
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
		y = (int) Math.abs(y - GameConfig.HEIGHT / 32);
		return new Point(x, y);
	}

	public void addTile(String element, Point point) {
		StaticObject staticObject;
		Point pointTable;
		StaticObject table;
		switch (element) {
		case "SHOP":
			staticObject = new Shop(point);
			pointTable = new Point(((int) (point.getX() + 64 / 32)), (int) Math.abs((point.getY() - 64 / 32)));
			table = new StaticObject("TABLE", point);
			table.setPoint(pointTable);
			table.setInfo("In questo shop potrai trovare tutto quello che ti serve per sconfiggere i tuoi nemici!!");
			listStaticObjects.add(table);
			break;
		case "PREENEMYHOME":
			if (current)
				staticObject = new PreEnemyHouse(Level.EASY, online);
			else
				staticObject = new PreEnemyHouse(Level.MEDIUM, online);
			pointTable = new Point(((int) (point.getX() + 96 / 32)), (int) Math.abs((point.getY() - 96 / 32)));
			table = new StaticObject("TABLE", point);
			table.setPoint(pointTable);
			table.setInfo(
					"In questa Pre-Enemy House ci sono nemici con cui ti potrai allenare");
			listStaticObjects.add(table);
			break;
		case "TEMPLE":
			if (current)
				staticObject = new EnemyHome(Level.EASY, Element.TEMPLE);
			else
				staticObject = new EnemyHome(Level.MEDIUM, Element.TEMPLE);
			pointTable = new Point(((int) (point.getX() + 128 / 32)), (int) Math.abs((point.getY() - 96 / 32)));
			table = new StaticObject("TABLE", point);
			table.setPoint(pointTable);
			table.setInfo(
					"In questo Tempio ci sono nemici che dovrai sconfiggere per poter affrontare il capo villaggio!");
			listStaticObjects.add(table);
			break;
			
		case "CASTLE":
			pointTable = new Point(((int) (point.getX() + 160 / 32)), (int) Math.abs((point.getY() - 160 / 32)));
			table = new StaticObject("TABLE", point);
			table.setPoint(pointTable);
			listStaticObjects.add(table);
			if (current == true) {
				table.setInfo("In questa castello c'e' il capo del villaggio");
				staticObject = new BossHome(Level.MEDIUM, Element.CASTLE);
			} else {
				table.setInfo("In questa castello c'e' il capo del villaggio");
				staticObject = new BossHome(Level.HARD, Element.CASTLE);
			}
			break;

		default:
			staticObject = new StaticObject(element, point);
			break;
		}
		staticObject
				.setPoint(new Point((int) point.x, (int) Math.abs(point.y - (staticObject.shape.getHeight() / 32))));
		// Tile tile = new Tile(element, point);
		// tile.setPoint(
		// new Point((int) tile.shape.getX(), (int) Math.abs(tile.shape.getY() -
		// (tile.shape.getHeight() / 32))));
		listStaticObjects.add(staticObject);
	}

	public LinkedList<StaticObject> getListTile() {
		return listStaticObjects;
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
		listStaticObjects = new LinkedList<StaticObject>();
		listItems = new LinkedList<Item>();
		readMap(path);
		setMapPath(path);
	}

	public void setCurrent(boolean bool) {
		current = bool;
	}
}
