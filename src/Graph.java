
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A directed graph data structure.
 * @param <T>
 */

public class Graph<T> {

  /** Vector<Vertex> of graph verticies */
  public List<Vertex<T>> verticies;

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
  
  /**
   * The method that finds shortest distances from src to all other vertices using Bellman-Ford algorithm.
   * The method also detects negative weight cycle
   * @param graph -- the graph on which algorithm runs
   * @param src -- the source vertex
   * @return result -- the result which contains vertices and shortest distances from source
   */
  public Map<String,Integer> BellmanFord(Graph<T> graph, int src)
  {
      int V = graph.verticessize();
      int E = graph.edgessize();
      int dist[] = new int[V];
      
      Map<String,Integer> result = new HashMap<String,Integer>();
   
      // Step 1: Initialize distances from src to all other
      // vertices as INFINITE
      for (int i=0; i<V; ++i)
          dist[i] = Integer.MAX_VALUE;
      dist[src] = 0;

      // Step 2: Relax all edges |V| - 1 times. A simple
      // shortest path from src to any other vertex can
      // have at-most |V| - 1 edges
      for (int i=1; i<V; ++i)
      {
          for (int j=0; j<E; ++j)
          {
              int u = graph.verticies.indexOf(graph.edges.get(j).getFrom());   
              int v = graph.verticies.indexOf(graph.edges.get(j).getTo());
              int weight = graph.edges.get(j).getCost();
              if (dist[u]!=Integer.MAX_VALUE &&
                  dist[u]+weight<dist[v])
                  dist[v]=dist[u]+weight;
          }
      }

      // Step 3: check for negative-weight cycles.  The above
      // step guarantees shortest distances if graph doesn't
      // contain negative weight cycle. If we get a shorter
      //  path, then there is a cycle.
      for (int j=0; j<E; ++j)
      {
          int u = verticies.indexOf(edges.get(j).getFrom()); 
          int v = verticies.indexOf(edges.get(j).getTo());
          int weight = edges.get(j).getCost();
          if (dist[u]!=Integer.MAX_VALUE &&
              dist[u]+weight<dist[v])
            System.out.println("Graph contains negative weight cycle");
      }
      
      for (int i=0; i<V; ++i)
    	  result.put(graph.verticies.get(i).name, dist[i]);
      
      return result ;
  }
  
  /**
   * The method that finds shortest distances from src to all other vertices using Djikstra algorithm.
   * @param graph -- the graph on which algorithm runs
   * @param src -- the source vertex
   * @return result -- the result which contains vertices and shortest distances from source
   */
  public Map<String,Integer> djikstra(Graph<T> graph, int src)
  {
	  int V = graph.verticessize();
      int dist[] = new int[V]; // The output array. dist[i] will hold
                               // the shortest distance from src to i

      // sptSet[i] will true if vertex i is included in shortest
      // path tree or shortest distance from src to i is finalized
      Boolean sptSet[] = new Boolean[V];
      
      Map<String,Integer> result = new HashMap<String,Integer>();

      // Initialize all distances as INFINITE and stpSet[] as false
      for (int i = 0; i < V; i++)
      {
          dist[i] = Integer.MAX_VALUE;
          sptSet[i] = false;
      }

      // Distance of source vertex from itself is always 0
      dist[src] = 0;

      // Find shortest path for all vertices
      for (int count = 0; count < V-1; count++)
      {
          // Pick the minimum distance vertex from the set of vertices
          // not yet processed. u is always equal to src in first
          // iteration.
          int u = minDistance(graph,dist, sptSet);

          // Mark the picked vertex as processed
          sptSet[u] = true;

          // Update dist value of the adjacent vertices of the
          // picked vertex.
          for (int v = 0; v < V; v++)
          {  
        	  Vertex<T> from = graph.verticies.get(u);
              Vertex<T> to = graph.verticies.get(v);

              // Update dist[v] only if is not in sptSet, there is an
              // edge from u to v, and total weight of path from src to
              // v through u is smaller than current value of dist[v]
              if (!sptSet[v] && from.findEdge(to)!= null &&
                      dist[u] != Integer.MAX_VALUE &&
                      dist[u]+ from.findEdge(to).getCost() < dist[v])
                  dist[v] = dist[u] + from.findEdge(to).getCost();
          }
      }

      for (int i=0; i<V; ++i)
    	  result.put(graph.verticies.get(i).name, dist[i]);
      
      return result ;
  }
  
  /**
   * Private method called by djikstra to find the vertex with 
   * minimum distance value,from the set of vertices not yet 
   * included in shortest path tree
   * 
   */
  int minDistance(Graph<T> graph,int dist[], Boolean sptSet[])
  {
      // Initialize min value
      int min = Integer.MAX_VALUE, min_index=-1;

      for (int v = 0; v < graph.verticessize(); v++)
          if (sptSet[v] == false && dist[v] <= min)
          {
              min = dist[v];
              min_index = v;
          }

      return min_index;
  }

  
  /**
   * The method that finds shortest distances between all pairs of vertices using Floyd Warshall algorithm.
   * @param graph -- the graph on which algorithm runs
   * @return result -- the result which contains shortest distances 
   */
  public int[][] floydWarshall(Graph<T> graph)
  {
	  int V = graph.verticessize();
      int dist[][] = new int[V][V];
      int i, j, k;

      /* Initialize the solution matrix same as edge weights.
         Or we can say the initial values of shortest distances
         are based on shortest paths considering no intermediate
         vertex. */
      for (i = 0; i < V; i++)
      {
          for (j = 0; j < V; j++)
          {
        	  Vertex<T> from = graph.verticies.get(i);
              Vertex<T> to = graph.verticies.get(j);
              if(from.findEdge(to)!= null)
              {
                dist[i][j] = from.findEdge(to).cost;
              }
              else if(i==j)
            	dist[i][j] = 0 ;
              else
            	dist[i][j] = 999;
          }
      }
      
      System.out.println("Graph adjacency matrix: ");
      for(i=0 ; i<V ; i++)
      {
    	  System.out.print(graph.verticies.get(i).name+":\t");
    	  for(j = 0 ; j <V ; j++)
    	  {
    		  System.out.print(dist[i][j]+"\t");
    	  }
    	 System.out.println();
      }
    	 
      /* Add all vertices one by one to the set of intermediate
         vertices.
        ---> Before start of a iteration, we have shortest
             distances between all pairs of vertices such that
             the shortest distances consider only the vertices in
             set {0, 1, 2, .. k-1} as intermediate vertices.
        ----> After the end of a iteration, vertex no. k is added
              to the set of intermediate vertices and the set
              becomes {0, 1, 2, .. k} */
      for (k = 0; k < V; k++)
      {
          // Pick all vertices as source one by one
          for (i = 0; i < V; i++)
          {
              // Pick all vertices as destination for the
              // above picked source
              for (j = 0; j < V; j++)
              {
                  // If vertex k is on the shortest path from
                  // i to j, then update the value of dist[i][j]
                  if (dist[i][k] + dist[k][j] < dist[i][j])
                      dist[i][j] = dist[i][k] + dist[k][j];
              }
          }
      }

      // Return the shortest distance matrix
      return dist;
  }
  
}


