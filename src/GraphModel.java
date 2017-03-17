/**
   Graph model class for testing
   Version 1.0
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class GraphModel
{
	static Vertex<String> ST1,ST2,C1,C2,C3,C4,E1,E2,E3,E4,S1,S2,S3,S4,S5 ;
	static List<Vertex<String>> verticies = new ArrayList<Vertex<String>>();
	static List<Vertex<String>> bfsVertices ;
	static List<Vertex<String>> dfsVertices ;
	static Map<String,Integer> bellmanFordResult = new HashMap<String,Integer>();
	static Map<String,Integer> djikstraResult = new HashMap<String,Integer>();
	static int[][] floydWarshallResult ;
	
	public static void main(String[] a)
	{
	  Graph<String> graph = new Graph<String>();
	  
	  Graph<String> subgraph;
	  
	  //creating vertices with their attributes
	  
	  ST1 = new Vertex<String>("ST1","Stance","Yes");
	  
	  C1 = new Vertex<String>("C1","Claim","Java is great for web development");
	  
	  E1 = new Vertex<String>("E1","Evidence","Java can be used in servlet containers and JSP");
	  
	  S1 = new Vertex<String>("S1","Source","Java Wikipedia page");
	  
	  E2 = new Vertex<String>("E2","Evidence","Java is used in Hadoop and distributed file systems");
	  
	  S2 = new Vertex<String>("S2","Source","Apache Hadoop Page");

      C2 = new Vertex<String>("C2","Claim","Java is great for writing applications");
    
      E3 = new Vertex<String>("E3","Evidence","Java is used in writing academic applications");
	  
      S3 = new Vertex<String>("S3","Source","Some website showing this");
  
      ST2 = new Vertex<String>("ST2","Stance","No");
      
	  C3 = new Vertex<String>("C3","Claim","Java is very slow");
	  
	  E4 = new Vertex<String>("E4","Evidence","Java is an interpreted language and is very slow");
	  
	  S4 = new Vertex<String>("S4","Source","Horstmann Java Textbook");

	  C4 = new Vertex<String>("C4","Claim","Java requires too much scaffolding for writing programs");
	  
	  S5 = new Vertex<String>("S5","Source","Lewis Java Textbook");
	  	  
	  //adding all the vertices to the graph
	  
	  graph.addVertex(ST1);
	  graph.addVertex(ST2);
	  graph.addVertex(C1);
	  graph.addVertex(C2);
	  graph.addVertex(C3);
	  graph.addVertex(C4);
	  graph.addVertex(E1);
	  graph.addVertex(E2);
	  graph.addVertex(E3);
	  graph.addVertex(E4);
	  graph.addVertex(S1);
	  graph.addVertex(S2);
	  graph.addVertex(S3);
	  graph.addVertex(S4);
	  graph.addVertex(S5);
	 
	  //adding edges between vertices
	  
	  graph.addEdge(ST1, C1,2,"PRO");
	  graph.addEdge(C1, E1,1,"PRO");
	  graph.addEdge(E1,S1,3,"PRO");
	  graph.addEdge(C1, E2,1,"PRO");
	  graph.addEdge(E2,S2,3,"PRO");
	  graph.addEdge(ST1, C2,2,"PRO");
	  graph.addEdge(C2, E3,1,"PRO");
	  graph.addEdge(E3,S3,3,"PRO");
	  graph.addEdge(ST2, C3,2,"PRO");
	  graph.addEdge(C3, E4,1,"PRO");
	  graph.addEdge(E4,S4,3,"PRO");
	  graph.addEdge(ST2, C4,2,"PRO");
	  graph.addEdge(C4,S5,5,"PRO");
	  
      //displaying vertices in graph
	  
	  System.out.println("The information about vertices");
	  System.out.println("The number of vertices in graph is:" + graph.verticessize());
	  System.out.println(graph.getVertices());
	  System.out.println("---------------------------------------------");
	  
      //displaying edges in graph
	  
	  System.out.println("The information about edges");
	  System.out.println("The number of edges in graph is:" + graph.edgessize());
	  System.out.println(graph.getEdges());
	  System.out.println("---------------------------------------------");
	  	  
	  System.out.println("The information about BFS traversal for Yes stance");
	  bfsVertices = graph.breadthFirstSearch(ST1);
	  for(Vertex<String> vertex : bfsVertices)
		 {
			 System.out.println(vertex);
		 }
	  bfsVertices.clear();
	  System.out.println("---------------------------------------------");
	  
	  System.out.println("The information about BFS traversal for No stance");
	  bfsVertices = graph.breadthFirstSearch(ST2);
	  for(Vertex<String> vertex : bfsVertices)
		 {
			 System.out.println(vertex);
		 }
	  bfsVertices.clear();
	  System.out.println("---------------------------------------------");
	  
	  System.out.println("The information about DFS traversal for Yes stance");
	  dfsVertices = graph.depthFirstSearch(ST1);
	  for(Vertex<String> vertex : dfsVertices)
		 {
			 System.out.println(vertex);
		 }
	  dfsVertices.clear();
	  System.out.println("---------------------------------------------");
	  
	  System.out.println("The information about DFS traversal for No stance");
	  dfsVertices = graph.depthFirstSearch(ST2);
	  for(Vertex<String> vertex : dfsVertices)
		 {
			 System.out.println(vertex);
		 }
	  dfsVertices.clear();
	  System.out.println("---------------------------------------------");
	  
	  verticies.add(ST1);
	  verticies.add(C1);
	  verticies.add(C2);
	  verticies.add(E1);
	  verticies.add(E2);
	  verticies.add(E3);
	  verticies.add(S1);
	  verticies.add(S2);
	  verticies.add(S3);
	  subgraph = graph.Subgraph(graph,verticies);
	  
	  System.out.println("SUBGRAPH for Stance Yes");

	  System.out.println("The number of vertices in graph is:" + subgraph.verticessize());
	  System.out.println("The information about vertices");
	  System.out.println(subgraph.getVertices());
	  
	  System.out.println("--------------------------");
	  
	  System.out.println("The number of edges in graph is:" + subgraph.edgessize());
	  System.out.println("The information about edges");
	  System.out.println(subgraph.getEdges());
	  
	  System.out.println("---------------------------------------------");
	  
	  verticies.clear();
	  verticies.add(ST2);
	  verticies.add(C3);
	  verticies.add(C4);
	  verticies.add(E4);
	  verticies.add(S4);
	  verticies.add(S5);
	  subgraph = graph.Subgraph(graph,verticies);
	  
	  System.out.println("SUBGRAPH for Stance No");

	  System.out.println("The number of vertices in graph is:" + subgraph.verticessize());
	  System.out.println("The information about vertices");
	  System.out.println(subgraph.getVertices());
	  
	  System.out.println("--------------------------");
	  
	  System.out.println("The number of edges in graph is:" + subgraph.edgessize());
	  System.out.println("The information about edges");
	  System.out.println(subgraph.getEdges());
	  
	  System.out.println("---------------------------------------------");
	  
	  bellmanFordResult = graph.BellmanFord(graph, 0);
	  System.out.println("Bellman ford from Stance1:");
	  System.out.println(bellmanFordResult);
	  
	  System.out.println("---------------------------------------------");
	  
	  bellmanFordResult = graph.BellmanFord(graph, 1);
	  System.out.println("Bellman ford from Stance2:");
	  System.out.println(bellmanFordResult);
	  
	  System.out.println("---------------------------------------------");
	  
	  bellmanFordResult = graph.BellmanFord(graph, 3);
	  System.out.println("Bellman ford from Claim2:");
	  System.out.println(bellmanFordResult);
	  
	  System.out.println("---------------------------------------------");
	  
	  djikstraResult = graph.BellmanFord(graph, 0);
	  System.out.println("Djikstra from Stance1:");
	  System.out.println(djikstraResult);
	  
	  System.out.println("---------------------------------------------");
	  
	  djikstraResult = graph.BellmanFord(graph, 1);
	  System.out.println("Djikstra from Stance2:");
	  System.out.println(djikstraResult);
	  
	  System.out.println("---------------------------------------------");
	  
	  djikstraResult = graph.BellmanFord(graph, 3);
	  System.out.println("Djikstra from Claim2:");
	  System.out.println(djikstraResult);
	  
	  System.out.println("---------------------------------------------");
	  
	  floydWarshallResult = new int[graph.verticessize()][graph.verticessize()];
	  floydWarshallResult = graph.floydWarshall(graph);
	  System.out.println("Floyd Warshalls Algorithm:");
	  for (int i=0; i<graph.verticessize(); ++i)
      {
		  System.out.print(graph.verticies.get(i).name +":\t");
          for (int j=0; j<graph.verticessize(); ++j)
          {
                  System.out.print(floydWarshallResult[i][j]+"\t");
          }
          System.out.println();
      }
	  
	  System.out.println("---------------------------------------------");
	  
	}
}