
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A directed graph data structure.
 * @param <T>
 */

public class Graph<T> {

  /** Vector<Vertex> of graph verticies */
  private List<Vertex<T>> verticies;

  /** Vector<Edge> of edges in the graph */
  private List<Edge<T>> edges;

  /** The vertex identified as the root of the graph */
  private Vertex<T> rootVertex;
  
  /** The list of vertices for BFS traversal */
  private List<Vertex<T>> bfsMarkedVerticies = new ArrayList<Vertex<T>>();
  
  /** The list of vertices for DFS traversal */
  private List<Vertex<T>> dfsMarkedVerticies = new ArrayList<Vertex<T>>();
  
  /**
   * Construct a new graph without any vertices or edges
   */
  public Graph() {
    verticies = new ArrayList<Vertex<T>>();
    edges = new ArrayList<Edge<T>>();
  }

  /**
   * Are there any verticies in the graph
   * 
   * @return true if there are no verticies in the graph
   */
  public boolean isEmpty() {
    return verticies.size() == 0;
  }

  /**
   * Add a vertex to the graph
   * 
   * @param v
   *          the Vertex to add
   * @return true if the vertex was added, false if it was already in the graph.
   */
  public boolean addVertex(Vertex<T> v) {
    boolean added = false;
    if (verticies.contains(v) == false) {
      added = verticies.add(v);
    }
    return added;
  }

  /**
   * Get the vertex count.
   * 
   * @return the number of verticies in the graph.
   */
  public int verticessize() {
    return verticies.size();
  }
  
  /**
   * Get the edges count.
   * 
   * @return the number of edges in the graph.
   */
  public int edgessize() {
    return edges.size();
  }

  /**
   * Get the root vertex
   * 
   * @return the root vertex if one is set, null if no vertex has been set as
   *         the root.
   */
  public Vertex<T> getRootVertex() {
    return rootVertex;
  }

  /**
   * Set a root vertex. If root does no exist in the graph it is added.
   * 
   * @param root -
   *          the vertex to set as the root and optionally add if it does not
   *          exist in the graph.
   */
  public void setRootVertex(Vertex<T> root) {
    this.rootVertex = root;
    if (verticies.contains(root) == false)
      this.addVertex(root);
  }

  /**
   * Get the given Vertex.
   * 
   * @param n
   *          the index [0, size()-1] of the Vertex to access
   * @return the nth Vertex
   */
  public Vertex<T> getVertex(int n) {
    return verticies.get(n);
  }

  /**
   * Get the graph vertices
   * 
   * @return the graph vertices
   */
  public List<Vertex<T>> getVertices() {
    return this.verticies;
  }

  /**
   * Insert a directed, weighted Edge<T> into the graph.
   * 
   * @param from -
   *          the Edge<T> starting vertex
   * @param to -
   *          the Edge<T> ending vertex
   * @param cost -
   *          the Edge<T> weight/cost
   * @return true if the Edge<T> was added, false if from already has this Edge<T>
   * @throws IllegalArgumentException
   *           if from/to are not verticies in the graph
   */
  public boolean addEdge(Vertex<T> from, Vertex<T> to, int cost,String connection) throws IllegalArgumentException {
    if (verticies.contains(from) == false)
      throw new IllegalArgumentException("from is not in graph");
    if (verticies.contains(to) == false)
      throw new IllegalArgumentException("to is not in graph");

    Edge<T> e = new Edge<T>(from, to, cost,connection);
    if (from.findEdge(to) != null)
      return false;
    else {
      from.addEdge(e);
      to.addEdge(e);
      edges.add(e);
      return true;
    }
  }


  /**
   * Get the graph edges
   * 
   * @return the graph edges
   */
  public List<Edge<T>> getEdges() {
    return this.edges;
  }

  /**
   * Search the verticies for one with name.
   * 
   * @param name -
   *          the vertex name
   * @return the first vertex with a matching name, null if no matches are found
   */
  public Vertex<T> findVertexByName(String name) {
    Vertex<T> match = null;
    for (Vertex<T> v : verticies) {
      if (name.equals(v.getName())) {
        match = v;
        break;
      }
    }
    return match;
  }

