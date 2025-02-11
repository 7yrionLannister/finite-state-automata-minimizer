package dataStructures;


public class Vertex<E> implements Comparable<Vertex<E>> {
	public static enum Color {WHITE, GRAY, BLACK}
	/**It represents the object represented by the vertex.
	 */
	private E element;
	/**It represents the vertex color (state) in the traversal algorithms. 
	 */
	private Color color;
	/**It represents the associated predecessor vertex in the tree formed by the traversal algorithm. 
	 */
	private Vertex<E> predecessor;
	
	//BFS and Dijkstra Attributes
	/**It represents the distance between the last origin vertex in a traversal algorithm and this vertex.
	 */
	private int distance;
	
	//DFS Attributes
	/**It represents the total amount of iterations of DFS until the vertex is discovered. 
	 */
	private int discovered;
	/**It represents the total amount of iterations of DFS until the vertex is visited the second and last time. 
	 */
	private int finished;
	
	/**This creates a new vertex as from an object of type E with its state in white and its predecessor in null.
	 * @param element It represents the object that will be associated with the vertex.
	 */
	public Vertex(E element) {
		this.element = element;
		color = Color.WHITE;
		setPredecessor(null);
	}
	/**It allows to get the distance from the last origin vertex.
	 * @return An Integer that represents the distance from origin.
	 */
	public int getDistance() {
		return distance;
	}
	/**It allows to set the actual vertex distance.
	 * @param distance is an integer represents the actual vertex distance.
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
	/**It allows to get the related E object of the actual vertex.
	 * @return An E object that represents the related E object of the actual vertex.
	 */
	public E getElement(){
		return element;
	}
	/**It allows to set the related E object of the actual vertex.
	 * @param element is an E object that represents the related E object of the actual vertex.
	 */
	public void setElement(E element){
		this.element = element;
	}
	/**It allows to get the state of the actual vertex defined by a color.
	 * @return A State object that represents the state of the actual vertex.
	 */
	public Color getColor() {
		return color;
	}
	/**It allows to set the state of the actual vertex defining a color.
	 * @param color is a State object that represents the state of the actual vertex.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**It allows to get an integer that represents the total amount of iterations of DFS until the vertex is visited the second and last time.
	 * @return An Integer that represents the total amount of iterations of DFS until the vertex is visited the second and last time.
	 */
	public int getFinished() {
		return finished;
	}
	/**It allows to set an integer that represents the total amount of iterations of DFS until the vertex is visited the second and last time.
	 * @param finished is an integer that represents the total amount of iterations of DFS until the vertex is visited the second and last time.
	 */
	public void setFinished(int finished) {
		this.finished = finished;
	}
	/**It allows to get an integer that represents the total amount of iterations of DFS until the vertex is discovered.
	 * @return An integer that represents the total amount of iterations of DFS until the vertex is discovered.
	 */
	public int getDiscovered() {
		return discovered;
	}
	/**It allows to set an integer that represents the total amount of iterations of DFS until the vertex is discovered.
	 * @param discovered is an integer that represents the total amount of iterations of DFS until the vertex is discovered.
	 */
	public void setDiscovered(int discovered) {
		this.discovered = discovered;
	}
	/**This method compares if another vertex of E type is equals to the actual vertex.
	 * @return A boolean that indicates if the vertex another is equal to the actual one.
	 * @param another is a vertex which is going to be compare with the actual one.
	 * @throws ClassCastException if another is not a Vertex<E>.
	 */
	@Override
	public boolean equals(Object another) {
		Vertex<E> a = (Vertex<E>)another; 
		return a.element.equals(element);
	}
	/**This method compares the actual vertex distance with another one that arrives as parameter.
	 * @return An integer that indicates if the other vertex distance is bigger, less or equal than the actual vertex.
	 * @param other is a vertex which is going to be compare with the actual one. 
	 */
	@Override
	public int compareTo(Vertex<E> other) {
		return this.distance-other.distance;
	}
	/***It allows to get the actual vertex predecessor. 
	 * @return A Vertex that represents the actual vertex predecessor. 
	 */
	public Vertex<E> getPredecessor() {
		return predecessor;
	}
	/**It allows to set the actual vertex predecessor. 
	 * @param predecessor is a Vertex that represents the actual vertex predecessor.
	 */
	public void setPredecessor(Vertex<E> predecessor) {
		this.predecessor = predecessor;
	}
}
