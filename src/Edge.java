/**
 * A directed, weighted edge in a graph
 * @param <T>
 */
class Edge<T> {
  protected Vertex<T> from;

  protected Vertex<T> to;

  protected int cost;
  
  protected String connection ;

  /**
   * Create a zero cost edge between from and to
   * 
   * @param from
   *          the starting vertex
   * @param to
   *          the ending vertex
   */
  public Edge(Vertex<T> from, Vertex<T> to) {
    this(from, to, 0 , "PRO");
  }

  /**
   * Create an edge between from and to with the given cost.
   * 
   * @param from
   *          the starting vertex
   * @param to
   *          the ending vertex
   * @param cost
   *          the cost of the edge
   */
  public Edge(Vertex<T> from, Vertex<T> to, int cost , String connection) {
    this.from = from;
    this.to = to;
    this.cost = cost;
    this.connection = connection;
  }

  /**
   * Get the ending vertex
   * 
   * @return ending vertex
   */
  public Vertex<T> getTo() {
    return to;
  }

  /**
   * Get the starting vertex
   * 
   * @return starting vertex
   */
  public Vertex<T> getFrom() {
    return from;
  }

  /**
   * Get the cost of the edge
   * 
   * @return cost of the edge
   */
  public int getCost() {
    return cost;
  }

  /**
   * Get the connection of the edge
   * 
   * @return connection of the edge
   */
  public String getConnection() {
    return connection;
  }

  
  /**
   * String rep of edge
   * 
   * @return string rep with from/to vertex names and cost
   */
  public String toString() {
    StringBuffer tmp = new StringBuffer("Edge[from: ");
    tmp.append(from.getName());
    tmp.append(",to: ");
    tmp.append(to.getName());
    tmp.append(", cost: ");
    tmp.append(cost);
    tmp.append(", connection: ");
    tmp.append(connection);
    tmp.append("]");
    return tmp.toString();
  }
}