  /**
   * Search the verticies for one with rating.
   * 
   * @param rating -
   *          the vertex rating to match
   * @param compare -
   *          the comparator to perform the match
   * @return the first vertex with a matching rating, null if no matches are found
   */
  public Vertex<T> findVertexByRating(String rating, Comparator<String> compare) {
    Vertex<T> match = null;
    for (Vertex<T> v : verticies) {
      if (compare.compare(rating, v.getRating()) == 0) {
        match = v;
        break;
      }
    }
    return match;
  }
  
  /**
   * Perform a depth first serach using recursion. 
   * @param v -
   *          the Vertex to start the search from
   */
  public List<Vertex<T>> depthFirstSearch(Vertex<T> v){
    v.visit();
    dfsMarkedVerticies.add(v);
    for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
      Edge<T> e = v.getOutgoingEdge(i);
      Vertex<T> to = e.getTo();
      if (!to.visited())
      {
        depthFirstSearch(e.getTo());
      }
    }
    clearMark();
    return dfsMarkedVerticies ;
  }
  
  /**
   * Perform a breadth first search 
   * @param v -
   *          the Vertex to start the search from
   */
  public List<Vertex<T>> breadthFirstSearch(Vertex<T> v)
  {
    LinkedList<Vertex<T>> q = new LinkedList<Vertex<T>>();
    q.add(v);
    v.visit();	    
    while (!q.isEmpty()) {
      v = q.removeFirst(); 
      bfsMarkedVerticies.add(v);
      for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
        Edge<T> e = v.getOutgoingEdge(i);
        Vertex<T> to = e.getTo();
        if (!to.visited()) {
          q.add(to);
          to.visit();
        }
      }
    }
    clearMark();
    return bfsMarkedVerticies ;
  }  
 
  /**
   * Private method called by DFS,BFS to clear the marked
   * vertices,so that it wont affect another call of
   * traversal 
   * 
   */
  private void clearMark() {
	for (Vertex<T> v : verticies)
		v.mark = false ;
  }

  /**
   * Gives a subgraph
   * @param graph -
   *          the graph from which subgraph is to be obtained
   * @param verticies -
   *          the list of verticies in subgraph
   * @return subgraph -
   *          the resultant subgraph 
   */
  public Graph<T> Subgraph(Graph<T> graph, List<Vertex<T>> verticies) {
	
	    Graph<T> subgraph = new Graph<T>();
	    
	    ArrayList<Vertex<T>> subgraphverticies = new ArrayList<Vertex<T>>();
	    
	    Vertex<T> vertex_from = null,vertex_to=null ;
	    
	    //creating vertices with same attribute information as main graph
	    for (Vertex<T> vertex : verticies) {
	    	String id  = vertex.name;
	         String type  = vertex.type;
	         String data = vertex.data;
	         String note  = vertex.note;
	         String authority = vertex.authority;
	         String trust = vertex.trust;
	         String rating = vertex.rating;
	         subgraphverticies.add(new Vertex<T>(id,type,data,note,authority,trust,rating));  
	    }
	    
	    //adding vertices to subgraph
	    Iterator<Vertex<T>>  iterator = subgraphverticies.iterator();
		while (iterator.hasNext()) {
			subgraph.addVertex(iterator.next());
		}
	    
	 	for (Vertex<T> temp : verticies )
        {
	 	   //int i = 0,j = 0;
	 	   for(Vertex<T> fromVertex : subgraphverticies)
	 	    {
	 	       if((temp.name).equals(fromVertex.name))
	 	       {
       	          vertex_from = fromVertex ;
       	          //i = subgraphverticies.indexOf(fromVertex);
	 	       }
	 	    }	        	 
       	   for(Vertex<T> temp2 : verticies)
       	    {
       		   for(Vertex<T> toVertex : subgraphverticies)
	 	         {
	 	            if((temp2.name).equals(toVertex.name))
	 	              {
	 	            	vertex_to = toVertex ;
	 	            	//j = subgraphverticies.indexOf(toVertex);
       	                  
	 	              }
	 	          }
       		  
	 			 if (graph.edges.contains(temp.findEdge(temp2)))
	 			  {
	 				 Edge<T> e = temp.findEdge(temp2);
	 				 int cost = e.getCost();
	 				 String connection = e.getConnection();
	                 //subgraph.addEdge(subgraphverticies.get(i),subgraphverticies.get(j),0,"PRO");
	 			     subgraph.addEdge(vertex_from,vertex_to,cost,connection);
	 		      }
	         }
     }		
	   return subgraph;
   }
  
}

