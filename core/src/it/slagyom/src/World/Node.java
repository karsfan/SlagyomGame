package it.slagyom.src.World;

public class Node {
	private String info;
	private Enemy enemy;
	private Node children;

	public Node(String info) {
		this.info = info;
	}

	public void addEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void addChildren(Node children) {
		this.children = children;
	}

	public String getInfo() {
		return info;
	}

	public Node getChildren() {
		return children;
	}

}
